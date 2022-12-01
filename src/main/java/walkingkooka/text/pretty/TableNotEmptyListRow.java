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

import java.util.List;

/**
 * An immutable {@link List} view of a single row. Element indicies become the column coordinate.
 */
final class TableNotEmptyListRow extends TableNotEmptyList {

    static TableNotEmptyListRow with(final int row,
                                     final TableNotEmpty table) {
        return new TableNotEmptyListRow(row, table);
    }

    private TableNotEmptyListRow(final int row,
                                 final TableNotEmpty table) {
        super(table);
        this.row = row;
    }

    @Override
    CharSequence cell(final int column) {
        return this.table.cell(
                column,
                this.row
        );
    }

    @Override
    String columnOrRow() {
        return "column";
    }

    @Override
    public int size() {
        return this.table.width();
    }

    private final int row;
}
