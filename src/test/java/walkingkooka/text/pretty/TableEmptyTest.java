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
    public void testToString() {
        this.toStringAndCheck(TableEmpty.INSTANCE, Maps.empty().toString());
    }

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
