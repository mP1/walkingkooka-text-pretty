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

import walkingkooka.naming.Name;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

/**
 * A read only {@link List} that can be manipulated by {@link TreePrintingBiConsumerRequest}.
 */
final class TreePrintingBiConsumerRequestList<N extends Name> extends AbstractList<N> {

    static <N extends Name> TreePrintingBiConsumerRequestList<N> empty() {
        return new TreePrintingBiConsumerRequestList<>();
    }

    private TreePrintingBiConsumerRequestList() {
        super();
    }

    @Override
    public N get(final int index) {
        final int size = this.size();
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index " + index + " not between 0 and " + size);
        }
        return (N)this.elements[index];
    }

    /**
     * Equivalent functionality to {@link #add(Object)} but package private, leaving add throwing {@link UnsupportedOperationException}.
     */
    void append(final N element) {
        int size = this.size;
        Object[] elements = this.elements;

        if(size == elements.length) {
            Object[] expanded = new Object[ size * 2 ];
            System.arraycopy(elements, 0, expanded, 0, size);
            this.elements = expanded;
            elements = expanded;
        }
        elements[size] = element;
        this.size = 1 + size;
    }

    void pop() {
        if(0 == this.size) {
            throw new IllegalStateException("Cannot pop List empty");
        }
        this.size--;
    }

    private Object[] elements = new Object[ 4];

    @Override
    public int size() {
        return this.size;
    }

    private int size = 0;
}
