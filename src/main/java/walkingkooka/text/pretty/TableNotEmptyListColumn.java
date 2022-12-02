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
 * An immutable {@link List} view of a single column. Element indicies become the row coordinate.
 */
final class TableNotEmptyListColumn extends TableNotEmptyList {

    static TableNotEmptyListColumn with(final int column,
                                        final TableNotEmpty table) {
        return new TableNotEmptyListColumn(column, table);
    }

    private TableNotEmptyListColumn(final int column,
                                    final TableNotEmpty table) {
        super(table);
        this.column = column;
    }

    @Override
    CharSequence cell(final int row) {
        return this.table.cell(
                this.column,
                row
        );
    }

    @Override
    String columnOrRow() {
        return "row";
    }

    @Override
    public int size() {
        return this.table.height();
    }

    private final int column;
}
