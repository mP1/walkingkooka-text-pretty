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

public final class TableCollectorColumnTest extends TableCollectorTestCase2<TableCollectorColumn> {

    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyTableAddColumn() {
        this.collectAndCheck(
                Table.empty(),
                0,
                Lists.of("column0-a", "column0-b")
        );
    }

    @SuppressWarnings("unchecked")

    @Test
    public void testEmptyTableAddColumns() {
        this.collectAndCheck(
                Table.empty(),
                0,
                Lists.of("column0-a", "column0-b"),
                Lists.of("column1-a", "column1-b")
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNotEmptyTableAddColumns() {
        this.collectAndCheck(
                Table.empty().setCell(2, 2, "cell-2-2"),
                0,
                Lists.of("column0-a", "column0-b"),
                Lists.of("column1-a", "column1-b")
        );
    }

    @Override
    Table add(final Table table,
              final int start,
              final List<CharSequence>... data) {
        Table result = table;

        int r = start;
        for (final List<CharSequence> d : data) {
            result = result.setColumn(r, d);
            r++;
        }

        return result;
    }

    @Override
    TableCollectorColumn createCollector(final Table table,
                                         final int next) {
        return TableCollectorColumn.with(table, next);
    }

    @Override
    String columnOrRow() {
        return "column";
    }

    @Override
    public Class<TableCollectorColumn> type() {
        return TableCollectorColumn.class;
    }
}
