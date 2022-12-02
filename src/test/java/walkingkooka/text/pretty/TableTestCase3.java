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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TableTestCase3<T extends Table> extends TableTestCase2<T> {

    TableTestCase3() {
        super();
    }

    @Test
    public final void testCellInvalidColumnFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.createTable().cell(-1, 0)
        );
    }

    @Test
    public final void testCellInvalidRowFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.createTable().cell(0, -1)
        );
    }

    @Test
    public final void testCellOutOfBounds() {
        this.cellAndCheck(
                this.width(),
                this.height(),
                CharSequences.empty()
        );
    }

    @Test
    public final void testCellOutOfBounds2() {
        this.cellAndCheck(
                this.width() + 1,
                this.height() + 1,
                CharSequences.empty()
        );
    }

    @Test
    public final void testColumnInvalidColumnFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.createTable().column(-1)
        );
    }

    @Test
    public final void testColumnWidth() {
        this.checkEquals(
                Lists.empty(),
                this.createTable().column(this.width())
        );
    }

    @Test
    public final void testSetCellInvalidColumnFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.createTable().setCell(-1, 0, "invalid")
        );
    }

    @Test
    public final void testSetCellInvalidRowFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.createTable().setCell(0, -1, "invalid")
        );
    }

    @Test
    public final void testSetColumnInvalidColumnFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.createTable().setColumn(-1, Lists.empty())
        );
    }

    @Test
    public final void testSetColumnNullTextFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createTable().setColumn(0, null)
        );
    }

    @Test
    public final void testSetNewColumnEmpty() {
        final T table = this.createTable();
        assertSame(table, table.setColumn(99, Lists.empty()));
    }

    @Test
    public final void testRowInvalidRowFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.createTable().row(-1)
        );
    }

    @Test
    public final void testRowHeight() {
        this.checkEquals(Lists.empty(), this.createTable().row(this.height()));
    }

    @Test
    public final void testSetRowInvalidRowFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.createTable().setRow(-1, Lists.empty())
        );
    }

    @Test
    public final void testSetRowNullTextFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createTable().setRow(0, null)
        );
    }

    @Test
    public final void testSetNewRowEmpty() {
        final T table = this.createTable();
        assertSame(table, table.setRow(99, Lists.empty()));
    }

    abstract T createTable();

    abstract int width();

    abstract int height();

    final void widthAndCheck(final Table table, final int expected) {
        this.checkEquals(
                expected,
                table.width(),
                () -> "width of " + table
        );
    }

    final Table setCellAndCheck(final int column,
                                final int row,
                                final CharSequence text) {
        return this.setCellAndCheck(
                this.createTable(),
                column,
                row,
                text
        );
    }

    final Table setCellAndCheck(final Table table,
                                final int column,
                                final int row,
                                final CharSequence text) {
        final Table different = table.setCell(column, row, text);
        if (Objects.equals(text, table.cell(column, row))) {
            assertSame(different, table, () -> "table setCell " + column + "," + row + " in " + table);
        } else {
            assertNotSame(different, table, () -> "table setCell " + column + "," + row + " in " + table);
        }

        return different;
    }

    final Table setColumnAndCheck(final int column,
                                  final CharSequence... text) {
        return this.setColumnAndCheck(
                this.createTable(),
                column,
                text
        );
    }

    final Table setColumnAndCheck(final Table table,
                                  final int column,
                                  final CharSequence... text) {
        return this.setColumnAndCheck(
                table,
                column,
                Lists.of(text)
        );
    }

    final Table setColumnAndCheck(final Table table,
                                  final int column,
                                  final List<CharSequence> text) {
        final Table after = table.setColumn(column, text);
        assertNotSame(table, after);
        this.check(after);
        return after;
    }

    final void heightAndCheck(final Table table, final int expected) {
        this.checkEquals(expected,
                table.height(),
                () -> "height of " + table);
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
        this.checkEquals(text,
                table.cell(column, row),
                () -> "cell at " + column + "," + row + " in " + table);
    }

    final Table setRowAndCheck(final int row,
                               final CharSequence... text) {
        return this.setRowAndCheck(
                this.createTable(),
                row,
                text
        );
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

    final <T> List<T> list(final T...elements) {
        return Arrays.asList(elements);
    }

    final void check(final Table table) {
        if (table instanceof TableNotEmpty) {
            this.check((TableNotEmpty) table);
        }
    }

    final void check(final TableNotEmpty table) {
        int width = -1;
        int height = -1;

        int row = 0;
        for (final List<CharSequence> rowText : table.rows) {
            int lastNotEmptyColumn = -1;
            if(null != rowText) {

                int c = 0;
                for(final CharSequence text : rowText) {
                    if(null != text) {
                        lastNotEmptyColumn = c;
                    }
                    c++;
                }

                // if lastColumnCount != row.size() -1 missing cells exist
                final int rowFinal = row;
                this.checkEquals(
                        lastNotEmptyColumn,
                        rowText.size() -1,
                        () -> "row " + rowFinal + " has trailing missing cells in row List: " + rowText
                );

                width = Math.max(width, lastNotEmptyColumn + 1);
                height = row;
            }

            row++;
        }

        this.widthAndCheck(table, width);
        this.heightAndCheck(table, height + 1);
    }

    final void check(final Table table,
                     final List<CharSequence>... rows) {
        this.check(
                table,
                Lists.of(rows)
        );
    }

    final void check(final Table table,
                     final List<List<CharSequence>> rows) {
        this.checkEquals(
                rows,
                table.asList()
        );
    }

    @Override
    public final T createObject() {
        return this.createTable();
    }
}
