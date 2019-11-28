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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.util.FunctionTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TableConfigTestCase<T extends TableConfig> extends TextPrettyTestCase<T>
        implements FunctionTesting<T, Table, Table>,
        ToStringTesting<T> {

    TableConfigTestCase() {
        super();
    }

    @Test
    public final void testAddColumnFails() {
        assertThrows(NullPointerException.class, () -> this.createTableConfig().add(null));
    }

    final void addColumnAndCheck(final TableConfig table,
                                 final ColumnConfig column,
                                 final ColumnConfig...expected) {
        final TableConfigNotEmpty not = (TableConfigNotEmpty)table.add(column);
        assertEquals(Lists.of(expected), not.columns, table + "  add column " + column);
    }

    @Test
    public final void testApplyNullFails() {
        assertThrows(NullPointerException.class, () -> this.createTableConfig().add(null));
    }

    @Test
    public void testApplyEmptyTable() {
        this.applyAndCheck(Table.empty(), Table.empty());
    }

    abstract T createTableConfig();

    // Function.........................................................................................................

    @Override
    public final T createFunction() {
        return this.createTableConfig();
    }

    // TypeNameTesting...................................................................................................

    @Override
    public final String typeNamePrefix() {
        return TableConfig.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
