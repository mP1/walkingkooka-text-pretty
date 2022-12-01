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

import java.util.AbstractList;

/**
 * An immutable {@link java.util.List} for a single row or column.
 */
abstract class TableNotEmptyList extends AbstractList<CharSequence> {

    static {
        Lists.registerImmutableType(TableNotEmptyListColumn.class);
        Lists.registerImmutableType(TableNotEmptyListRow.class);
    }

    TableNotEmptyList(final TableNotEmpty table) {
        super();
        this.table = table;
    }

    @Override
    public final CharSequence get(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Invalid " + this.columnOrRow() + " < 0");
        }
        return this.cell(index);
    }

    abstract CharSequence cell(final int columnOrRow);

    /**
     * Index corresponds to the coordinate column/row
     */
    abstract String columnOrRow();

    final TableNotEmpty table;
}
