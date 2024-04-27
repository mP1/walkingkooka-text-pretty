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
 * An immutable {@link List} view of a single row, where the elements are non null {@link CharSequence}.
 */
final class TableNotEmptyListRow extends TableNotEmptyList<CharSequence> {

    static TableNotEmptyListRow alwaysEmpty() {
        return with(0);
    }

    static TableNotEmptyListRow empty() {
        return with(INITIAL_CAPACITY);
    }

    static TableNotEmptyListRow with(final int initialCapacity) {
        return new TableNotEmptyListRow(new Object[initialCapacity]);
    }

    private TableNotEmptyListRow(final Object[] elements) {
        super(elements);
    }

    @Override
    String elementLabel() {
        return "column";
    }

    @Override
    CharSequence missing() {
        return MISSING;
    }

    // @VisibleForTesting
    final static CharSequence MISSING = Table.MISSING_TEXT;

    @Override
    boolean isMissing(final CharSequence text) {
        return null == text || text.length() == 0;
    }

    @Override
    public int size() {
        return this.width;
    }

    @Override
    void setWidth(final int width) {
        this.width = width;

        final int elementCount = this.elementCount;

        // garbage collect deleted elements
        for (int c = width; c < elementCount; c++) {
            this.elements[c] = null;
        }

        this.size = Math.min(
                this.size,
                width
        );
    }

    int width;

    @Override
    TableNotEmptyListRow copy() {
        final int elementCount = this.elementCount;

        // GWT Object.clone() not implemented
        final Object[] elements = this.elements;
        final Object[] elementsCopy = new Object[elements.length];

        System.arraycopy(
                elements,
                0,
                elementsCopy,
                0,
                elementCount
        );

        final TableNotEmptyListRow copy = new TableNotEmptyListRow(elementsCopy);
        copy.size = this.size;
        copy.elementCount = elementCount;
        copy.width = this.width;
        return copy;
    }

    void copy(final List<CharSequence> rowText) {
        int i = 0;

        for(final CharSequence text : rowText) {
            this.setAuto(
                    i,
                    text
            );
            i++;
        }
    }

    @Override
    boolean equalsTableNotEmptyListRow(final TableNotEmptyListRow other) {
        return this.equalsTableNotEmptyList(other);
    }

    @Override
    boolean equalsTableNotEmptyListRows(final TableNotEmptyListRows other) {
        return false;
    }
}
