/*
 * Copyright © 2020 Miroslav Pokorny
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
 */
package test;


import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Assert;
import org.junit.Test;

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.StringName;
import walkingkooka.naming.StringPath;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.pretty.ColumnConfig;
import walkingkooka.text.pretty.Table;
import walkingkooka.text.pretty.TableConfig;
import walkingkooka.text.pretty.TextPretty;
import walkingkooka.text.pretty.TreePrinting;
import walkingkooka.text.pretty.TreePrintingBranches;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@J2clTestInput(JunitTest.class)
public class JunitTest {

    // uses walkingkooka.text.pretty.sample.*

    @Test
    public void testTreePrinting() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter printer = Printers.stringBuilder(printed, LineEnding.NL)
                .indenting(Indentation.SPACES2);

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
            public TreePrintingBranches branches(final StringPath parent) {
                return TreePrintingBranches.SORTED;
            }

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

        Assert.assertEquals("~/github/project\n" +
                        "  target\n" +
                        "    classes/java/walkingkooka/text/pretty\n" +
                        "      CharSequenceBiFunction.class\n" +
                        "      CharSequenceBiFunctionAlign.class\n" +
                        "    maven-archiver\n" +
                        "      pom.properties\n" +
                        "    maven-status/maven-compiler-plugin\n" +
                        "      compile/default-compile\n" +
                        "        createdFiles.lst\n" +
                        "        inputFiles.lst\n" +
                        "      testCompile/default-compile\n" +
                        "        createdFiles.lst\n" +
                        "        inputFiles.lst\n" +
                        "  jacoco.exec\n" +
                        "  walkingkooka-text-pretty-1.0-SNAPSHOT-sources.jar\n" +
                        "  walkingkooka-text-pretty-1.0-SNAPSHOT.jar\n",
                printed.toString());
    }

    @Test
    public void testTableColumnPrint() {
        // create three columns with different widths and alignments.
        final ColumnConfig states = TextPretty.columnConfig()
                .minWidth(20)
                .maxWidth(20)
                .leftAlign();

        final ColumnConfig population = TextPretty.columnConfig()
                .minWidth(10)
                .maxWidth(10)
                .rightAlign();

        final ColumnConfig money = TextPretty.columnConfig()
                .minWidth(12)
                .maxWidth(12)
                .characterAlign(CharPredicates.is('.'), 7);

        // populate table with 3 columns.
        final TableConfig tableConfig = TextPretty.tableConfig()
                .add(states)
                .add(population)
                .add(money);

        // create table with a single row from a csv line
        final Table table1 = TextPretty.table()
                .setRow(0, TextPretty.csv(',').apply("\"New South Wales\",10000000,$12.00"));

        // streaming a list of csv lines (different delimiters) and collect (aka add to table)
        final Table table123 = Lists.of(TextPretty.csv('/').apply("Queensland/4000000/$11.75"),
                        TextPretty.csv(';').apply("Tasmania;500000;$9.0"))
                .stream()
                .collect(table1.collectRow(1));

        // format the table with cells using the columns.
        final Table formattedTable = tableConfig.apply(table123);

        final StringBuilder printed = new StringBuilder();

        // print row by row
        try (final IndentingPrinter printer = Printers.stringBuilder(printed, LineEnding.NL)
                .indenting(Indentation.SPACES2)) {
            for (int i = 0; i < formattedTable.height(); i++) {
                printer.print(
                        TextPretty.rowColumnsToLine((column -> 2), LineEnding.SYSTEM)
                                .apply(formattedTable.row(i)));
            }
        }

        Assert.assertEquals("New South Wales         10000000      $12.00\n" +
                        "Queensland               4000000      $11.75\n" +
                        "Tasmania                  500000       $9.0\n",
                printed.toString());
    }
}
