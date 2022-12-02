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
import walkingkooka.collect.map.Maps;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

/**
 * An empty {@link Table} all rows and columns are empty.
 */
final class TableEmpty extends Table {

    /**
     * Singleton
     */
    final static TableEmpty INSTANCE = new TableEmpty();

    /**
     * Private ctor use singleton.
     */
    private TableEmpty() {
        super();
    }

    // setCell..........................................................................................................

    /**
     * Creates a new {@link TableNotEmpty} with one cell.
     */
    @Override
    Table setCell0(final int column,
                   final int row,
                   final CharSequence text) {
        return TableNotEmpty.withCell(column, row, text);
    }

    // column...........................................................................................................

    @Override
    List<CharSequence> column0(final int row) {
        return Lists.empty();
    }

    @Override
    Table setColumn1(final int column,
                     final List<CharSequence> text) {
        return TableNotEmpty.withColumn(column, text);
    }

    @Override
    public int width() {
        return 0;
    }

    // row..............................................................................................................

    @Override
    List<CharSequence> row0(final int row) {
        return Lists.empty();
    }

    @Override
    Table setRow1(final int row, final List<CharSequence> text) {
        return text.isEmpty() ?
                this :
                TableNotEmpty.withRow(row, text);
    }

    // support..........................................................................................................

    @Override
    List<List<CharSequence>> asList() {
        return Lists.empty();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return "{}";
    }
}
