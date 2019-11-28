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

import walkingkooka.collect.list.Lists;

/**
 * A {@link TableConfig} with no columns, also used as the starting point so columns can be added.
 */
final class TableConfigEmpty extends TableConfig {

    /**
     * A singleton
     */
    final static TableConfigEmpty INSTANCE = new TableConfigEmpty();

    /**
     * Use singleton
     */
    private TableConfigEmpty() {
        super();
    }

    @Override
    TableConfig add0(final ColumnConfig column) {
        return TableConfigNotEmpty.with(Lists.of(column));
    }

    @Override
    Table apply0(final Table table) {
        return table;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return Lists.empty().toString();
    }
}
