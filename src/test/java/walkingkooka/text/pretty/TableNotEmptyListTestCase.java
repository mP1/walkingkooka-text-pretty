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
import walkingkooka.collect.list.ListTesting2;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.NavigableMap;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TableNotEmptyListTestCase<T extends TableNotEmptyList> extends TableTestCase2<T> implements ListTesting2<T, CharSequence> {

    final static int ROW = 3;
    final static int COLUMN = 3;

    TableNotEmptyListTestCase() {
        super();
    }

    @Test
    public final void testImmutableList() {
        final T list = this.createList();
        assertSame(list, Lists.immutable(list));
    }

    @Test
    public final void testGetInvalid() {
        this.getAndCheck(this.createList(), 3, CharSequences.empty());
    }

    @Test
    public final void testSetFails() {
        assertThrows(UnsupportedOperationException.class, () -> this.createList().set(0, "Z"));
    }

    @Test
    public final void testRemoveFails() {
        assertThrows(UnsupportedOperationException.class, () -> this.createList().remove(0));
    }

    @Override
    public final T createObject() {
        return this.createList();
    }

    @Override
    public final T createList() {
        final List<List<CharSequence>> rows = Lists.array();

        rows.add(
            Lists.of(
                    R0C0,
                    R0C1,
                    R0C2
            )
        );

        rows.add(
                Lists.of(
                        R1C0,
                        R1C1,
                        R1C2
                )
        );

        rows.add(
                Lists.of(
                        R2C0,
                        R2C1,
                        R2C2
                )
        );

        return this.createList(rows, COLUMN, ROW);
    }

    abstract T createList(final List<List<CharSequence>> rows,
                          final int column,
                          final int row);

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return TableNotEmptyList.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
