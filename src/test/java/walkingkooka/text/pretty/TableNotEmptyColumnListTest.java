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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class TableNotEmptyColumnListTest extends TableTestCase2<TableNotEmptyColumnList>
        implements ListTesting2<TableNotEmptyColumnList, CharSequence> {

    private final static int COLUMN = 1;

    @Test
    public void testImmutableList() {
        final TableNotEmptyColumnList list = this.createList();
        assertSame(
                list,
                Lists.immutable(list)
        );
    }

    @Test
    public void testGet() {
        this.getAndCheck(
                this.createList(),
                0,
                R0C1
        );
    }

    @Test
    public void testGet2() {
        this.getAndCheck(
                this.createList(),
                2,
                R2C1
        );
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(
                this.createList(),
                3
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createList(),
                Lists.of(R0C1, R1C1, R2C1).toString()
        );
    }

    @Override
    public TableNotEmptyColumnList createObject() {
        return this.createList();
    }

    @Override
    public TableNotEmptyColumnList createList() {
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

        final TableNotEmptyListRows tableRows = TableNotEmptyListRows.empty();

        int r = 0;
        for(final List<CharSequence> row : rows) {
            final TableNotEmptyListRow tableRow = TableNotEmptyListRow.empty();

            int c = 0;
            for(final CharSequence text : row) {
                tableRow.setAuto(
                        c,
                        text
                );
                c++;
            }

            tableRows.setAuto(
                    r,
                    tableRow
            );

            r++;
        }

        final int width = 3;
        tableRows.setWidth(width);

        return TableNotEmptyColumnList.with(
                COLUMN,
                TableNotEmpty.with(
                        tableRows,
                        width
                )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TableNotEmptyColumnList> type() {
        return TableNotEmptyColumnList.class;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNamePrefix() {
        return TableNotEmpty.class.getSimpleName();
    }
}
