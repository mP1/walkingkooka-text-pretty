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
import walkingkooka.text.CharSequences;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class TableNotEmptyTest extends TableTestCase3<TableNotEmpty>
        implements HashCodeEqualsDefinedTesting2<TableNotEmpty> {

    private final static CharSequence X = "x";
    private final static CharSequence Y = "y";
    private final static CharSequence Z = "z";
    private final static CharSequence A = "A";

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

    @Test
    public void testCellAbsentRowMissing() {
        this.cellAndCheck(
                TableNotEmpty.with(
                        list(
                                list(R0C0, R0C0),
                                list(R0C1, R1C1)
                        ),
                        2
                ),
                1,
                2,
                CharSequences.empty()
        );
    }

    @Test
    public void testCellAbsentColumnOob() {
        this.cellAndCheck(
                TableNotEmpty.with(
                        list(
                                list(R0C0, R0C0),
                                list(R0C1, R1C1)
                        ),
                        2
                ),
                2,
                1,
                CharSequences.empty()
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

        for(int c = 0; c < table.width(); c++) {
            for(int r = 0; r < table.height(); r++) {
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
                                R2C0, R2C1
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
                                R2C0, R2C1
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
                                null, R0C1, R0C2
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
                this.createTable().setCell(0, 0, ""),
                list(
                        list(
                                null, R0C1, R0C2
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
                                R0C0, R0C1
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
                this.createTable().setCell(2, 0, ""),
                list(
                        list(
                                R0C0, R0C1
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
    public void testSetCellAllNull() {
        Table table = TableNotEmpty.with(
                list(
                        list( A)
                ),
                1
        );
        assertSame(
                Table.empty(),
                table.setCell(0, 0, null)
        );
    }

    @Test
    public void testSetCellAllNull2() {
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
        Table table = TableNotEmpty.with(
                list(
                       list( A)
                ),
                1
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

        assertSame(Table.empty(), table);
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
    public void testColumnMissing() {
        this.columnAndCheck(
                this.createTable(),
                4
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
                CharSequences.empty(),
                CharSequences.empty()
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
                        null, R0C1, R0C2
                ),
                list(
                        null, R1C1, R1C2
                ),
                list(
                        null, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetColumnEmptyList2() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column),
                list(
                        R0C0, null, R0C2
                ),
                list(
                        R1C0, null, R1C2
                ),
                list(
                        R2C0, null, R2C2
                )
        );
    }

    @Test
    public void testSetColumnEmptyList3() {
        final int column = 2;

        this.check(
                this.setColumnAndCheck(column),
                list(
                        R0C0, R0C1
                ),
                list(
                        R1C0, R1C1
                ),
                list(
                        R2C0, R2C1
                )
        );
    }

    @Test
    public void testSetColumnWithListWithNulls() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, null, null),
                list(
                        null, R0C1, R0C2
                ),
                list(
                        null, R1C1, R1C2
                ),
                list(
                        null, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithNulls2() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, null, null),
                list(
                        R0C0, null, R0C2
                ),
                list(
                        R1C0, null, R1C2
                ),
                list(
                        R2C0, null, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithNulls3() {
        final int column = 2;

        this.check(
                this.setColumnAndCheck(column, null, null),
                list(
                        R0C0, R0C1
                ),
                list(
                        R1C0, R1C1
                ),
                list(
                        R2C0, R2C1
                )
        );
    }

    @Test
    public void testSetColumnWithListWithEmptyString() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, "", ""),
                list(
                        null, R0C1, R0C2
                ),
                list(
                        null, R1C1, R1C2
                ),
                list(
                        null, R2C1, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithEmptyString2() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, "", ""),
                list(
                        R0C0, null, R0C2
                ),
                list(
                        R1C0, null, R1C2
                ),
                list(
                        R2C0, null, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithEmptyString3() {
        final int column = 2;

        this.check(
                this.setColumnAndCheck(column, "", ""),
                list(
                        R0C0, R0C1
                ),
                list(
                        R1C0, R1C1
                ),
                list(
                        R2C0, R2C1
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
                        null, R1C1, R1C2
                ),
                list(
                        null, R2C1, R2C2
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
                        R1C0, null, R1C2
                ),
                list(
                        R2C0, null, R2C2
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
                        R1C0, R1C1
                ),
                list(
                        R2C0, R2C1
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
                        null, R1C1, R1C2
                ),
                list(
                        null, R2C1, R2C2
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
                        null, R1C1, R1C2
                ),
                list(
                        null, R2C1, R2C2
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
                        R1C0, null, R1C2
                ),
                list(
                        R2C0, null, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithFewerCellsIncludesNull() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, null, X),
                list(
                        null, R0C1, R0C2
                ),
                list(
                        X, R1C1, R1C2
                ),
                list(
                        null, R2C1, R2C2
                )
        );
    }


    @Test
    public void testSetColumnWithListWithFewerCellsIncludesNull2() {
        final int column = 1;

        this.check(
                this.setColumnAndCheck(column, null, X),
                list(
                        R0C0, null, R0C2
                ),
                list(
                        R1C0, X, R1C2
                ),
                list(
                        R2C0, null, R2C2
                )
        );
    }

    @Test
    public void testSetColumnWithListWithFewerCellsIncludesEmpty() {
        final int column = 0;

        this.check(
                this.setColumnAndCheck(column, "", X),
                list(
                        null, R0C1, R0C2
                ),
                list(
                        X, R1C1, R1C2
                ),
                list(
                        null, R2C1, R2C2
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
                        A
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
                        null, A
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
                        R2C0, null, R2C2
                ),
                list(
                        null, A
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
                        R2C0, null, R2C2
                ),
                list(
                        null, A
                )
        );
    }

    @Test
    public void testSetColumnWithListWithMoreCellsTrimmed() {
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
    public void testRowWithMissingCells() {
        this.rowAndCheck(
                this.createTable()
                        .setRow(1, list("A", null, "B")),
                1,
                "A",
                CharSequences.empty(),
                "B"
        );
    }

    @Test
    public void testRowMissing() {
        this.rowAndCheck(
                this.createTable(),
                4
        );
    }

    @Test
    public void testSetRowRow() {
        final int row = 1;
        this.rowAndCheck(
                this.createTable()
                        .setRow(row, list(R1C0)),
                row,
                R1C0,
                CharSequences.empty(),
                CharSequences.empty()
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
    public void testSetRow() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, X, Y, Z),
                list(
                        list(
                                X, Y, Z
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
    public void testSetRowEmptyList() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row),
                list(
                        null,
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
    public void testSetRowWithListWithNulls() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, null, null),
                list(
                        null,
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
    public void testSetRowWithListWithEmptyString() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, "", ""),
                list(
                        null,
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
    public void testSetRowWithListWithFewerCells() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, X),
                list(
                        list(
                                X
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
    public void testSetRowWithListWithFewerCellsTrailingNull() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, X, null),
                list(
                        list(
                                X
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
    public void testSetRowWithListWithFewerCellsTrailingEmpty() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, X, ""),
                list(
                        list(
                                X
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
    public void testSetRowWithListWithFewerCellsIncludesNull() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, null, X),
                list(
                        list(
                                null, X
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
    public void testSetRowWithListWithFewerCellsIncludesEmpty() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, "", X),
                list(
                        list(
                                null, X
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
    public void testSetRowWithListWithMoreCells() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, X, Y, Z, A),
                list(
                        list(
                                X, Y, Z, A
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
    public void testSetRowWithListWithMoreCellsIncludesNull() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, X, Y, null, A),
                list(
                        list(
                                X, Y, null, A
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
    public void testSetRowWithListWithMoreCellsIncludesEmpty() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, X, Y, "", A),
                list(
                        list(
                                X, Y, null, A
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
    public void testSetRowWithListWithMoreCellsIncludesNullAndEmpty() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, X, null, "", A),
                list(
                        list(
                                X, null, null, A
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
    public void testSetRowWithListWithMoreCellsIncludesNullAndEmptyAndTrimmed() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, X, null, "", A, null),
                list(
                        list(
                                X, null, null, A
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
    public void testSetRowWithListWithMoreCellsAllNullOrEmpty() {
        final int row = 0;

        this.check(
                this.setRowAndCheck(row, null, "", null, ""),
                list(
                        null,
                        list(
                                R1C0, R1C1, R1C2
                        ),
                        list(
                                R2C0, R2C1, R2C2
                        )
                )
        );
    }

    // equals...........................................................................................................

    @Test
    public void testDifferentCells() {
        this.checkNotEquals(
                TableNotEmpty.with(
                        list(
                                list(R0C0, R0C1)
                        ),
                        2
                ),
                TableNotEmpty.with(
                        list(
                                list(R0C0)
                        ),
                        1
                )
        );
    }

    @Test
    public void testDifferentCellsTypeSameContent() {
        this.checkNotEquals(
                TableNotEmpty.with(
                        list(
                                list(R0C0)
                        ),
                        1
                ),
                TableNotEmpty.with(
                        list(
                                list(new StringBuilder(R0C0))
                        ),
                        1
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
        return TableNotEmpty.with(
                this.listOrRows(),
                3
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
                () -> "column " + column + " from " + table
        );
    }

    private void rowAndCheck(final Table table,
                             final int row,
                             final CharSequence... text) {
        this.checkEquals(
                list(text),
                table.row(row),
                () -> "row " + row + " from " + table
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
