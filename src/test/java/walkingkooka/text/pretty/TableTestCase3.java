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
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TableTestCase3<T extends Table> extends TableTestCase2<T> {

    TableTestCase3() {
        super();
    }

    @Test
    public final void testCellInvalidColumnFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createTable().cell(-1, 0));
    }

    @Test
    public final void testCellInvalidRowFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createTable().cell(0, -1));
    }

    @Test
    public final void testCellOutOfBounds() {
        this.cellAndCheck(this.maxColumn(), this.maxRow(), CharSequences.empty());
    }

    @Test
    public final void testCellOutOfBounds2() {
        this.cellAndCheck(this.maxColumn() + 1, this.maxRow() + 1, CharSequences.empty());
    }

    @Test
    public final void testColumnInvalidColumnFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createTable().column(-1));
    }

    @Test
    public final void testColumnMaxColumn() {
        assertEquals(Lists.empty(), this.createTable().column(this.maxColumn()));
    }

    @Test
    public final void testSetCellInvalidColumnFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createTable().setCell(-1, 0, "invalid"));
    }

    @Test
    public final void testSetCellInvalidRowFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createTable().setCell(0, -1, "invalid"));
    }

    @Test
    public final void testSetColumnInvalidColumnFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createTable().setColumn(-1, Lists.empty()));
    }

    @Test
    public final void testSetColumnNullTextFails() {
        assertThrows(NullPointerException.class, () -> this.createTable().setColumn(0, null));
    }

    @Test
    public final void testSetNewColumnEmpty() {
        final T table = this.createTable();
        assertSame(table, table.setColumn(99, Lists.empty()));
    }

    @Test
    public final void testRowInvalidRowFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createTable().row(-1));
    }

    @Test
    public final void testRowMaxRow() {
        assertEquals(Lists.empty(), this.createTable().row(this.maxRow()));
    }

    @Test
    public final void testSetRowInvalidRowFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createTable().setRow(-1, Lists.empty()));
    }

    @Test
    public final void testSetRowNullTextFails() {
        assertThrows(NullPointerException.class, () -> this.createTable().setRow(0, null));
    }

    @Test
    public final void testSetNewRowEmpty() {
        final T table = this.createTable();
        assertSame(table, table.setRow(99, Lists.empty()));
    }

    abstract T createTable();

    abstract int maxColumn();

    abstract int maxRow();

    final void maxColumnAndCheck(final Table table, final int expected) {
        assertEquals(expected,
                table.maxColumn(),
                () -> "maxColumn of " + table);
    }

    final Table setCellAndCheck(final int column,
                                final int row,
                                final CharSequence text) {
        return this.setCellAndCheck(this.createTable(), column, row, text);
    }

    final Table setCellAndCheck(final Table table,
                                final int column,
                                final int row,
                                final CharSequence text) {
        final Table different = table.setCell(column, row, text);
        if (text.equals(table.cell(column, row))) {
            assertSame(different, table, () -> "table setCell " + column + "," + row + " in " + table);
        } else {
            assertNotSame(different, table, () -> "table setCell " + column + "," + row + " in " + table);
        }

        return different;
    }

    final Table setColumnAndCheck(final int column,
                                  final CharSequence... text) {
        return this.setColumnAndCheck(this.createTable(), column, text);
    }

    final Table setColumnAndCheck(final Table table,
                                  final int column,
                                  final CharSequence... text) {
        return this.setColumnAndCheck(table, column, Lists.of(text));
    }

    final Table setColumnAndCheck(final Table table,
                                  final int column,
                                  final List<CharSequence> text) {
        final Table after = table.setColumn(column, text);
        assertNotSame(table, after);
        this.check(after);
        return after;
    }

    final void maxRowAndCheck(final Table table, final int expected) {
        assertEquals(expected,
                table.maxRow(),
                () -> "maxRow of " + table);
    }

    final void cellAndCheck(final int column,
                            final int row,
                            final CharSequence text) {
        this.cellAndCheck(this.createTable(),
                column,
                row,
                text);
    }

    final void cellAndCheck(final Table table,
                            final int column,
                            final int row,
                            final CharSequence text) {
        assertEquals(text,
                table.cell(column, row),
                () -> "cell at " + column + "," + row + " in " + table);
    }

    final Table setRowAndCheck(final int row,
                               final CharSequence... text) {
        return this.setRowAndCheck(this.createTable(), row, Lists.of(text));
    }

    final Table setRowAndCheck(final Table table,
                               final int row,
                               final CharSequence... text) {
        return this.setRowAndCheck(table, row, Lists.of(text));
    }

    final Table setRowAndCheck(final Table table,
                               final int row,
                               final List<CharSequence> text) {
        final Table after = table.setRow(row, text);
        assertNotSame(table, after);
        return after;
    }

    final void check(final Table table) {
        if (table instanceof TableNotEmpty) {
            this.check((TableNotEmpty) table);
        }
    }

    final void check(final TableNotEmpty table) {
        int maxColumn = 0;
        int maxRow = 0;

        for (final TableCellCoordinates coords : table.table.keySet()) {
            maxColumn = Math.max(maxColumn, 1+ coords.column);
            maxRow = Math.max(maxRow, 1+ coords.row);
        }
        this.maxColumnAndCheck(table, maxColumn);
        this.maxRowAndCheck(table, maxRow);
    }

    final void checkMap(final Table table,
                        final Map<TableCellCoordinates, CharSequence> map) {
        if (map.isEmpty()) {
            assertSame(TableEmpty.empty(), table);
        } else {
            this.checkMap((TableNotEmpty) table, map);
        }
    }

    final void checkMap(final TableNotEmpty table,
                        final Map<TableCellCoordinates, CharSequence> map) {
        assertEquals(map, table.table, () -> table + " map");
        this.check(table);
    }

    @Override
    public final T createObject() {
        return this.createTable();
    }
}
