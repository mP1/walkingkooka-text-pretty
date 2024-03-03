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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.collect.list.Lists;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unchecked")
public final class TableNotEmptyTest extends TableTestCase3<TableNotEmpty>
        implements HashCodeEqualsDefinedTesting2<TableNotEmpty> {

    private final static CharSequence X = "x";
    private final static CharSequence Y = "y";
    private final static CharSequence Z = "z";
    private final static CharSequence A = "A";
    private final static CharSequence B = "B";
    private final static CharSequence C = "C";

    // width...........................................................................................................

    @Test
    public void testWithOneColumnOneRow() {
        this.check(
                this.createTable(
                        1,
                        list(X)
                ),
                list(
                        X
                )
        );
    }

    @Test
    public void testWithOneColumnOneRowMissing() {
        this.check(
                this.createTable(
                        2,
                        list(null, X)
                ),
                list(
                        MISSING,
                        X
                )
        );
    }

    @Test
    public void testWithOneColumnOneRowMissing2() {
        this.check(
                this.createTable(
                        1,
                        list(X)
                ),
                list(
                        X
                )
        );
    }

    @Test
    public void testWithOneColumnOneRowMissingRemoved() {
        this.check(
                this.createTable(
                        2,
                        list(X, null)
                ),
                list(
                        X, MISSING
                )
        );
    }

    @Test
    public void testWithOneColumnOneRowMissingRemoved2() {
        this.check(
                this.createTable(
                        1,
                        list(X, "")
                ),
                list(
                        X
                )
        );
    }

    @Test
    public void testWithTrailingEmptyRow() {
        this.check(
                this.createTable(
                        2,
                        list(R0C0, R0C1),
                        null
                ),
                list(
                        R0C0, R0C1
                ),
                list(
                        MISSING, MISSING
                )
        );
    }

    @Test
    public void testWithManyRows() {
        this.check(
                this.createTable(
                        2,
                        list(R0C0, R0C1),
                        list(R1C0, R1C1)
                ),
                list(
                        R0C0, R0C1
                ),
                list(
                        R1C0, R1C1
                )
        );
    }

    // width & height...................................................................................................

    @Test
    public void testHeight() {
        this.heightAndCheck(
                this.createObject(),
                3
        );
    }

    @Test
    public void testWidth() {
        this.widthAndCheck(
                this.createObject(),
                3
        );
    }

    // cell.............................................................................................................

    @Test
    public void testCellRowMissingFails() {
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> this.createTable().cell(0, 4)
        );
    }

    @Test
    public void testCellColumnMissingFails() {
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> this.createTable().cell(4, 0)
        );
    }

    @Test
    public void testCell() {
        this.cellAndCheck(
                1,
                1,
                R1C1
        );
    }

    @Test
    public void testCell2() {
        this.cellAndCheck(
                0,
                2,
                R2C0
        );
    }

    // setCell..........................................................................................................

    @Test
    public void testSetSameCell() {
        final TableNotEmpty table = this.createTable();
        assertSame(table, table.setCell(0, 0, R0C0));
    }

    @Test
    public void testSetSameCell2() {
        final TableNotEmpty table = this.createTable();
        assertSame(table, table.setCell(1, 2, R2C1));
    }

    @Test
    public void testSetSameCell3() {
        final TableNotEmpty table = this.createTable();

        for (int c = 0; c < table.width(); c++) {
            for (int r = 0; r < table.height(); r++) {
                assertSame(table, table.setCell(c, r, table.cell(c, r)));
            }
        }
    }

    @Test
    public void testSetCellDifferent() {
        this.check(
                this.setCellAndCheck(
                        0,
                        0,
                        "x"
                ),
                list(
                        list(
                                "x", R0C1, R0C2
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, R2C2
                        )
                )
        );
    }

    @Test
    public void testSetCellDifferent2() {
        this.check(
                this.setCellAndCheck(
                        1,
                        1,
                        "x"
                ),
                list(
                        list(
                                R0C0, R0C1, R0C2
                        ),
                        list(
                                R1C0, "x", R1C2
                        ),
                        list(
                                R2C0, R2C1, R2C2
                        )
                )
        );
    }

    @Test
    public void testSetCellDifferent3() {
        this.check(
                this.setCellAndCheck(
                        2,
                        2,
                        "x"
                ),
                list(
                        list(
                                R0C0, R0C1, R0C2
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, "x"
                        )
                )
        );
    }

    @Test
    public void testSetCellDifferentNull() {
        this.check(
                this.setCellAndCheck(
                        2,
                        2,
                        null
                ),
                list(
                        list(
                                R0C0, R0C1, R0C2
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetCellDifferentEmpty() {
        this.check(
                this.setCellAndCheck(
                        2,
                        2,
                        ""
                ),
                list(
                        list(
                                R0C0, R0C1, R0C2
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetCellAllDifferent() {
        Table table = this.createTable();

        final List<List<CharSequence>> allRows = Lists.array();

        for (int r = 0; r < table.height(); r++) {

            final List<CharSequence> newRow = Lists.array();

            for (int c = 0; c < table.width(); c++) {
                final String text = "new" + r + "," + c;
                table = this.setCellAndCheck(
                        table,
                        c,
                        r,
                        text
                );

                newRow.add(text);
            }

            allRows.add(newRow);
        }

        this.check(
                table,
                allRows
        );
    }

    @Test
    public void testSetCellNull() {
        this.check(
                this.createTable().setCell(0, 0, null),
                list(
                        list(
                                MISSING, R0C1, R0C2
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, R2C2
                        )
                )
        );
    }

    @Test
    public void testSetCellEmpty() {
        this.check(
                this.createTable()
                        .setCell(0, 0, ""),
                list(
                        list(
                                MISSING, R0C1, R0C2
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, R2C2
                        )
                )
        );
    }

    @Test
    public void testSetCellLastNull() {
        this.check(
                this.createTable().setCell(2, 0, null),
                list(
                        list(
                                R0C0, R0C1, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, R2C2
                        )
                )
        );
    }

    @Test
    public void testSetCellLastEmpty() {
        this.check(
                this.createTable()
                        .setCell(2, 0, ""),
                list(
                        list(
                                R0C0, R0C1, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, R2C2
                        )
                )
        );
    }

    @Test
    public void testSetCellAddsColumn() {
        this.check(
                this.createTable()
                        .setCell(3, 0, X),
                list(
                        list(
                                R0C0, R0C1, R0C2, X
                        ),
                        list(
                                R1C0, R1C1, R1C2, MISSING
                        ),
                        list(
                                R2C0, R2C1, R2C2, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetCellAddsColumn2() {
        this.check(
                this.createTable()
                        .setCell(4, 1, X),
                list(
                        list(
                                R0C0, R0C1, R0C2, MISSING, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2, MISSING, X
                        ),
                        list(
                                R2C0, R2C1, R2C2, MISSING, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetCellAddsRow() {
        this.check(
                this.createTable()
                        .setCell(0, 3, X),
                list(
                        list(
                                R0C0, R0C1, R0C2
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, R2C2
                        ),
                        list(
                                X, MISSING, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetCellAddsRow2() {
        this.check(
                this.createTable()
                        .setCell(1, 3, X),
                list(
                        list(
                                R0C0, R0C1, R0C2
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, R2C2
                        ),
                        list(
                                MISSING, X,  MISSING
                        )
                )
        );
    }

    @Test
    public void testSetCellAddsRow3() {
        this.check(
                this.createTable()
                        .setCell(1, 3, X),
                list(
                        list(
                                R0C0, R0C1, R0C2
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, R2C2
                        ),
                        list(
                                MISSING, X,  MISSING
                        )
                )
        );
    }

    @Test
    public void testSetCellAddsColumnAndRow() {
        this.check(
                this.createTable()
                        .setCell(3, 3, X),
                list(
                        list(
                                R0C0, R0C1, R0C2, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2, MISSING
                        ),
                        list(
                                R2C0, R2C1, R2C2, MISSING
                        ),
                        list(
                                MISSING, MISSING,  MISSING, X
                        )
                )
        );
    }

    @Test
    public void testSetCellAddsColumnAndRow2() {
        this.check(
                this.createTable()
                        .setCell(4, 4, X),
                list(
                        list(
                                R0C0, R0C1, R0C2, MISSING, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2, MISSING, MISSING
                        ),
                        list(
                                R2C0, R2C1, R2C2, MISSING, MISSING
                        ),
                        list(
                                MISSING, MISSING, MISSING, MISSING, MISSING
                        ),
                        list(
                                MISSING, MISSING,  MISSING, MISSING, X
                        )
                )
        );
    }

    @Test
    public void testSetCellOnlyNull() {
        final Table table = this.createTable(
                1,
                list(A)
        );
        assertSame(
                Table.empty(),
                table.setCell(0, 0, null)
        );
    }

    @Test
    public void testSetCellOnlyNullAndNull() {
        final Table table = this.createTable(
                2,
                list(X, Y)
        );
        assertSame(
                Table.empty(),
                table.setCell(0, 0, null)
                        .setCell(1, 0, null)
        );
    }

    @Test
    public void testSetCellBothRowsNullAndNull() {
        final Table table = this.createTable(
                1,
                list(X),
                list(Y)
        );
        assertSame(
                Table.empty(),
                table.setCell(0, 0, null)
                        .setCell(0, 1, null)
        );
    }

    @Test
    public void testSetCellAllNull3() {
        Table table = this.createTable();

        for (int c = 0; c < table.width(); c++) {
            for (int r = 0; r < table.height(); r++) {
                table = this.setCellAndCheck(table, c, r, null);
            }
        }

        assertSame(Table.empty(), table);
    }

    @Test
    public void testSetCellAllEmpty() {
        final Table table = this.createTable(
                1,
                list(A)
        );
        assertSame(
                Table.empty(),
                table.setCell(0, 0, "")
        );
    }

    @Test
    public void testSetCellAllEmpty2() {
        Table table = this.createTable();

        for (int c = 0; c < table.width(); c++) {
            for (int r = 0; r < table.height(); r++) {
                table = this.setCellAndCheck(table, c, r, "");
            }
        }

        assertSame(
                Table.empty(),
                table
        );
    }

    // column...........................................................................................................

    @Test
    public void testColumn() {
        this.columnAndCheck(
                this.createTable(),
                1,
                R0C1,
                R1C1,
                R2C1
        );
    }

    @Test
    public void testSetColumnColumn() {
        final int column = 1;

        this.columnAndCheck(
                this.createTable()
                        .setColumn(column, list(R0C1)),
                column,
                R0C1,
                MISSING,
                MISSING
        );
    }

    // setColumn........................................................................................................

    @Test
    public void testSetSameColumn() {
        final TableNotEmpty table = this.createTable();
        assertSame(table, table.setColumn(1, list(R0C1, R1C1, R2C1)));
    }

    @Test
    public void testSetColumn() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, X, Y, Z),
                list(
                        list(
                                X, R0C1, R0C2
                        ),
                        list(
                                Y, R1C1, R1C2
                        ),
                        list(
                                Z, R2C1, R2C2
                        )
                )
        );
    }

    @Test
    public void testSetColumnEmptyList() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column),
                list(
                        MISSING, R0C1, R0C2
                ),
                list(
                        MISSING, R1C1, R1C2
                ),
                list(
                        MISSING, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetColumnEmptyList2() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column),
                list(
                        R0C0, MISSING, R0C2
                ),
                list(
                        R1C0, MISSING, R1C2
                ),
                list(
                        R2C0, MISSING, R2C2
                )
        );
    }

    @Test
    public void testSetColumnEmptyList3() {
        final int column = 2;

        this.check(
                this.setColumnAndCheck(column),
                list(
                        R0C0, R0C1, MISSING
                ),
                list(
                        R1C0, R1C1, MISSING
                ),
                list(
                        R2C0, R2C1, MISSING
                )
        );
    }

    @Test
    public void testSetColumnWithListWithNulls() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, null, null),
                list(
                        MISSING, R0C1, R0C2
                ),
                list(
                        MISSING, R1C1, R1C2
                ),
                list(
                        MISSING, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithNulls2() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, null, null),
                list(
                        R0C0, MISSING, R0C2
                ),
                list(
                        R1C0, MISSING, R1C2
                ),
                list(
                        R2C0, MISSING, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithNulls3() {
        final int column = 2;

        this.check(
                this.setColumnAndCheck(column, null, null),
                list(
                        R0C0, R0C1, MISSING
                ),
                list(
                        R1C0, R1C1, MISSING
                ),
                list(
                        R2C0, R2C1, MISSING
                )
        );
    }

    @Test
    public void testSetColumnWithListWithEmptyString() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, "", ""),
                list(
                        MISSING, R0C1, R0C2
                ),
                list(
                        MISSING, R1C1, R1C2
                ),
                list(
                        MISSING, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithEmptyString2() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, "", ""),
                list(
                        R0C0, MISSING, R0C2
                ),
                list(
                        R1C0, MISSING, R1C2
                ),
                list(
                        R2C0, MISSING, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithEmptyString3() {
        final int column = 2;

        this.check(
                this.setColumnAndCheck(column, "", ""),
                list(
                        R0C0, R0C1, MISSING
                ),
                list(
                        R1C0, R1C1, MISSING
                ),
                list(
                        R2C0, R2C1, MISSING
                )
        );
    }

    @Test
    public void testSetColumnWithListWithFewerCells() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, X),
                list(
                        X, R0C1, R0C2
                ),
                list(
                        MISSING, R1C1, R1C2
                ),
                list(
                        MISSING, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithFewerCells2() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, X),
                list(
                        R0C0, X, R0C2
                ),
                list(
                        R1C0, MISSING, R1C2
                ),
                list(
                        R2C0, MISSING, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithFewerCells3() {
        final int column = 2;

        this.check(
                this.setColumnAndCheck(column, X),
                list(
                        R0C0, R0C1, X
                ),
                list(
                        R1C0, R1C1, MISSING
                ),
                list(
                        R2C0, R2C1, MISSING
                )
        );
    }

    @Test
    public void testSetColumnWithListWithFewerCellsTrailingNull() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, X, null),
                list(
                        X, R0C1, R0C2
                ),
                list(
                        MISSING, R1C1, R1C2
                ),
                list(
                        MISSING, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithFewerCellsTrailingEmpty() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, X, ""),
                list(
                        X, R0C1, R0C2
                ),
                list(
                        MISSING, R1C1, R1C2
                ),
                list(
                        MISSING, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithFewerCellsTrailingEmpty2() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, X, ""),
                list(
                        R0C0, X, R0C2
                ),
                list(
                        R1C0, MISSING, R1C2
                ),
                list(
                        R2C0, MISSING, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithFewerCellsIncludesNull() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, null, X),
                list(
                        MISSING, R0C1, R0C2
                ),
                list(
                        X, R1C1, R1C2
                ),
                list(
                        MISSING, R2C1, R2C2
                )
        );
    }


    @Test
    public void testSetColumnWithListWithFewerCellsIncludesNull2() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, null, X),
                list(
                        R0C0, MISSING, R0C2
                ),
                list(
                        R1C0, X, R1C2
                ),
                list(
                        R2C0, MISSING, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithFewerCellsIncludesEmpty() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, "", X),
                list(
                        MISSING, R0C1, R0C2
                ),
                list(
                        X, R1C1, R1C2
                ),
                list(
                        MISSING, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithMoreCells() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, X, Y, Z, A),
                list(
                        X, R0C1, R0C2
                ),
                list(
                        Y, R1C1, R1C2
                ),
                list(
                        Z, R2C1, R2C2
                ),
                list(
                        A, MISSING, MISSING
                )
        );
    }

    @Test
    public void testSetColumnWithListWithMoreCells2() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, X, Y, Z, A),
                list(
                        R0C0, X, R0C2
                ),
                list(
                        R1C0, Y, R1C2
                ),
                list(
                        R2C0, Z, R2C2
                ),
                list(
                        MISSING, A, MISSING
                )
        );
    }

    @Test
    public void testSetColumnWithListWithMoreCellsIncludesNull() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, X, Y, null, A),
                list(
                        R0C0, X, R0C2
                ),
                list(
                        R1C0, Y, R1C2
                ),
                list(
                        R2C0, MISSING, R2C2
                ),
                list(
                        MISSING, A, MISSING
                )
        );
    }

    @Test
    public void testSetColumnWithListWithMoreCellsIncludesEmpty() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, X, Y, "", A),
                list(
                        R0C0, X, R0C2
                ),
                list(
                        R1C0, Y, R1C2
                ),
                list(
                        R2C0, MISSING, R2C2
                ),
                list(
                        MISSING, A, MISSING
                )
        );
    }

    @Test
    public void testSetColumnWithListWithMoreCellsIncludesNullAndEmpty() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, X, Y, Z, null, ""),
                list(
                        R0C0, X, R0C2
                ),
                list(
                        R1C0, Y, R1C2
                ),
                list(
                        R2C0, Z, R2C2
                ),
                list(
                        MISSING, MISSING, MISSING
                ),
                list(
                        MISSING, MISSING, MISSING
                )
        );
    }

    @Test
    public void testSetColumnSetColumn() {
        this.check(
                this.createTable()
                        .setColumn(
                                0,
                                list(X)
                        ).setColumn(
                                1,
                                list(Y)
                        ),
                list(
                        X, Y, R0C2
                ),
                list(
                        MISSING, MISSING, R1C2
                ),
                list(
                        MISSING, MISSING, R2C2
                )
        );
    }

    @Test
    public void testSetColumnSetColumn2() {
        this.check(
                this.createTable()
                        .setColumn(
                                0,
                                list(X)
                        ).setColumn(
                                3,
                                list(Y)
                        ),
                list(
                        X, R0C1, R0C2, Y
                ),
                list(
                        MISSING, R1C1, R1C2, MISSING
                ),
                list(
                        MISSING, R2C1, R2C2, MISSING
                )
        );
    }

    // row..............................................................................................................

    @Test
    public void testRow() {
        this.rowAndCheck(
                this.createTable(),
                1,
                R1C0, R1C1, R1C2
        );
    }

    @Test
    public void testRowWithMissingRow() {
        this.rowAndCheck(
                this.createTable(
                        3,
                        list("A", B, C),
                        null
                ),
                1,
                MISSING,
                MISSING,
                MISSING
        );
    }

    @Test
    public void testRowWithMissingCells() {
        this.rowAndCheck(
                this.createTable(
                        3,
                        null, // row 0
                        list("A", null, C) // row 1
                ),
                1,
                "A",
                MISSING,
                C
        );
    }

    @Test
    public void testRowWithMissingCells2() {
        this.rowAndCheck(
                this.createTable(
                        3,
                        null,
                        list("A", "", C)
                ),
                1,
                "A",
                MISSING,
                C
        );
    }

    @Test
    public void testSetRowFullRow() {
        final int row = 1;
        this.check(
                this.createTable()
                        .setRow(
                                row,
                                list("A", B, C)
                        ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        "A", B, C
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowFullRowIncludesNullAndEmpty() {
        final int row = 1;
        this.check(
                this.createTable()
                        .setRow(
                                row,
                                list(null, "", C)
                        ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        MISSING, MISSING, C
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowLongerRow() {
        final int row = 1;
        this.check(
                this.createTable()
                        .setRow(
                                row,
                                list("A", B, C, "D")
                        ),
                list(
                        R0C0, R0C1, R0C2, MISSING
                ),
                list(
                        "A", B, C, "D"
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING
                )
        );
    }

    @Test
    public void testSetRowShortRow() {
        final int row = 1;
        this.check(
                this.createTable()
                        .setRow(row, list(R1C0)),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        R1C0, MISSING, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowEmptyRow() {
        final int row = 1;
        this.check(
                this.createTable()
                        .setRow(row, list()),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        MISSING, MISSING, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    // setRow...........................................................................................................

    @Test
    public void testSetSameRow() {
        final TableNotEmpty table = this.createTable();
        assertSame(
                table,
                table.setRow(0, list(R0C0, R0C1, R0C2))
        );
    }


    @Test
    public void testSetSameRow2() {
        final TableNotEmpty table = this.createTable();
        assertSame(
                table,
                table.setRow(1, list(R1C0, R1C1, R1C2))
        );
    }

    @Test
    public void testSetRowDifferent() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        X, Y, Z
                ),
                list(
                        X, Y, Z
                ),
                list(
                        R1C0, R1C1, R1C2
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowDifferent2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        X, Y, Z
                ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        X, Y, Z
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowDifferent3() {
        final int row = 2;

        this.check(
                this.createAndSetRow(
                        row,
                        X, Y, Z
                ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        R1C0, R1C1, R1C2
                ),
                list(
                        X, Y, Z
                )
        );
    }

    @Test
    public void testSetRowEmptyList() {
        final int row = 0;

        this.check(
                this.createAndSetRow(row),
                list(
                        MISSING, MISSING, MISSING
                ),
                list(
                        R1C0, R1C1, R1C2
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowEmptyList2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(row),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        MISSING, MISSING, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithNulls() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        null, null
                ),
                list(
                        MISSING, MISSING, MISSING
                ),
                list(
                        R1C0, R1C1, R1C2
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithNulls2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        null, null
                ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        MISSING, MISSING, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithEmptyString() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        "", ""
                ),
                list(
                        MISSING, MISSING, MISSING
                ),
                list(
                        R1C0, R1C1, R1C2
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithEmptyString2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        "", ""
                ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        MISSING, MISSING, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithFewerCells() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        X
                ),
                list(
                        X, MISSING, MISSING
                ),
                list(
                        R1C0, R1C1, R1C2
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithFewerCells2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        X
                ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        X, MISSING, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithFewerCellsTrailingNull() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        X, null
                ),
                list(
                        X, MISSING, MISSING
                ),
                list(
                        R1C0, R1C1, R1C2
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithFewerCellsTrailingNull2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        X, null
                ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        X, MISSING, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithFewerCellsTrailingEmpty() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        X,
                        ""
                ),
                list(
                        X, MISSING, MISSING
                ),
                list(
                        R1C0, R1C1, R1C2
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithFewerCellsTrailingEmpty2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        X,
                        ""
                ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        X, MISSING, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithFewerCellsIncludesNull() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        null, X
                ),
                list(
                        MISSING, X, MISSING
                ),
                list(
                        R1C0, R1C1, R1C2
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithFewerCellsIncludesNull2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        null, X
                ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        MISSING, X, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithFewerCellsIncludesEmpty() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        "", X
                ),
                list(
                        MISSING, X, MISSING
                ),
                list(
                        R1C0, R1C1, R1C2
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithFewerCellsIncludesEmpty2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        "", X
                ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        MISSING, X, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCells() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        X, Y, Z, A
                ),
                list(
                        X, Y, Z, A
                ),
                list(
                        R1C0, R1C1, R1C2, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCells2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        X, Y, Z, A
                ),
                list(
                        R0C0, R0C1, R0C2, MISSING
                ),
                list(
                        X, Y, Z, A
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCellsIncludesNull() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        X, Y, null, A
                ),
                list(
                        X, Y, MISSING, A
                ),
                list(
                        R1C0, R1C1, R1C2, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCellsIncludesNull2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        X, Y, null, A
                ),
                list(
                        R0C0, R0C1, R0C2, MISSING
                ),
                list(
                        X, Y, MISSING, A
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCellsIncludesEmpty() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        X, Y, "", A
                ),
                list(
                        X, Y, MISSING, A
                ),
                list(
                        R1C0, R1C1, R1C2, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCellsIncludesEmpty2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        X, Y, "", A
                ),
                list(
                        R0C0, R0C1, R0C2, MISSING
                ),
                list(
                        X, Y, MISSING, A
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCellsIncludesNullAndEmpty() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        X, null, "", A
                ),
                list(
                        X, MISSING, MISSING, A
                ),
                list(
                        R1C0, R1C1, R1C2, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCellsIncludesNullAndEmpty2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        X, null, "", A
                ),
                list(
                        R0C0, R0C1, R0C2, MISSING
                ),
                list(
                        X, MISSING, MISSING, A
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCellsIncludesNullAndEmptyAndTrimmed() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        X, null, "", A, null
                ),
                list(
                        X, MISSING, MISSING, A, MISSING
                ),
                list(
                        R1C0, R1C1, R1C2, MISSING, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCellsIncludesNullAndEmptyAndTrimmed2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        X, null, "", A, null
                ),
                list(
                        R0C0, R0C1, R0C2, MISSING, MISSING
                ),
                list(
                        X, MISSING, MISSING, A, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCellsAllNullOrEmpty() {
        final int row = 0;

        this.check(
                this.createAndSetRow(
                        row,
                        null, "", null, ""
                ),
                list(
                        MISSING, MISSING, MISSING, MISSING
                ),
                list(
                        R1C0, R1C1, R1C2, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCellsAllNullOrEmpty2() {
        final int row = 1;

        this.check(
                this.createAndSetRow(
                        row,
                        null, "", null, ""
                ),
                list(
                        R0C0, R0C1, R0C2, MISSING
                ),
                list(
                        MISSING, MISSING, MISSING, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2, MISSING
                )
        );
    }

    @Test
    public void testSetRowWithListWithMoreCellsAllNullOrEmpty3() {
        final int row = 2;

        this.check(
                this.createAndSetRow(
                        row,
                        null, "", null, ""
                ),
                list(
                        R0C0, R0C1, R0C2, MISSING
                ),
                list(
                        R1C0, R1C1, R1C2, MISSING
                ),
                list(
                        MISSING,MISSING,MISSING,MISSING
                )
        );
    }

    @Test
    public void testSetRowSetRow() {
        this.check(
                this.createTable()
                        .setRow(0, list(X))
                        .setRow(1, list(null, Y)),
                list(
                        X, MISSING, MISSING
                ),
                list(
                        MISSING, Y, MISSING
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetEmptyRows() {
        this.check(
                Table.empty()
                        .setRow(0, list())
                        .setRow(1, list()),
                list(),
                list()
        );
    }

    @Test
    public void testSetEmptyRows2() {
        this.check(
                this.createTable()
                        .setRow(0, list())
                        .setRow(1, list())
                        .setRow(2, list()),
                list(),
                list(),
                list()
        );
    }

    // setRows.........................................................................................................

    @Test
    public void testSetRowsEmptyNonOrigin() {
        final Table expected = this.createTable()
                .setCell(
                        3,
                        3,
                        ""
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                4,
                                4,
                                Lists.empty()
                        )
        );
    }

    @Test
    public void testSetRowsAtOrigin() {
        final Table expected = this.createTable()
                .setRow(
                        0,
                        list(X, Y, Z)
                );

        expected.setRow(
                1,
                list(R1C0, R1C1, R1C2)
        );

        expected.setRow(
                2,
                list(R2C0, R2C1, R2C2)
        );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                0,
                                0,
                                Lists.of(
                                        list(X, Y, Z)
                                )
                        )
        );
    }

    @Test
    public void testSetRowsXOffset() {
        final Table expected = this.createTable()
                .setCell(
                        2,
                        0,
                        X
                ).setCell(
                        2,
                        1,
                        Y
                ).setCell(
                        2,
                        2,
                        Z
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                2,
                                0,
                                Lists.of(
                                        list(X),
                                        list(Y),
                                        list(Z)
                                )
                        )
        );
    }

    @Test
    public void testSetRowsYOffset() {
        final Table expected = this.createTable()
                .setRow(
                        1,
                        list(X, Y, Z)
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                0,
                                1,
                                Lists.of(
                                        list(X, Y, Z)
                                )
                        )
        );
    }

    @Test
    public void testSetRowsXOffsetAndYOffset() {
        final Table expected = this.createTable()
                .setCell(
                        1,
                        1,
                        X
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                1,
                                1,
                                Lists.of(
                                        list(X)
                                )
                        )
        );
    }

    @Test
    public void testSetRowsXOffsetAndYOffset2() {
        final Table expected = this.createTable()
                .setCell(
                        1,
                        1,
                        X
                ).setCell(
                        2,
                        1,
                        Y
                ).setCell(
                        1,
                        2,
                        Z
                ).setCell(
                        2,
                        2,
                        A
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                1,
                                1,
                                Lists.of(
                                        list(X, Y),
                                        list(Z, A)
                                )
                        )
        );
    }

    @Test
    public void testSetRowsWindowRight() {
        final Table expected = this.createTable()
                .setCell(
                        3,
                        0,
                        X
                ).setCell(
                        3,
                        1,
                        Y
                ).setCell(
                        3,
                        2,
                        Z
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                3,
                                0,
                                Lists.of(
                                        list(X),
                                        list(Y),
                                        list(Z)
                                )
                        )
        );
    }

    @Test
    public void testSetRowsWindowRight2() {
        final Table expected = this.createTable()
                .setCell(
                        3,
                        0,
                        X
                ).setCell(
                        4,
                        0,
                        Y
                ).setCell(
                        3,
                        1,
                        Z
                ).setCell(
                        4,
                        1,
                        A
                ).setCell(
                        3,
                        2,
                        B
                ).setCell(
                        4,
                        2,
                        C
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                3,
                                0,
                                Lists.of(
                                        list(X, Y),
                                        list(Z, A),
                                        list(B, C)
                                )
                        )
        );
    }

    @Test
    public void testSetRowsYOffsetBottom() {
        final Table expected = this.createTable()
                .setRow(
                        3,
                        list(X, Y, Z)
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                0,
                                3,
                                Lists.of(
                                        list(X, Y, Z)
                                )
                        )
        );
    }

    @Test
    public void testSetRowsXOffsetRightYOffsetBottom() {
        final Table expected = this.createTable()
                .setCell(
                        2,
                        2,
                        X
                ).setCell(
                        3,
                        2,
                        Y
                ).setCell(
                        2,
                        3,
                        Z
                ).setCell(
                        3,
                        3,
                        A
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                2,
                                2,
                                Lists.of(
                                        list(X, Y),
                                        list(Z, A)
                                )
                        )
        );
    }

    @Test
    public void testSetRowsNonSquareRows() {
        final Table expected = this.createTable()
                .setCell(
                        1,
                        1,
                        X
                ).setCell(
                        2,
                        1,
                        Y
                ).setCell(
                        1,
                        2,
                        Z
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                1,
                                1,
                                Lists.of(
                                        list(X, Y),
                                        list(Z)
                                )
                        )
        );
    }

    @Test
    public void testSetRowsNullFirstRow() {
        final Table expected = this.createTable()
                .setCell(
                        1,
                        2,
                        Z
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                1,
                                1,
                                Lists.of(
                                        null, // row1
                                        list(Z) // row2
                                )
                        )
        );
    }

    @Test
    public void testSetRowsEmptyFirstRow() {
        final Table expected = this.createTable()
                .setCell(
                        1,
                        2,
                        Z
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                1,
                                1,
                                Lists.of(
                                        list(), // row1
                                        list(Z) // row2
                                )
                        )
        );
    }

    @Test
    public void testSetRowsNullElement() {
        final Table expected = this.createTable()
                .setCell(
                        1,
                        1,
                        null
                ).setCell(
                        2,
                        1,
                        Z
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                1,
                                1,
                                Lists.of(
                                        list(null, Z) // row2
                                )
                        )
        );
    }

    @Test
    public void testSetRowsEmptyElement() {
        final Table expected = this.createTable()
                .setCell(
                        1,
                        1,
                        ""
                ).setCell(
                        2,
                        1,
                        Z
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setRows(
                                1,
                                1,
                                Lists.of(
                                        list("", Z) // row2
                                )
                        )
        );
    }

    // setHeight........................................................................................................

    // width should also decrease to the max width of row 0 and row 1 which is 2
    @Test
    public void testSetHeightDecrease() {
        this.setHeightAndCheck(
                Table.empty()
                        .setCell(
                                0,
                                0,
                                X
                        ).setCell(
                                1,
                                1,
                                Y
                        ).setCell(
                                0,
                                2,
                                Z
                        ),
                2,
                Table.empty()
                        .setCell(
                                0,
                                0,
                                X
                        ).setCell(
                                1,
                                1,
                                Y
                        )
        );
    }

    @Test
    public void testSetHeightDecrease2() {
        this.setHeightAndCheck(
                Table.empty()
                        .setCell(
                                0,
                                0,
                                X
                        ).setCell(
                                0,
                                1,
                                Y
                        ).setCell(
                                0,
                                2,
                                Z
                        ),
                1,
                Table.empty()
                        .setCell(
                                0,
                                0,
                                X
                        )
        );
    }

    @Test
    public void testSetHeightDecreaseBecomes1EmptyRow() {
        // - - -
        // - - -
        // - - Z

        this.setHeightAndCheck(
                Table.empty()
                        .setCell(
                                2,
                                2,
                                Z
                        ),
                1,
                Table.empty()
                        .setRow(
                                0,
                                Lists.of(
                                        MISSING,
                                        MISSING,
                                        MISSING
                                )
                        )
        );
    }

    @Test
    public void testSetHeightIncreaseRowsAdded() {
        this.setHeightAndCheck(
                this.createTable(),
                5,
                this.createTable()
                        .setRow(4, Lists.empty())
        );
    }

    @Test
    public void testSetHeightAndSetCell() {
        this.checkEquals(
                this.createTable()
                        .setHeight(5)
                        .setCell(4, 4, "X"),
                this.createTable()
                        .setCell(4, 4, "X")
        );
    }

    @Test
    public void testSetHeightAndSetCell2() {
        this.checkEquals(
                this.createTable()
                        .setHeight(5)
                        .setCell(3, 1, "X"),
                this.createTable(
                        4,
                        list(
                                R0C0, R0C1, R0C2, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2, "X"
                        ),
                        list(
                                R2C0, R2C1, R2C2, MISSING
                        ),
                        list(
                                MISSING, MISSING, MISSING, MISSING
                        ),
                        list(
                                MISSING, MISSING, MISSING, MISSING
                        )
                )
        );
    }

    // setWidth.........................................................................................................

    @Test
    public void testSetWidthIncreased() {
        this.setWidthAndCheck(
                this.createTable(),
                4,
                this.createTable(
                        4,
                        list(
                                R0C0, R0C1, R0C2, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2, MISSING
                        ),
                        list(
                                R2C0, R2C1, R2C2, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetWidthIncreased2() {
        this.setWidthAndCheck(
                this.createTable(),
                5,
                this.createTable(
                        5,
                        list(
                                R0C0, R0C1, R0C2, MISSING, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2, MISSING, MISSING
                        ),
                        list(
                                R2C0, R2C1, R2C2, MISSING, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetWidthDecreased() {
        this.setWidthAndCheck(
                this.createTable(),
                2,
                this.createTable(
                        2,
                        list(
                                R0C0, R0C1
                        ),
                        list(
                                R1C0, R1C1
                        ),
                        list(
                                R2C0, R2C1
                        )
                )
        );
    }

    @Test
    public void testSetWidthDecreased2() {
        this.setWidthAndCheck(
                this.createTable(),
                1,
                this.createTable(
                        1,
                        list(
                                R0C0
                        ),
                        list(
                                R1C0
                        ),
                        list(
                                R2C0
                        )
                )
        );
    }

    @Test
    public void testSetWidthDecreaseThenIncreased() {
        this.checkEquals(
                this.createTable(
                        4,
                        list(
                                R0C0, R0C1, MISSING, MISSING
                        ),
                        list(
                                R1C0, R1C1, MISSING, MISSING
                        ),
                        list(
                                R2C0, R2C1, MISSING, MISSING
                        )
                ),
                this.createTable()
                        .setWidth(2)
                        .setWidth(4)
        );
    }

    @Test
    public void testSetWidthDecreaseThenIncreased2() {
        this.checkEquals(
                this.createTable()
                        .setWidth(2)
                        .setWidth(4)
                        .setCell(1, 1, X),
                this.createTable(
                        4,
                        list(
                                R0C0, R0C1, MISSING, MISSING
                        ),
                        list(
                                R1C0, X, MISSING, MISSING
                        ),
                        list(
                                R2C0, R2C1, MISSING, MISSING
                        )
                )
        );
    }

    // setSize...........................................................................................................

    @Test
    public void testSetSizeLessWidth() {
        this.setSizeAndCheck(
                this.createTable(),
                2,
                3,
                this.createTable(
                        2,
                        list(
                                R0C0, R0C1
                        ),
                        list(
                                R1C0, R1C1
                        ),
                        list(
                                R2C0, R2C1
                        )
                )
        );
    }

    @Test
    public void testSetSizeMoreWidth() {
        this.setSizeAndCheck(
                this.createTable(),
                4,
                3,
                this.createTable(
                        4,
                        list(
                                R0C0, R0C1, R0C2, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2, MISSING
                        ),
                        list(
                                R2C0, R2C1, R2C2, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetSizeLessHeight() {
        this.setSizeAndCheck(
                this.createTable(),
                3,
                2,
                this.createTable(
                        3,
                        list(
                                R0C0, R0C1, R0C2
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        )
                )
        );
    }

    @Test
    public void testSetSizeMoreHeight() {
        this.setSizeAndCheck(
                this.createTable(),
                3,
                4,
                this.createTable(
                        3,
                        list(
                                R0C0, R0C1, R0C2
                        ),
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, R2C2
                        ),
                        list(
                                MISSING, MISSING, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetSizeLessWidthLessHeight() {
        this.setSizeAndCheck(
                this.createTable(),
                2,
                2,
                this.createTable(
                        2,
                        list(
                                R0C0, R0C1
                        ),
                        list(
                                R1C0, R1C1
                        )
                )
        );
    }

    @Test
    public void testSetSizeLessWidthMoreHeight() {
        this.setSizeAndCheck(
                this.createTable(),
                2,
                4,
                this.createTable(
                        2,
                        list(
                                R0C0, R0C1
                        ),
                        list(
                                R1C0, R1C1
                        ),
                        list(
                                R2C0, R2C1
                        ),
                        list(
                                MISSING, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetSizeMoreWidthLessHeight() {
        this.setSizeAndCheck(
                this.createTable(),
                4,
                2,
                this.createTable(
                        4,
                        list(
                                R0C0, R0C1, R0C2, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetSizeMoreWidthMoreHeight() {
        this.setSizeAndCheck(
                this.createTable(),
                4,
                4,
                this.createTable(
                        4,
                        list(
                                R0C0, R0C1, R0C2, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2, MISSING
                        ),
                        list(
                                R2C0, R2C1, R2C2, MISSING
                        ),
                        list(
                                MISSING, MISSING, MISSING, MISSING
                        )
                )
        );
    }

    @Test
    public void testSetSizeMoreWidthMoreHeight2() {
        this.setSizeAndCheck(
                this.createTable(),
                5,
                5,
                this.createTable(
                        5,
                        list(
                                R0C0, R0C1, R0C2, MISSING, MISSING
                        ),
                        list(
                                R1C0, R1C1, R1C2, MISSING, MISSING
                        ),
                        list(
                                R2C0, R2C1, R2C2, MISSING, MISSING
                        ),
                        list(
                                MISSING, MISSING, MISSING, MISSING, MISSING
                        ),
                        list(
                                MISSING, MISSING, MISSING, MISSING, MISSING
                        )
                )
        );
    }

    // setColumns......................................................................................................

    @Test
    public void testSetColumnsXOffset() {
        final Table expected = this.createTable()
                .setCell(
                        2,
                        0,
                        X
                ).setCell(
                        2,
                        1,
                        Y
                ).setCell(
                        2,
                        2,
                        Z
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setColumns(
                                2,
                                0,
                                Lists.of(
                                        Lists.of(X, Y, Z)
                                )
                        )
        );
    }

    @Test
    public void testSetColumnsXOffset2() {
        final Table expected = this.createTable()
                .setCell(
                        2,
                        0,
                        X
                ).setCell(
                        2,
                        1,
                        Y
                ).setCell(
                        3,
                        0,
                        Z
                ).setCell(
                        3,
                        1,
                        A
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setColumns(
                                2,
                                0,
                                Lists.of(
                                        Lists.of(X, Y),
                                        Lists.of(Z, A)
                                )
                        )
        );
    }

    @Test
    public void testSetColumnsYOffset() {
        final Table expected = this.createTable()
                .setCell(
                        0,
                        2,
                        X
                ).setCell(
                        0,
                        3,
                        Y
                ).setCell(
                        0,
                        4,
                        Z
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setColumns(
                                0,
                                2,
                                Lists.of(
                                        Lists.of(X, Y, Z)
                                )
                        )
        );
    }

    @Test
    public void testSetColumnsXOffsetAndYOffset() {
        final Table expected = this.createTable()
                .setCell(
                        1,
                        2,
                        X
                ).setCell(
                        1,
                        3,
                        Y
                ).setCell(
                        1,
                        4,
                        Z
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setColumns(
                                1,
                                2,
                                Lists.of(
                                        Lists.of(X, Y, Z)
                                )
                        )
        );
    }

    @Test
    public void testSetColumnsWindowRight() {
        final Table expected = this.createTable()
                .setCell(
                        3,
                        0,
                        X
                ).setCell(
                        3,
                        1,
                        Y
                ).setCell(
                        3,
                        2,
                        Z
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setColumns(
                                3,
                                0,
                                Lists.of(
                                        Lists.of(X, Y, Z)
                                )
                        )
        );
    }

    @Test
    public void testSetColumnsYOffsetBottom() {
        final Table expected = this.createTable()
                .setColumn(
                        0,
                        list(R0C0, R1C0, R2C0, X, Y, Z)
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setColumns(
                                0,
                                3,
                                Lists.of(
                                        list(X, Y, Z)
                                )
                        )
        );
    }

    @Test
    public void testSetColumnsXOffsetRightYOffsetBottom() {
        final Table expected = this.createTable()
                .setCell(
                        3,
                        3,
                        X
                ).setCell(
                        3,
                        4,
                        Y
                ).setCell(
                        3,
                        5,
                        Z
                ).setCell(
                        4,
                        3,
                        A
                ).setCell(
                        4,
                        4,
                        B
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setColumns(
                                3,
                                3,
                                Lists.of(
                                        list(X, Y, Z),
                                        list(A, B)
                                )
                        )
        );
    }

    @Test
    public void testSetColumnsNonSquareRows() {
        final Table expected = this.createTable()
                .setCell(
                        2,
                        1,
                        X
                ).setCell(
                        2,
                        2,
                        Y
                ).setCell(
                        2,
                        3,
                        Z
                ).setCell(
                        3,
                        1,
                        A
                ).setCell(
                        4,
                        1,
                        B
                );

        this.checkEquals(
                expected,
                this.createTable()
                        .setColumns(
                                2,
                                1,
                                Lists.of(
                                        list(X, Y, Z),
                                        list(A),
                                        list(B)
                                )
                        )
        );
    }
    
    // equals...........................................................................................................

    @Test
    public void testDifferentCells() {
        this.checkNotEquals(
                this.createTable(
                        2,
                        list(R0C0, R0C1)
                ),
                this.createTable(
                        1,
                        list(R0C0)
                )
        );
    }

    @Test
    public void testDifferentCellsTypeSameContent() {
        this.checkNotEquals(
                this.createTable(
                        1,
                        list(R0C0)
                ),
                this.createTable(
                        1,
                        list(new StringBuilder(R0C0))
                )
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createTable(),
                this.listOrRows().toString()
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                this.createTable(),
                "Table\n" +
                        "  width: 3\n" +
                        "  height: 3\n" +
                        "  row: 0\n" +
                        "    \"r0c0\"\n" +
                        "    \"r0c1\"\n" +
                        "    \"r0c2\"\n" +
                        "  row: 1\n" +
                        "    \"r1c0\"\n" +
                        "    \"r1c1\"\n" +
                        "    \"r1c2\"\n" +
                        "  row: 2\n" +
                        "    \"r2c0\"\n" +
                        "    \"r2c1\"\n" +
                        "    \"r2c2\"\n"
        );
    }

    // helpers..........................................................................................................

    @Override
    TableNotEmpty createTable() {
        return createTable(
                3,
                this.listOrRows()
        );
    }

    private TableNotEmpty createTable(final int width,
                                      final List<CharSequence>... rows) {
        return this.createTable(
                width,
                Lists.of(
                        rows
                )
        );
    }

    private TableNotEmpty createTable(final int width,
                                      final List<List<CharSequence>> rows) {
        final TableNotEmptyListRows copiedRows = TableNotEmptyListRows.empty();

        int row = 0;
        for (final List<CharSequence> rowText : rows) {
            final TableNotEmptyListRow copiedRowText;

            if (null != rowText && !rowText.isEmpty()) {
                copiedRowText = TableNotEmptyListRow.empty();

                int column = 0;
                for (final CharSequence text : rowText) {
                    copiedRowText.setAuto(
                            column,
                            text
                    );

                    column++;
                }
            } else {
                copiedRowText = null;
            }

            copiedRows.setAuto(
                    row,
                    copiedRowText
            );
            row++;
        }

        copiedRows.setWidth(width);

        return TableNotEmpty.with(
                copiedRows,
                width
        );
    }

    private List<List<CharSequence>> listOrRows() {
        return list(
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        R1C0, R1C1, R1C2
                ),
                list(
                        R2C0, R2C1, R2C2
                )
        );
    }

    private void columnAndCheck(final Table table,
                                final int column,
                                final CharSequence... text) {
        this.checkEquals(
                list(text),
                table.column(column),
                () -> "column " + column + " from " + table.toStringTest()
        );
    }

    private void rowAndCheck(final Table table,
                             final int row,
                             final CharSequence... text) {
        this.checkEquals(
                list(text),
                table.row(row),
                () -> "row " + row + " from " + table.toStringTest()
        );
    }

    @Override
    int width() {
        return 3;
    }

    @Override
    int height() {
        return 3;
    }

    @Override
    public Class<TableNotEmpty> type() {
        return TableNotEmpty.class;
    }
}
