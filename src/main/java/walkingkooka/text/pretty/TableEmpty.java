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

import javaemul.internal.annotations.GwtIncompatible;
import walkingkooka.text.CharSequences;

import java.util.List;

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

    // cell.............................................................................................................
    @Override
    CharSequence cell0(final int column,
                       final int row) {
        checkColumn(column);
        checkRow(row);

        return MISSING_CELL;
    }

    private final static CharSequence MISSING_CELL = CharSequences.empty();

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
        throw new UnsupportedOperationException();
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
    Table addRow0(final int row,
                  final TableNotEmptyListRow rowText) {
        return null == rowText ?
                this :
                TableNotEmpty.withRow(row, rowText);
    }

    @Override
    Table replaceRow(final int row,
                              final TableNotEmptyListRow rowText) {
        throw new UnsupportedOperationException();
    }

    // height...........................................................................................................

    @Override
    public int height() {
        return 0;
    }

    // rows.............................................................................................................

    @Override
    TableNotEmptyListRows rows() {
        return EMPTY_ROWS;
    }

    private final static TableNotEmptyListRows EMPTY_ROWS = TableNotEmptyListRows.with(0);

    // Object...........................................................................................................

    @GwtIncompatible
    @Override
    String toStringTest() {
        return "[]";
    }
}
