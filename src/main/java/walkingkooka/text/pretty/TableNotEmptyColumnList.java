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

import walkingkooka.collect.list.ImmutableList;

import java.util.AbstractList;
import java.util.List;

/**
 * An immutable {@link List} view of a single column. Element indicies given to methods like {@link #get(int)}
 * become the row coordinate to locate the cell.
 */
final class TableNotEmptyColumnList extends AbstractList<CharSequence> implements ImmutableList<CharSequence> {

    static TableNotEmptyColumnList with(final int column,
                                        final TableNotEmpty table) {
        return new TableNotEmptyColumnList(column, table);
    }

    private TableNotEmptyColumnList(final int column,
                                    final TableNotEmpty table) {
        super();
        this.column = column;
        this.table = table;
    }

    @Override
    public CharSequence get(final int row) {
        return this.table.cell(
                this.column,
                row
        );
    }

    private final int column;

    @Override
    public int size() {
        return this.table.height();
    }

    private final TableNotEmpty table;

    // ImmutableList....................................................................................................

    /**
     * Not possible to set new elements because the updated {@link Table} is not returned.
     */
    @Override
    public ImmutableList<CharSequence> setElements(final List<CharSequence> list) {
        return this.setElementsFailIfDifferent(list);
    }
}
