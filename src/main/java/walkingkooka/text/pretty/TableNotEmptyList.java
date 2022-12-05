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

import javaemul.internal.annotations.GwtIncompatible;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * An immutable {@link java.util.List} that includes a reference to the parent {@link TableNotEmpty}
 */
abstract class TableNotEmptyList<T> extends AbstractList<T> {

    final static int INITIAL_CAPACITY = 3;

    static int computeCapacity(final int min) {
        return min / INITIAL_CAPACITY * INITIAL_CAPACITY + INITIAL_CAPACITY;
    }

    TableNotEmptyList(final Object[] elements) {
        super();
        this.elements = elements;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final T get(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Invalid " + this.elementLabel() + " " + index + " < 0");
        }
        final int size = this.size();
        if (index >= size) {
            throw new IndexOutOfBoundsException("Invalid " + this.elementLabel() + " " + index + " >= " + size);
        }

        T element;

        if (index >= this.elementCount) {
            element = this.missing();
        } else {
            final Object[] elements = this.elements;
            element = (T) elements[index];
            if (null == element) {
                element = this.missing();
            }
        }

        return element;
    }

    /**
     * Used when forming an {@link IndexOutOfBoundsException} message, rather than mention index this word will be used.
     */
    abstract String elementLabel();

    /**
     * The value that is returned when an element is null or missing from the {@link #elements}.
     */
    abstract T missing();

    @Override
    public boolean isEmpty() {
        return false; // a TableNotEmpty width or height is never 0.
    }

    // mutable TableNotEmpty only methods...............................................................................

    @SuppressWarnings("unchecked")
    final void setAuto(final int index,
                       final T element) {
        final Object[] elements = this.elements;
        final int elementCount = this.elementCount;
        final int size = this.size;

        if(this.isMissing(element)) {
            if (index < elements.length) {
                elements[index] = null;

                if(size -1 == index) {
                    // need to update elementCount with the first non null element
                    int newElementCount = elementCount - 1;

                    // last element has become null, need to shrink $elementCount
                    while (newElementCount >= 0) {
                        if (!isMissing((T) elements[newElementCount])) {
                            break;
                        }
                        newElementCount--;
                    }

                    this.elementCount = newElementCount + 1;
                } else {
                    this.size = Math.max(
                            size,
                            index + 1
                    );
                }
            } else {
                this.size = Math.max(
                        size,
                        index + 1
                );
            }
        } else {
            if (index < elements.length) {
                elements[index] = element;

                this.elementCount = Math.max(
                        elementCount,
                        index + 1
                );

                this.size = Math.max(
                        size,
                        index + 1
                );
            } else {
                // auto expand...
                final Object[] newElements = new Object[computeCapacity(index)];
                System.arraycopy(
                        elements,
                        0,
                        newElements,
                        0,
                        elementCount
                );
                newElements[index] = element;

                this.elements = newElements;
                this.elementCount = index + 1;
                this.size = index + 1;
            }
        }
    }

    abstract boolean isMissing(final T element);

    abstract void setWidth(final int width);

    abstract TableNotEmptyList<T> copy();

    /**
     * The elements in this {@link List}.
     */
    Object[] elements;

    /**
     * The true number of elements in {@link #elements}.
     */
    int elementCount = 0;

    /**
     * The size of this List, including nulls.
     */
    int size;

    @Override
    public final int hashCode() {
        return Objects.hash(
                this.elements
        );
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                other instanceof TableNotEmptyListRow ?
                this.equalsTableNotEmptyListRow((TableNotEmptyListRow) other) :
                other instanceof TableNotEmptyListRows ?
                        this.equalsTableNotEmptyListRows((TableNotEmptyListRows) other) :
                        other instanceof List ?
                                this.equalsList((List<?>) other) :
                                false;
    }

    final boolean equalsTableNotEmptyList(final TableNotEmptyList<?> other) {
        boolean equals = false;

        final int count = this.elementCount;
        if(count == other.elementCount && this.size == other.size) {
            // Gwt jre Arrays.equals(Object[], int, Object[], int, int) is not implemented.
            final Object[] elements = this.elements;
            final Object[] otherElements = other.elements;

            for(int i = 0; i < count; i++) {
                equals = Objects.equals(
                        elements[i],
                        otherElements[i]
                );
                if(!equals) {
                    break;
                }
            }
        }

        return equals;
    }

    abstract boolean equalsTableNotEmptyListRow(final TableNotEmptyListRow other);

    abstract boolean equalsTableNotEmptyListRows(final TableNotEmptyListRows other);

    private boolean equalsList(final List<?> other) {
        return super.equals(other);
    }

    @GwtIncompatible
    final String toStringTest() {
        return this.elementCount + "/" + this.size + " " + Arrays.toString(this.elements);
    }
}
