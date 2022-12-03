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

/**
 * A {@link TableConfig} with no columns, also used as the starting point so columns can be added.
 */
final class TableConfigNotEmpty extends TableConfig {

    /**
     * A singleton
     */
    static TableConfigNotEmpty with(final List<ColumnConfig> columns) {
        return new TableConfigNotEmpty(columns);
    }

    /**
     * Use factory
     */
    private TableConfigNotEmpty(final List<ColumnConfig> columns) {
        super();
        this.columns = columns;
    }

    @Override
    TableConfig add0(final ColumnConfig column) {
        final List<ColumnConfig> copy = Lists.array();
        copy.addAll(this.columns);
        copy.add(column);
        return new TableConfigNotEmpty(copy);
    }

    // UnaryOperator....................................................................................................

    @Override
    Table apply0(final Table table) {
        Table result = table;
        final int width = table.width();

        int columnCounter = 0;
        for (final ColumnConfig column : this.columns) {
            if(columnCounter >= width) {
                break;
            }

            result = result.setColumn(
                    columnCounter,
                    column.apply(
                            result.column(columnCounter)
                    )
            );
            columnCounter++;
        }

        return result;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.columns.toString();
    }

    final List<ColumnConfig> columns;
}
