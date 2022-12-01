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
import walkingkooka.compare.ComparableTesting2;

public final class TableCellCoordinatesTest extends TableTestCase2<TableCellCoordinates>
        implements ComparableTesting2<TableCellCoordinates> {

    private final static int COLUMN = 2;
    private final static int ROW = 3;

    @Test
    public void testDifferentColumn() {
        this.checkNotEquals(TableCellCoordinates.with(99, 3));
    }

    @Test
    public void testDifferentRow() {
        this.checkNotEquals(TableCellCoordinates.with(COLUMN, 99));
    }

    @Test
    public void testCompareColumn() {
        this.compareToAndCheckLess(TableCellCoordinates.with(COLUMN +1, ROW));
    }

    @Test
    public void testCompareRow() {
        this.compareToAndCheckLess(TableCellCoordinates.with(COLUMN, ROW +1));
    }

    @Test
    public void testSortedArray() {
        final TableCellCoordinates cell11 = TableCellCoordinates.with(1, 1);
        final TableCellCoordinates cell21 = TableCellCoordinates.with(2, 1);
        final TableCellCoordinates cell12 = TableCellCoordinates.with(1, 2);
        final TableCellCoordinates cell13 = TableCellCoordinates.with(1, 3);
        final TableCellCoordinates cell23 = TableCellCoordinates.with(2, 3);

        // rows then columns.
        this.compareToArraySortAndCheck(
            cell23,
                cell12,
                cell11,
                cell21,
                cell13,
                // sorted
                cell11,
                cell21,
                cell12,
                cell13,
                cell23
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(TableCellCoordinates.with(2, 3), "2,3");
    }

    @Override
    public TableCellCoordinates createComparable() {
        return TableCellCoordinates.with(COLUMN, ROW);
    }

    @Override
    public Class<TableCellCoordinates> type() {
        return TableCellCoordinates.class;
    }
}
