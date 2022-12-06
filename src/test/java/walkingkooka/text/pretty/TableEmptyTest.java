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

public final class TableEmptyTest extends TableTestCase3<TableEmpty> {

    @Test
    public void testWidth() {
        this.widthAndCheck(
                this.createObject(),
                0
        );
    }

    @Test
    public void testSetCell() {
        this.check(
                this.setCellAndCheck(
                        0,
                        0,
                        R0C0
                ),
                Lists.of(
                        list(R0C0)
                )
        );
    }

    @Test
    public void testSetCell2() {
        this.check(
                this.setCellAndCheck(
                        2,
                        0,
                        R0C2
                ),
                Lists.of(
                        list(MISSING, MISSING, R0C2)
                )
        );
    }

    @Test
    public void testSetCell3() {
        this.check(
                this.setCellAndCheck(
                        1,
                        1,
                        R1C1
                ),
                Lists.of(
                        list(MISSING, MISSING), // row 0
                        list(MISSING, R1C1)
                )
        );
    }

    @Test
    public void testSetColumn() {
        this.setColumnAndCheck(
                0,
                R0C0,
                R1C0,
                R2C0
        );
    }

    @Test
    public void testSetColumn2() {
        this.setColumnAndCheck(
                1,
                R0C1,
                R1C1,
                R2C1
        );
    }

    @Test
    public void testHeight() {
        this.heightAndCheck(
                this.createObject(),
                0
        );
    }

    @Test
    public void testSetRow() {
        this.createAndSetRow(0, R0C0, R0C1, R0C2);
    }

    @Test
    public void testSetRow2() {
        this.createAndSetRow(1, R1C0, R1C1, R1C2);
    }

    @Test
    public void testSetEmptyRow() {
        this.createAndSetRow(
                1
        );
    }

    // setRows.........................................................................................................

    @Test
    public void testSetRowsAtOrigin() {
        final Table expected = TableEmpty.INSTANCE.setRow(
                0,
                list(R0C0, R0C1, R0C2)
        );

        this.checkEquals(
                expected,
                TableEmpty.INSTANCE.setRows(
                        0,
                        0,
                        Lists.of(
                                list(R0C0, R0C1, R0C2)
                        )
                )
        );
    }

    @Test
    public void testSetRowsNonOriginEmpty() {
        final Table expected = TableEmpty.INSTANCE.setCell(
                2,
                2,
                ""
        );

        this.checkEquals(
                expected,
                TableEmpty.INSTANCE.setRows(
                        3,
                        3,
                        Lists.empty()
                )
        );
    }

    @Test
    public void testSetRowsNonOrigin() {
        final Table expected = TableEmpty.INSTANCE.setRow(
                2,
                list(MISSING, MISSING, R0C0, R0C1, R0C2)
        );

        this.checkEquals(
                expected,
                TableEmpty.INSTANCE.setRows(
                        2,
                        2,
                        Lists.of(
                                list(R0C0, R0C1, R0C2)
                        )
                )
        );
    }

    @Test
    public void testSetRowsManyRows() {
        final Table expected = TableEmpty.INSTANCE.setRow(
                1,
                list(R1C0, R1C1, R1C2)
        ).setRow(
                2,
                list(R2C0, R2C1, R2C2)
        );

        this.checkEquals(
                expected,
                TableEmpty.INSTANCE.setRows(
                        0,
                        1,
                        Lists.of(
                                list(R1C0, R1C1, R1C2), // row1
                                list(R2C0, R2C1, R2C2) // row2
                        )
                )
        );
    }

    @Test
    public void testSetRowsManyRows2() {
        final Table expected = TableEmpty.INSTANCE.setRow(
                1,
                list(R1C0)
        ).setRow(
                2,
                list(R2C0, R2C1, R2C2)
        );

        this.checkEquals(
                expected,
                TableEmpty.INSTANCE.setRows(
                        0,
                        1,
                        Lists.of(
                                list(R1C0), // row1
                                list(R2C0, R2C1, R2C2) // row2
                        )
                )
        );
    }

    @Test
    public void testSetRowsIncludesFirstNullRow() {
        final Table expected = TableEmpty.INSTANCE.setRow(
                2,
                list(R2C0, R2C1, R2C2)
        );

        this.checkEquals(
                expected,
                TableEmpty.INSTANCE.setRows(
                        0,
                        1,
                        Lists.of(
                                null, // row1
                                list(R2C0, R2C1, R2C2) // row2
                        )
                )
        );
    }

    @Test
    public void testSetRowsIncludesFirstEmptyRow() {
        final Table expected = TableEmpty.INSTANCE.setRow(
                2,
                list(R2C0, R2C1, R2C2)
        );

        this.checkEquals(
                expected,
                TableEmpty.INSTANCE.setRows(
                        0,
                        1,
                        Lists.of(
                                list(), // row1
                                list(R2C0, R2C1, R2C2) // row2
                        )
                )
        );
    }

    // setHeight........................................................................................................

    @Test
    public void testSetHeightIncreaseFromEmpty() {
        this.setHeightAndCheck(
                Table.empty(),
                3,
                Table.empty()
                        .setRow(2, Lists.empty())
        );
    }

    // setWidth.........................................................................................................

    @Test
    public void testSetWidthIncreaseFromEmpty() {
        this.setWidthAndCheck(
                Table.empty(),
                2,
                Table.empty()
                        .setColumn(
                                1,
                                list()
                        )
        );
    }

    @Test
    public void testSetWidthWithZero() {
        this.setWidthAndCheck(
                this.createTable(),
                0,
                Table.empty()
        );
    }

    @Test
    public void testSetWidthThenSetHeight() {
        this.checkEquals(
                Table.empty()
                        .setWidth(3)
                        .setHeight(4),
                Table.empty()
                        .setCell(2, 3, "")
        );
    }

    // setSize..........................................................................................................

    @Test
    public void testSetSizeIncreased() {
        this.setSizeAndCheck(
                Table.empty(),
                2,
                3,
                Table.empty()
                        .setRow(
                                3 - 1,
                                list(
                                        MISSING,
                                        MISSING
                                )
                        )
        );
    }

    @Test
    public void testSetSizeIncreased2() {
        this.setSizeAndCheck(
                Table.empty(),
                1,
                2,
                Table.empty()
                        .setRow(
                                2 - 1,
                                list(
                                        MISSING
                                )
                        )
        );
    }

    // setColumns.......................................................................................................

    @Test
    public void testSetColumnsAtOrigin() {
        final Table expected = TableEmpty.INSTANCE.setColumn(
                0,
                list(R0C0, R1C0, R2C0)
        );

        this.checkEquals(
                expected,
                TableEmpty.INSTANCE.setColumns(
                        0,
                        0,
                        Lists.of(
                                list(R0C0, R1C0, R2C0)
                        )
                )
        );
    }

    @Test
    public void testSetColumnsNonOriginEmpty() {
        final Table expected = TableEmpty.INSTANCE.setCell(
                2,
                2,
                ""
        );

        this.checkEquals(
                expected,
                TableEmpty.INSTANCE.setColumns(
                        3,
                        3,
                        Lists.empty()
                )
        );
    }

    @Test
    public void testSetColumnsNonOrigin() {
        final Table expected = TableEmpty.INSTANCE.setColumn(
                2,
                list(MISSING, MISSING, "X", "Y", "Z")
        );

        this.checkEquals(
                expected,
                TableEmpty.INSTANCE.setColumns(
                        2,
                        2,
                        Lists.of(
                                list("X", "Y", "Z")
                        )
                )
        );
    }
    
    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                TableEmpty.INSTANCE,
                Lists.empty().toString()
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                this.createTable(),
                "Table\n"
        );
    }

    // helpers..........................................................................................................

    @Override
    TableEmpty createTable() {
        return TableEmpty.INSTANCE;
    }

    @Override
    int width() {
        return 0;
    }

    @Override
    int height() {
        return 0;
    }

    @Override
    public Class<TableEmpty> type() {
        return TableEmpty.class;
    }
}
