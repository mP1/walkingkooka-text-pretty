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

public final class TableNotEmptyListRowsTest extends TableNotEmptyListTestCase<TableNotEmptyListRows, TableNotEmptyListRow> {

    @Test
    public void testEmpty() {
        this.check(
                TableNotEmptyListRows.empty(),
                TableNotEmptyList.INITIAL_CAPACITY,
                0
        );
    }

    @Test
    public void testOneEmptyRow() {
        final TableNotEmptyListRow row = null;

        final TableNotEmptyListRows rows = TableNotEmptyListRows.empty();
        rows.setAuto(0, row);

        this.check(
                rows,
                TableNotEmptyList.INITIAL_CAPACITY,
                1
        );
    }

    @Test
    public void testOneRow() {
        final TableNotEmptyListRow row = TableNotEmptyListRow.empty();
        row.setAuto(0, "A");

        final TableNotEmptyListRows rows = TableNotEmptyListRows.empty();
        rows.setAuto(0, row);

        this.check(
                rows,
                TableNotEmptyList.INITIAL_CAPACITY,
                1,
                row
        );
    }

    @Test
    public void testTwoRows() {
        final TableNotEmptyListRow row0 = TableNotEmptyListRow.empty();
        row0.setAuto(0, "A");

        final TableNotEmptyListRow row1 = TableNotEmptyListRow.empty();
        row1.setAuto(1, "B");

        final TableNotEmptyListRows rows = TableNotEmptyListRows.empty();
        rows.setAuto(0, row0);
        rows.setAuto(1, row1);

        this.check(
                rows,
                TableNotEmptyList.INITIAL_CAPACITY,
                2,
                row0,
                row1
        );
    }

    @Test
    public void testTwoRowsEmptyAndNonEmpty() {
        final TableNotEmptyListRow row0 = null;

        final TableNotEmptyListRow row1 = TableNotEmptyListRow.empty();
        row1.setAuto(1, "B");

        final TableNotEmptyListRows rows = TableNotEmptyListRows.empty();
        rows.setAuto(0, row0);
        rows.setAuto(1, row1);

        this.check(
                rows,
                TableNotEmptyList.INITIAL_CAPACITY,
                2,
                row0,
                row1
        );
    }

    @Test
    public void testTwoRowsNonEmptyAndEmpty() {
        final TableNotEmptyListRow row0 = TableNotEmptyListRow.empty();
        row0.setAuto(0, "A");

        final TableNotEmptyListRow row1 = null;

        final TableNotEmptyListRows rows = TableNotEmptyListRows.empty();
        rows.setAuto(0, row0);
        rows.setAuto(1, row1);

        this.check(
                rows,
                TableNotEmptyList.INITIAL_CAPACITY,
                2,
                row0
        );
    }

    @Override
    public TableNotEmptyListRows createList() {
        return TableNotEmptyListRows.empty();
    }

    @Override
    TableNotEmptyListRow element(final int index) {
        final TableNotEmptyListRow row = TableNotEmptyListRow.empty();
        row.setAuto(0, "row-" + index);
        return row;
    }

    @Override
    public Class<TableNotEmptyListRows> type() {
        return TableNotEmptyListRows.class;
    }
}
