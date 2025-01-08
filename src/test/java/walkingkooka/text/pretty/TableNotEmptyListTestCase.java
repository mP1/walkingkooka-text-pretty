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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.ImmutableListTesting;
import walkingkooka.collect.list.Lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

@SuppressWarnings("unchecked")
public abstract class TableNotEmptyListTestCase<L extends TableNotEmptyList<T>, T> extends TableTestCase<L>
        implements ImmutableListTesting<L, T> {

    TableNotEmptyListTestCase() {
        super();
    }

    @Test
    public final void testImmutableList() {
        final L list = this.createList();
        assertSame(
                list,
                Lists.immutable(list)
        );
    }

    @Override
    public void testIsEmptyAndSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testGetAndIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testIteratorContainsAndCollection() {
        throw new UnsupportedOperationException();
    }

    @Test
    public final void testSetAutoWithNull() {
        this.setAutoAndCheck(
                this.createList(),
                0,
                null,
                TableNotEmptyList.INITIAL_CAPACITY,
                1
        );
    }

    @Test
    public final void testSetAutoWithNull2() {
        this.setAutoAndCheck(
                this.createList(),
                1,
                null,
                TableNotEmptyList.INITIAL_CAPACITY,
                2
        );
    }

    @Test
    public final void testSetAutoWithNull3() {
        this.setAutoAndCheck(
                this.createList(),
                199,
                null,
                TableNotEmptyList.INITIAL_CAPACITY,
                200 // size
        );
    }

    @Test
    public void testSetIncreasesSize() {
        final L list = this.createList();
        final T element = this.element(0);

        this.setAutoAndCheck(
                list,
                0,
                element,
                TableNotEmptyList.INITIAL_CAPACITY,
                1,
                element // 0
        );
    }

    @Test
    public void testSetIncreasesSize2() {
        final L list = this.createList();
        final T element = this.element(2);

        this.setAutoAndCheck(
                list,
                2,
                element,
                TableNotEmptyList.INITIAL_CAPACITY,
                3,
                null, // 0
                null, // 1
                element // 2
        );
    }

    @Test
    public void testSetExpands() {
        final L list = this.createList();
        final T element = this.element(5);

        this.setAutoAndCheck(
                list,
                5,
                element,
                6, // capacity
                6, // size
                null, // 0
                null, // 1
                null, // 2
                null, // 3
                null, // 4
                element // 5
        );
    }

    @Test
    public void testSetExpands2() {
        final L list = this.createList();
        final T element = this.element(6);

        this.setAutoAndCheck(
                list,
                6,
                element,
                9, // capacity
                7, // size
                null, // 0
                null, // 1
                null, // 2
                null, // 3
                null, // 4
                null, // 5
                element // 6
        );
    }

    @Test
    public void testSetExpands3() {
        final L list = this.createList();
        final T element = this.element(9);

        this.setAutoAndCheck(
                list,
                9,
                element,
                12, // capacity
                10, // size
                null, // 0
                null, // 1
                null, // 2
                null, // 3
                null, // 4
                null, // 5
                null, // 6
                null, // 7
                null, // 8
                element // 9
        );
    }

    @Test
    public void testSetExpands4() {
        final L list = this.createList();
        final T element = this.element(11);

        this.setAutoAndCheck(
                list,
                11,
                element,
                12, // capacity
                12, // size
                null, // 0
                null, // 1
                null, // 2
                null, // 3
                null, // 4
                null, // 5
                null, // 6
                null, // 7
                null, // 8
                null, // 9
                null, // 10
                element // 11
        );
    }

    @Test
    public void testSetAndClearLast() {
        final L list = this.createList();

        final T element0 = this.element(0);
        list.setAuto(0, element0);

        list.setAuto(0, null);

        this.check(
                list,
                3, // capacity
                1 // size
        );
    }

    @Test
    public void testSetAndClearLast2() {
        final L list = this.createList();

        final T element0 = this.element(0);
        list.setAuto(0, element0);

        final T element1 = this.element(1);
        list.setAuto(1, element1);

        list.setAuto(1, null);

        this.check(
                list,
                3, // capacity
                2, // size
                element0
        );
    }

    @Test
    public void testSetAndClearLast3() {
        final L list = this.createList();

        final T element0 = this.element(0);
        list.setAuto(0, element0);

        final T element1 = this.element(1);
        list.setAuto(1, element1);

        final T element2 = this.element(2);
        list.setAuto(2, element2);

        list.setAuto(2, null);

        this.check(
                list,
                3, // capacity
                3, // size
                element0,
                element1
        );
    }

    @Test
    public void testSetAndClearBefore() {
        final L list = this.createList();

        final T element0 = this.element(0);
        list.setAuto(0, element0);

        final T element1 = this.element(1);
        list.setAuto(1, element1);

        list.setAuto(0, null);

        this.check(
                list,
                3, // capacity
                2, // size
                null,
                element1
        );
    }

    @Test
    public void testSetAndClearBefore2() {
        final L list = this.createList();

        final T element0 = this.element(0);
        list.setAuto(0, element0);

        final T element1 = this.element(1);
        list.setAuto(1, element1);

        final T element2 = this.element(2);
        list.setAuto(2, element2);

        list.setAuto(0, null);

        this.check(
                list,
                3, // capacity
                3, // size
                null,
                element1,
                element2
        );
    }

    @Test
    public void testSetExpandAndClearLast() {
        final L list = this.createList();

        final T element0 = this.element(0);
        list.setAuto(0, element0);

        final T element1 = this.element(1);
        list.setAuto(1, element1);

        final T element2 = this.element(2);
        list.setAuto(2, element2);

        final T element3 = this.element(3);
        list.setAuto(3, element2);

        list.setAuto(3, null);

        this.check(
                list,
                6, //capacity
                4, // size
                element0,
                element1,
                element2
        );
    }

    @Test
    public void testSetExpandAndClearLast2() {
        final L list = this.createList();

        final T element0 = this.element(0);
        list.setAuto(0, element0);

        final T element1 = this.element(1);
        list.setAuto(1, element1);

        final T element2 = this.element(2);
        list.setAuto(2, element2);

        final T element3 = this.element(3);
        list.setAuto(3, element3);

        final T element4 = this.element(4);
        list.setAuto(4, element4);

        list.setAuto(4, null);

        this.check(
                list,
                6, // capacity
                5, // size
                element0,
                element1,
                element2,
                element3
        );
    }

    @Test
    public void testSetExpandAndClearAll() {
        final L list = this.createList();

        for (int i = 0; i < 10; i++) {
            list.setAuto(i, this.element(i));
        }

        for (int i = 0; i < 10; i++) {
            list.setAuto(i, null);
        }

        this.check(
                list,
                12, // capacity
                10 // size
        );
    }

    final void setAutoAndCheck(final L list,
                               final int index,
                               final T element,
                               final int capacity,
                               final int size,
                               final T... elements) {
        list.setAuto(
                index,
                element
        );

        this.check(
                list,
                capacity,
                size,
                elements
        );
    }

    final void check(final L list,
                     final int capacity,
                     final int size,
                     final T... elements) {
        this.checkEquals(
                elements.length,
                list.elementCount,
                () -> "elementCount " + Arrays.asList(list.elements)
        );

        this.checkEquals(
                this.list(elements),
                this.list(list.elements).subList(0, elements.length),
                "elements"
        );

        this.checkEquals(
                Collections.nCopies(
                        list.elements.length - elements.length,
                        null
                ),
                list(list.elements)
                        .subList(
                                elements.length,
                                list.elements.length
                        ),
                () -> "elements extra elements must be null"
        );

        this.checkEquals(
                capacity,
                list.elements.length,
                () -> "capacity " + Arrays.asList(list.elements)
        );

        this.checkEquals(
                size,
                list.size,
                () -> "size " + Arrays.asList(list.elements)
        );
    }

    abstract T element(final int index);

    final List<?> list(final Object... elements) {
        return Arrays.asList(elements);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return TableNotEmpty.class.getSimpleName() +
                List.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
