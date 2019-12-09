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

package walkingkooka.text.pretty.sample;

import walkingkooka.collect.set.Sets;
import walkingkooka.naming.StringName;
import walkingkooka.naming.StringPath;
import walkingkooka.text.Indentation;
import walkingkooka.text.pretty.TreePrinting;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class TreePrintingSample {

    /**
     * Prints...
     * <pre>
     * ~/github/project
     *   target
     *     classes/java/walkingkooka/text/pretty
     *       CharSequenceBiFunction.class
     *       CharSequenceBiFunctionAlign.class
     *     maven-archiver
     *       pom.properties
     *     maven-status/maven-compiler-plugin
     *       compile/default-compile
     *         createdFiles.lst
     *         inputFiles.lst
     *       testCompile/default-compile
     *         createdFiles.lst
     *         inputFiles.lst
     *   jacoco.exec
     *   walkingkooka-text-pretty-1.0-SNAPSHOT-sources.jar
     *   walkingkooka-text-pretty-1.0-SNAPSHOT.jar
     * </pre>
     */
    public static void main(final String[] ignored) {
        final IndentingPrinter printer = Printers.sysOut().indenting(Indentation.with("  "));

        // over simplified sample of this projects target directory.
        final Set<StringPath> paths = Sets.of(
                "/target/classes/java/walkingkooka/text/pretty/CharSequenceBiFunction.class",
                "/target/classes/java/walkingkooka/text/pretty/CharSequenceBiFunctionAlign.class", // some class files...
                "/target/maven-archiver/pom.properties",
                "/target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst",
                "/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst",
                "/target/maven-status/maven-compiler-plugin/testCompile/default-compile/createdFiles.lst",
                "/target/maven-status/maven-compiler-plugin/testCompile/default-compile/inputFiles.lst",
                "/jacoco.exec",
                "/walkingkooka-text-pretty-1.0-SNAPSHOT.jar",
                "/walkingkooka-text-pretty-1.0-SNAPSHOT-sources.jar")
                .stream()
                .map(s -> StringPath.parse("/~/github/project" + s))
                .collect(Collectors.toSet());

        new TreePrinting<StringPath, StringName>() {

            @Override
            public void branchBegin(final List<StringName> names, final IndentingPrinter printer) {
                final String path = this.toPath(names);
                if (false == path.isEmpty()) {
                    printer.print(path + "\n");
                    printer.indent();
                }
            }

            @Override
            public void branchEnd(final List<StringName> names, final IndentingPrinter printer) {
                final String path = this.toPath(names);
                if (false == path.isEmpty()) {
                    printer.outdent();
                }
            }

            private String toPath(final List<StringName> names) {
                return this.toPath(names, StringPath.SEPARATOR);
            }

            @Override
            public void children(final Set<StringPath> paths, final IndentingPrinter printer) {
                paths.forEach(p -> printer.print(p.name() + "\n"));
            }
        }.biConsumer()
                .accept(paths, printer);
    }
}
