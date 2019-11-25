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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;

import java.util.Map;
import java.util.NavigableMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class TableNotEmptyTest extends TableTestCase3<TableNotEmpty> {

    private final static CharSequence X = "x";
    private final static CharSequence Y = "y";
    private final static CharSequence Z = "z";

    @Test
    public void testMaxColumns() {
        assertEquals(3, this.createObject().maxColumn());
    }

    @Test
    public void testMaxRows() {
        assertEquals(3, this.createObject().maxColumn());
    }

    // cell.............................................................................................................

    @Test
    public void testCell() {
        this.cellAndCheck(1, 1, R1C1);
    }

    @Test
    public void testCell2() {
        this.cellAndCheck(0, 2, R2C0);
    }

    @Test
    public void testCellAbsent() {
        final NavigableMap<TableCellCoordinates, CharSequence> map = Maps.navigable();

        map.put(TableCellCoordinates.with(3, 2), "x");

        this.cellAndCheck(TableNotEmpty.with(map, 4, 3),
                2,
                1,
                CharSequences.empty());
    }

    // column...........................................................................................................

    @Test
    public void testColumn() {
        this.columnAndCheck(this.createTable(), 1, R0C1, R1C1, R2C1);
    }

    @Test
    public void testColumnMissing() {
        this.columnAndCheck(this.createTable(), 4);
    }

    @Test
    public void testSetColumnColumn() {
        final int column = 1;
        this.columnAndCheck(this.createTable()
                        .setColumn(column, Lists.of(R0C1)),
                column,
                R0C1, CharSequences.empty(), CharSequences.empty());
    }

    // setColumn........................................................................................................

    @Test
    public void testSetSameColumn() {
        final TableNotEmpty table = this.createTable();
        assertSame(table, table.setColumn(1, Lists.of(R0C1, R1C1, R2C1)));
    }

    @Test
    public void testSetColumn() {
        final int column = 0;

        final Map<TableCellCoordinates, CharSequence> map = this.map();
        map.put(TableCellCoordinates.with(column, 0), X);
        map.put(TableCellCoordinates.with(column, 1), Y);
        map.put(TableCellCoordinates.with(column, 2), Z);

        this.checkMap(this.setColumnAndCheck(column, X, Y, Z), map);
    }

    @Test
    public void testSetColumn2() {
        final int column = 1;

        final Map<TableCellCoordinates, CharSequence> map = this.map();
        map.put(TableCellCoordinates.with(column, 0), X);
        map.put(TableCellCoordinates.with(column, 1), Y);
        map.put(TableCellCoordinates.with(column, 2), Z);

        this.checkMap(this.setColumnAndCheck(column, X, Y, Z), map);
    }

    @Test
    public void testSetColumnEmpty() {
        final int column = 2;

        final Map<TableCellCoordinates, CharSequence> map = this.map();
        map.remove(TableCellCoordinates.with(column, 0));
        map.remove(TableCellCoordinates.with(column, 1));
        map.remove(TableCellCoordinates.with(column, 2));

        this.checkMap(this.createTable().setColumn(column, Lists.empty()), map);
    }

    @Test
    public void testSetAllColumnEmpty() {
        final Table table = this.createTable()
                .setColumn(0, Lists.empty())
                .setColumn(1, Lists.empty())
                .setColumn(2, Lists.empty());

        this.checkMap(table, Maps.empty());
    }

    // row..............................................................................................................

    @Test
    public void testRow() {
        this.rowAndCheck(this.createTable(), 1, R1C0, R1C1, R1C2);
    }

    @Test
    public void testRowMissing() {
        this.rowAndCheck(this.createTable(), 4);
    }

    @Test
    public void testSetRowRow() {
        final int row = 1;
        this.rowAndCheck(this.createTable()
                        .setRow(row, Lists.of(R1C0)),
                row,
                R1C0, CharSequences.empty(), CharSequences.empty());
    }

    // setRow...........................................................................................................

    @Test
    public void testSetSameRow() {
        final TableNotEmpty table = this.createTable();
        assertSame(table, table.setRow(1, Lists.of(R1C0, R1C1, R1C2)));
    }

    @Test
    public void testSetRow() {
        final int row = 0;

        final Map<TableCellCoordinates, CharSequence> map = this.map();
        map.put(TableCellCoordinates.with(0, row), X);
        map.put(TableCellCoordinates.with(1, row), Y);
        map.put(TableCellCoordinates.with(2, row), Z);

        this.checkMap(this.setRowAndCheck(row, X, Y, Z), map);
    }

    @Test
    public void testSetRow2() {
        final int row = 1;

        final Map<TableCellCoordinates, CharSequence> map = this.map();
        map.put(TableCellCoordinates.with(0, row), X);
        map.put(TableCellCoordinates.with(1, row), Y);
        map.put(TableCellCoordinates.with(2, row), Z);

        this.checkMap(this.setRowAndCheck(row, X, Y, Z), map);
    }

    @Test
    public void testSetRowEmpty() {
        final int row = 2;

        final Map<TableCellCoordinates, CharSequence> map = this.map();
        map.remove(TableCellCoordinates.with(0, row));
        map.remove(TableCellCoordinates.with(1, row));
        map.remove(TableCellCoordinates.with(2, row));

        this.checkMap(this.createTable().setRow(row, Lists.empty()), map);
    }

    @Test
    public void testSetAllRowEmpty() {
        final Table table = this.createTable()
                .setRow(0, Lists.empty())
                .setRow(1, Lists.empty())
                .setRow(2, Lists.empty());

        this.checkMap(table, Maps.empty());
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createTable(), this.map().toString());
    }

    @Override
    TableNotEmpty createTable() {
        return TableNotEmpty.with(this.map(), 3, 3);
    }

    private NavigableMap<TableCellCoordinates, CharSequence> map() {
        final NavigableMap<TableCellCoordinates, CharSequence> map = Maps.navigable();

        map.put(TableCellCoordinates.with(0, 0), R0C0);
        map.put(TableCellCoordinates.with(0, 1), R1C0);
        map.put(TableCellCoordinates.with(0, 2), R2C0);

        map.put(TableCellCoordinates.with(1, 0), R0C1);
        map.put(TableCellCoordinates.with(1, 1), R1C1);
        map.put(TableCellCoordinates.with(1, 2), R2C1);

        map.put(TableCellCoordinates.with(2, 0), R0C2);
        map.put(TableCellCoordinates.with(2, 1), R1C2);
        map.put(TableCellCoordinates.with(2, 2), R2C2);

        return map;
    }

    private void columnAndCheck(final Table table,
                                final int column,
                                final CharSequence... text) {
        assertEquals(Lists.of(text),
                table.column(column),
                () -> "column " + column + " from " + table);
    }

    private void rowAndCheck(final Table table,
                             final int row,
                             final CharSequence... text) {
        assertEquals(Lists.of(text),
                table.row(row),
                () -> "row " + row + " from " + table);
    }

    private void checkMap(final Table table,
                          final Map<TableCellCoordinates, CharSequence> map) {
        if (map.isEmpty()) {
            assertSame(TableEmpty.empty(), table);
        } else {
            this.checkMap((TableNotEmpty) table, map);
        }
    }

    private void checkMap(final TableNotEmpty table,
                          final Map<TableCellCoordinates, CharSequence> map) {
        assertEquals(map, table.table, () -> table + " map");
        this.check(table);
    }

    @Override
    int maxColumn() {
        return 3;
    }

    @Override
    int maxRow() {
        return 3;
    }

    @Override
    public Class<TableNotEmpty> type() {
        return TableNotEmpty.class;
    }
}
