/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.text.pretty;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.StringName;
import walkingkooka.naming.StringPath;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printer;
import walkingkooka.text.printer.Printers;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class TreePrintingBiConsumerTest implements ClassTesting2<TreePrintingBiConsumer<StringPath, StringName>> {

    @Test
    public void testRoot() {
        this.printTreeAndCheck(Sets.of("/"), "");
    }

    @Test
    public void testLeaf() {
        this.printTreeAndCheck(Sets.of("/leaf"), "leaf\n");
    }

    @Test
    public void testLeaf2() {
        this.printTreeAndCheck(Sets.of("/branch/leaf"),
                "branch\n" +
                        ">leaf\n");
    }

    @Test
    public void testBranchAndLeaf() {
        this.printTreeAndCheck(Sets.of("/branch",
                "/branch/leaf"),
                "branch\n" +
                        ">leaf\n");
    }

    @Test
    public void testBranchAndLeaf2() {
        this.printTreeAndCheck(Sets.of("/branch", "/branch/leaf1", "/branch/leaf2"),
                "branch\n" +
                        ">leaf1\n" +
                        ">leaf2\n");
    }

    @Test
    public void testBranchWithLeafAndLeaf() {
        this.printTreeAndCheck(Sets.of("/branch", "/branch/leaf1", "/a-leaf2"),
                "branch\n" +
                        ">leaf1\n" +
                        "a-leaf2\n");
    }

    @Test
    public void testBranchWithLeafAndLeaf2() {
        this.printTreeAndCheck(Sets.of("/branch", "/branch/leaf1", "/z-leaf2"),
                "branch\n" +
                        ">leaf1\n" +
                        "z-leaf2\n");
    }

    @Test
    public void testBranchBranchLeaf() {
        this.printTreeAndCheck(Sets.of("/branch1/branch2/branch3/leaf"),
                "branch1/branch2/branch3\n" +
                        ">leaf\n");
    }

    @Test
    public void testBranchBranchLeafAndLeaf() {
        this.printTreeAndCheck(Sets.of("/branch1/branch2/branch3/leaf1", "/leaf2"),
                "branch1/branch2/branch3\n" +
                        ">leaf1\n" +
                        "leaf2\n");
    }

    @Test
    public void testBranchLeafBranchLeaf() {
        this.printTreeAndCheck(Sets.of("/branch1/leaf1", "/branch2/leaf2"),
                "branch1\n" +
                        ">leaf1\n" +
                        "branch2\n" +
                        ">leaf2\n");
    }

    @Test
    public void testBranchBranchLeafAndBranchLeaf() {
        this.printTreeAndCheck(Sets.of("/branch1/branch2/branch3/leaf1", "/branch1/leaf2"),
                "branch1\n" +
                        ">branch2/branch3\n" +
                        ">>leaf1\n" +
                        ">leaf2\n");
    }

    @Test
    public void testBranchBranchLeafAndBranchLeaf2() {
        this.printTreeAndCheck(Sets.of("/branch1/branch2/branch3/leaf1", "/branch1/leaf2", "/branch1/leaf3"),
                "branch1\n" +
                        ">branch2/branch3\n" +
                        ">>leaf1\n" +
                        ">leaf2\n" +
                        ">leaf3\n");
    }

    @Test
    public void testBranchTwoBranchLeaf() {
        this.printTreeAndCheck(Sets.of("/branch1/branch2/leaf1", "/branch1/branch3/leaf2"),
                "branch1\n" +
                        ">branch2\n" +
                        ">>leaf1\n" +
                        ">branch3\n" +
                        ">>leaf2\n");
    }

    @Test
    public void testBranchTwoBranchLeafVisit() {
        this.printTreeAndCheck2((p) -> TreePrintingBranches.VISITED,
                Sets.of("/branch1/branch3/leaf2", "/branch1/branch2/leaf1"),
                "branch1\n" +
                        ">branch3\n" +
                        ">>leaf2\n" +
                        ">branch2\n" +
                        ">>leaf1\n");
    }

    @Test
    public void testMixedTreePrintingBranches() {
        this.printTreeAndCheck2((p) -> p.toString().equals("/branch1/branch3") ? TreePrintingBranches.SORTED : TreePrintingBranches.VISITED,
                Sets.of("/branch1/branch3/leaf3", "/branch1/branch3/leaf2", "/branch1/branch2/leaf1", "/771", "/992", "/883", "/004"),
                "branch1\n" +
                        ">branch3\n" +
                        ">>leaf2\n" +
                        ">>leaf3\n" +
                        ">branch2\n" +
                        ">>leaf1\n" +
                        "771\n" +
                        "992\n" +
                        "883\n" +
                        "004\n");
    }

    private void printTreeAndCheck(final Set<String> paths,
                                   final String expected) {
        this.printTreeAndCheck2((p) -> TreePrintingBranches.SORTED,
                paths,
                expected);
    }

    private void printTreeAndCheck2(final Function<StringPath, TreePrintingBranches> branches,
                                    final Set<String> paths,
                                   final String expected) {
        final StringBuilder text = new StringBuilder();
        try (final Printer printer = Printers.stringBuilder(text, LineEnding.NL)) {
            new TestTreePrinting(branches)
                    .biConsumer()
                    .accept(paths.stream().map(StringPath::parse).collect(Collectors.toCollection(Sets::ordered)), printer.indenting(Indentation.with(">")));
        }

        this.checkEquals(expected,
                text.toString(),
                () -> paths.toString());
    }

    private static class TestTreePrinting implements TreePrinting<StringPath, StringName> {

        private TestTreePrinting(Function<StringPath, TreePrintingBranches> branches) {
            this.branches = branches;
        }

        @Override
        public TreePrintingBranches branches(final StringPath parent) {
            return this.branches.apply(parent);
        }

        private final Function<StringPath, TreePrintingBranches> branches;

        @Override
        public void branchBegin(final List<StringName> names, final IndentingPrinter printer) {
            final String path = names.stream()
                    .filter(n -> false == n.value().isEmpty())
                    .map(StringName::toString)
                    .collect(Collectors.joining("/"));
            if (false == path.isEmpty()) {
                printer.print(path + "\n");
                printer.indent();
            }
        }

        @Override
        public void branchEnd(final List<StringName> names, final IndentingPrinter printer) {
            final String path = names.stream()
                    .filter(n -> false == n.value().isEmpty())
                    .map(StringName::toString)
                    .collect(Collectors.joining("/"));
            if (false == path.isEmpty()) {
                printer.outdent();
            }
        }

        @Override
        public void children(final Set<StringPath> paths, final IndentingPrinter printer) {
            paths.forEach(p -> printer.print(p.name() + "\n"));
        }
    }


    // ClassTesting.....................................................................................................

    @Override
    public Class<TreePrintingBiConsumer<StringPath, StringName>> type() {
        return Cast.to(TreePrintingBiConsumer.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
