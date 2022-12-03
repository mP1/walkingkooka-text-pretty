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

import java.util.List;

public final class TableNotEmptyListRowTest extends TableNotEmptyListTestCase<TableNotEmptyListRow> {

    @Test
    public void testGet() {
        this.getAndCheck(this.createList(), 0, R1C0);
    }

    @Test
    public void testGet2() {
        this.getAndCheck(this.createList(), 2, R1C2);
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createList(), 3);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createList(), Lists.of(R1C0, R1C1, R1C2).toString());
    }

    @Override
    TableNotEmptyListRow createList(final List<List<CharSequence>> rows,
                                       final int column,
                                       final int row) {
        return TableNotEmptyListRow.with(1, TableNotEmpty.with(rows, row));
    }

    @Override
    public Class<TableNotEmptyListRow> type() {
        return TableNotEmptyListRow.class;
    }
}
