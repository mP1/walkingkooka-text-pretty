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

import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.pretty.ColumnConfig;
import walkingkooka.text.pretty.Table;
import walkingkooka.text.pretty.TableConfig;
import walkingkooka.text.pretty.TextPretty;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printers;

public final class TableColumnPrintSample {
    public static void main(final String[] ignored) {
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

        // print row by row
        try (final IndentingPrinter printer = Printers.sysOut().indenting(Indentation.with("  "))) {
            for (int i = 0; i < formattedTable.maxRow(); i++) {
                printer.print(TextPretty.rowColumnsToLine((column -> 2), LineEnding.SYSTEM)
                        .apply(formattedTable.row(i)));
            }
        }
    }
}
