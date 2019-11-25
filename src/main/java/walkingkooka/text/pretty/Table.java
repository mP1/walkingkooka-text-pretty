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
import walkingkooka.text.CharSequences;

import java.util.List;

/**
 * A {link Table} is a two dimensional grid holding cells. Note that fetches for non existent rows or columns will return
 * {@link List empty lists} while columns/rows with less cells will return empty cells so all columns/rows appear to have
 * equal lengths.
 */
public abstract class Table {

    /**
     * Returns an empty {@link Table}.
     */
    static Table empty() {
        return TableEmpty.INSTANCE;
    }

    // ctor.............................................................................................................

    /**
     * Package private ctor to limit sub classing.
     */
    Table() {
        super();
    }

    /**
     * Fetches the cell at the given coordinates. Coordinates out of bounds will return an empty {@link CharSequence}.
     */
    public final CharSequence cell(final int column,
                                   final int row) {
        checkColumn(column);
        checkRow(row);

        return column >= this.maxColumn() ||
                row >= this.maxRow() ?
                CharSequences.empty() :
                this.cell0(column, row);
    }

    abstract CharSequence cell0(final int column,
                                final int row);

    // column...........................................................................................................

    /**
     * Returns all the columns for the given column number.
     */
    public final List<CharSequence> column(final int column) {
        this.checkColumn(column);

        return column >= this.maxColumn() ?
                Lists.empty() :
                this.column0(column);
    }

    abstract List<CharSequence> column0(final int column);

    /**
     * Would be setter that replaces an existing column.
     */
    public final Table setColumn(final int column, final List<CharSequence> text) {
        this.checkColumn(column);

        return this.setColumn0(column, Lists.immutable(text));
    }

    private Table setColumn0(final int column, final List<CharSequence> text) {
        return text.isEmpty() && column >= this.maxColumn() ?
                this :
                this.setColumn1(column, text);
    }

    abstract Table setColumn1(final int column, final List<CharSequence> text);

    final void checkColumn(final int column) {
        if (column < 0) {
            throw new IllegalArgumentException("Invalid column " + column);
        }
    }

    /**
     * The first invalid column number. 0 indicates an empty table
     */
    public abstract int maxColumn();

    // row..............................................................................................................

    /**
     * Returns all the rows for the given row number.
     */
    public final List<CharSequence> row(final int row) {
        this.checkRow(row);

        return row >= this.maxRow() ?
                Lists.empty() :
                this.row0(row);
    }

    abstract List<CharSequence> row0(final int row);

    /**
     * Would be setter that replaces an existing row.
     */
    public final Table setRow(final int row, final List<CharSequence> text) {
        this.checkRow(row);

        return this.setRow0(row, Lists.immutable(text));
    }

    private Table setRow0(final int row, final List<CharSequence> text) {
        return text.isEmpty() && row > this.maxRow() ?
                this :
                this.setRow1(row, text);
    }

    abstract Table setRow1(final int row, final List<CharSequence> text);

    final void checkRow(final int row) {
        if (row < 0) {
            throw new IllegalArgumentException("Invalid row " + row);
        }
    }
    
    /**
     * The first invalid row number. 0 indicates an empty table
     */
    public abstract int maxRow();
}
