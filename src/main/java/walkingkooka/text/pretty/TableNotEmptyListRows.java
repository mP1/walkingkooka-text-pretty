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
import java.util.Objects;

/**
 * An immutable {@link List} view of a single row, where the elements are non null {@link CharSequence}.
 */
final class TableNotEmptyListRows extends TableNotEmptyList<TableNotEmptyListRow> {

    static TableNotEmptyListRows empty() {
        return with(INITIAL_CAPACITY);
    }

    static TableNotEmptyListRows with(final int initialCapacity) {
        return new TableNotEmptyListRows(new Object[initialCapacity]);
    }

    private TableNotEmptyListRows(final Object[] elements) {
        super(elements);
    }

    @Override
    String elementLabel() {
        return "row";
    }

    @Override
    TableNotEmptyListRow missing() {
        return this.missing;
    }

    final TableNotEmptyListRow missing = TableNotEmptyListRow.alwaysEmpty();

    @Override
    boolean isMissing(final TableNotEmptyListRow row) {
        return null == row;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    void setWidth(final int width) {
        this.missing.setWidth(width);

        for (int r = 0; r < this.elementCount; r++) {
            final TableNotEmptyListRow row = (TableNotEmptyListRow) this.elements[r];
            if (null != row) {
                row.setWidth(width);
            }
        }
    }

    int findAndSetWidth() {
        int width = 0;

        int row = this.size;
        for (final Object element : this.elements) {
            if (row <= 0) {
                break;
            }
            if (element instanceof TableNotEmptyListRow) {
                final TableNotEmptyListRow tableRow = (TableNotEmptyListRow) element;
                width = Math.max(width, tableRow.size);
            }

            row--;
        }

        this.setWidth(width);

        return width;
    }

    @Override
    TableNotEmptyListRows copy() {
        final Object[] elements = this.elements;
        final int elementCount = this.elementCount;

        final Object[] newElements = new Object[elements.length];
        for (int i = 0; i < elementCount; i++) {
            final Object element = elements[i];
            if (element instanceof TableNotEmptyListRow) {
                final TableNotEmptyListRow row = (TableNotEmptyListRow) element;
                newElements[i] = row.copy();
            }
        }

        final TableNotEmptyListRows copy = new TableNotEmptyListRows(newElements);
        copy.size = this.size;
        copy.elementCount = elementCount;
        copy.missing.width = this.missing.width;
        return copy;
    }


    @Override
    boolean equalsTableNotEmptyListRow(final TableNotEmptyListRow other) {
        return false;
    }

    @Override
    boolean equalsTableNotEmptyListRows(final TableNotEmptyListRows other) {
        return this.equalsTableNotEmptyList(other);
    }

    // ImmutableListDefaults............................................................................................

    @Override
    public void elementCheck(final TableNotEmptyListRow row) {
        Objects.requireNonNull(row, "row");
    }
}
