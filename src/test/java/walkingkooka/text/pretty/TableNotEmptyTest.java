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

public final class TableNotEmptyTest extends TableTestCase3<TableNotEmpty>
        implements HashCodeEqualsDefinedTesting2<TableNotEmpty> {

    private final static CharSequence X = "x";
    private final static CharSequence Y = "y";
    private final static CharSequence Z = "z";
    private final static CharSequence A = "A";

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

    //    @Test
//    public void testCellAbsentRowMissing() {
//        this.cellAndCheck(
//                this.createTable(
//                        2,
//                        list(R0C0, R0C0),
//                        list(R0C1, R1C1)
//                ),
//                1,
//                2,
//                MISSING
//        );
//    }
//
//    @Test
//    public void testCellAbsentColumnOob() {
//        this.cellAndCheck(
//                this.createTable(
//                        2,
//                        list(R0C0, R0C0),
//                        list(R0C1, R1C1)
//                ),
//                2,
//                1,
//                MISSING
//        );
//    }

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
                        list("A", "B", "C"),
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
                        list("A", null, "C") // row 1
                ),
                1,
                "A",
                MISSING,
                "C"
        );
    }

    @Test
    public void testRowWithMissingCells2() {
        this.rowAndCheck(
                this.createTable(
                        3,
                        null,
                        list("A", "", "C")
                ),
                1,
                "A",
                MISSING,
                "C"
        );
    }

    @Test
    public void testSetRowFullRow() {
        final int row = 1;
        this.check(
                this.createTable()
                        .setRow(
                                row,
                                list("A", "B", "C")
                        ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        "A", "B", "C"
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
                                list(null, "", "C")
                        ),
                list(
                        R0C0, R0C1, R0C2
                ),
                list(
                        MISSING, MISSING, "C"
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
                                list("A", "B", "C", "D")
                        ),
                list(
                        R0C0, R0C1, R0C2, MISSING
                ),
                list(
                        "A", "B", "C", "D"
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
