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

import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A {@link UnaryOperator} that applies the corresponding {@link ColumnConfig} to each column in a given {@link Table}.
 */
final class TableTransformerUnaryOperator implements UnaryOperator<Table> {

    static TableTransformerUnaryOperator with(final List<ColumnConfig> columns) {
        Objects.requireNonNull(columns, "columns");

        return new TableTransformerUnaryOperator(Lists.immutable(columns));
    }

    private TableTransformerUnaryOperator(final List<ColumnConfig> columns) {
        super();
        columns.forEach(TableTransformerUnaryOperator::checkColumnNotNull);
        this.columns = columns;
    }

    private static void checkColumnNotNull(final ColumnConfig column) {
        Objects.requireNonNull(column, "columns includes null");
    }

    // row/columns

    @Override
    public Table apply(final Table table) {
        Objects.requireNonNull(table, "table");

        final int width = table.width();
        final List<List<CharSequence>> newColumns = Lists.array();

        int c = 0;
        for (final ColumnConfig column : this.columns) {
            if (c >= width) {
                break;
            }

            newColumns.add(
                    column.apply(
                            table.column(c)
                    )
            );

            c++;
        }

        return table.setColumns(
                0, // startColumn
                0, // startRow
                newColumns
        );
    }

    private final List<ColumnConfig> columns;

    @Override
    public String toString() {
        return this.columns.toString();
    }
}
