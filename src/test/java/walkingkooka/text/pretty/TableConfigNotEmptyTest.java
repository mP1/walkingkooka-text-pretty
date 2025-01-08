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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;

import java.util.List;

public final class TableConfigNotEmptyTest extends TableConfigTestCase<TableConfigNotEmpty> {

    @Test
    public void testAdd() {
        final ColumnConfig first = this.columns().get(0);
        final ColumnConfig second = ColumnConfig.empty().maxWidth(222).rightAlign();
        this.addColumnAndCheck(this.createTableConfig(),
                second,
                first,
                second);
    }

    // Function.........................................................................................................

    @Test
    public void testApplyNotEmptyTable() {
        final Table table = Table.empty()
                .setCell(0, 1, "cell 0-1");
        final Table expected = Table.empty()
                .setCell(0, 1, "    cell 0-1");
        this.applyAndCheck2(table, expected);
    }

    @Test
    public void testApplyNotEmptyTable2() {
        final Table table = Table.empty()
                .setCell(0, 1, "cell 0-1")
                .setCell(1, 2, "cell 1-2")
                .setCell(2, 3, "cell 2-3");
        final Table expected = Table.empty()
                .setCell(0, 1, "    cell 0-1")
                .setCell(1, 2, "cell 1-2")
                .setCell(2, 3, "cell 2-3");
        this.applyAndCheck2(table, expected);
    }

    @Test
    public void testApplyNotEmptyTable3() {
        final TableConfigNotEmpty config = TableConfigNotEmpty.with(Lists.of(ColumnConfig.empty().maxWidth(12).minWidth(12).rightAlign(),
                ColumnConfig.empty().maxWidth(10).minWidth(10).centerAlign()));

        final Table table = Table.empty()
                .setCell(0, 1, "cell 0-1")
                .setCell(1, 2, "cell 1-2")
                .setCell(2, 3, "cell 2-3");
        final Table expected = Table.empty()
                .setCell(0, 1, "    cell 0-1")
                .setCell(1, 2, " cell 1-2 ")
                .setCell(2, 3, "cell 2-3");
        this.applyAndCheck2(
                config,
                table,
                expected);
    }

    @Test
    public void testApplyExtraColumns() {
        final TableConfigNotEmpty config = TableConfigNotEmpty.with(Lists.of(ColumnConfig.empty().maxWidth(12).minWidth(12).rightAlign(),
                ColumnConfig.empty().maxWidth(10).minWidth(10).centerAlign()));

        final Table table = Table.empty()
                .setCell(0, 1, "cell 0-1");
        final Table expected = Table.empty()
                .setCell(0, 1, "    cell 0-1");
        this.applyAndCheck2(
                config,
                table,
                expected);
    }

    // ToString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createTableConfig(), this.columns().toString());
    }

    @Override
    TableConfigNotEmpty createTableConfig() {
        return TableConfigNotEmpty.with(this.columns());
    }

    private List<ColumnConfig> columns() {
        return Lists.of(ColumnConfig.empty()
                .maxWidth(12)
                .minWidth(12)
                .rightAlign());
    }

    private void applyAndCheck2(final Table input,
                                final Table expected) {
        this.applyAndCheck2(this.createFunction(), input, expected);
    }

    private void applyAndCheck2(final TableConfigNotEmpty config,
                                final Table input,
                                final Table result) {
        // toString required because CharSequence values may not be equal to each other, eg String != StringBuilder same content.
        Assertions.assertEquals(result.toString(), config.apply(input).toString(), () -> "Wrong result for " + config + " for params: " + input);
    }

    @Override
    public Class<TableConfigNotEmpty> type() {
        return TableConfigNotEmpty.class;
    }
}
