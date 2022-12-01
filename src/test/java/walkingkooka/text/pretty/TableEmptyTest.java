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
import walkingkooka.collect.map.Maps;

public final class TableEmptyTest extends TableTestCase3<TableEmpty> {

    @Test
    public void testMaxColumns() {
        this.checkEquals(0, this.createObject().maxColumn());
    }

    @Test
    public void testSetCell() {
        this.checkMap(this.setCellAndCheck(0, 0, R0C0), Maps.of(TableCellCoordinates.with(0, 0), R0C0));
    }

    @Test
    public void testSetCell2() {
        this.checkMap(this.setCellAndCheck(1, 2, R2C1), Maps.of(TableCellCoordinates.with(1, 2), R2C1));
    }

    @Test
    public void testSetColumn() {
        this.setColumnAndCheck(0, R0C0, R1C0, R2C0);
    }

    @Test
    public void testSetColumn2() {
        this.setColumnAndCheck(1, R0C1, R1C1, R2C1);
    }

    @Test
    public void testMaxRows() {
        this.checkEquals(0, this.createObject().maxColumn());
    }

    @Test
    public void testSetRow() {
        this.setRowAndCheck(0, R0C0, R0C1, R0C2);
    }

    @Test
    public void testSetRow2() {
        this.setRowAndCheck(1, R1C0, R1C1, R1C2);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(TableEmpty.INSTANCE, Maps.empty().toString());
    }

    @Override
    TableEmpty createTable() {
        return TableEmpty.INSTANCE;
    }

    @Override
    int maxColumn() {
        return 0;
    }

    @Override
    int maxRow() {
        return 0;
    }

    @Override
    public Class<TableEmpty> type() {
        return TableEmpty.class;
    }
}
