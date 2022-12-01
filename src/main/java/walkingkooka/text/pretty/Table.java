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
import java.util.stream.Collector;

/**
 * A {link Table} is a two dimensional grid holding cells. Note that fetches for non existent rows or columns will return
 * {@link List empty lists} while columns/rows with less cells will return empty cells so all columns/rows appear to have
 * equal lengths.
 */
public abstract class Table {

    /**
     * Copies the given row of text, trimming null entries on the end, returning null if all are missing.
     */
    static List<CharSequence> copyRowText(final List<CharSequence> rowText) {
        List<CharSequence> copied = null;

        int column = rowText.size();

        if(0 != column) {
            // find a non null/empty
            while(column > 0) {
                column--;

                final CharSequence lastText = rowText.get(column);
                if(isNotEmpty(lastText)) {
                    copied = Lists.array();

                    int c = 0;
                    while(c <= column) {
                        final CharSequence text = rowText.get(c);
                        copied.add(
                                isEmpty(text) ?
                                        null :
                                        text
                        );
                        c++;
                    }

                    break;
                }
            }
        }

        return copied; // could be null
    }

    static boolean isEmpty(final CharSequence text) {
        return null == text || text.length() == 0;
    }

    static boolean isNotEmpty(final CharSequence text) {
        return null != text && text.length() > 0;
    }

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

    // cell.............................................................................................................

    /**
     * Fetches the cell at the given coordinates. Coordinates out of bounds will return an empty {@link CharSequence}.
     */
    public final CharSequence cell(final int column,
                                   final int row) {
        checkColumn(column);
        checkRow(row);

        return column >= this.width() ||
                row >= this.height() ?
                CharSequences.empty() :
                this.cell0(column, row);
    }

    private CharSequence cell0(final int column,
                                final int row) {
        final List<CharSequence> rowText = this.asList().get(row);

        CharSequence text;

        if(null != rowText && column < rowText.size()) {
            text = rowText.get(column);
            if(null == text) {
                text = CharSequences.empty();
            }
        } else {
            text = CharSequences.empty();
        }

        return text;
    }

    // setCell..........................................................................................................

    /**
     * Sets or replaces the cell at the given coordinates.
     */
    public final Table setCell(final int column,
                               final int row,
                               final CharSequence text) {
        checkColumn(column);
        checkRow(row);

        return this.setCell0(
                column,
                row,
                text
        );
    }

    /**
     * Sets a cell to the given coordinates.
     */
    abstract Table setCell0(final int column,
                            final int row,
                            final CharSequence text);

    // column...........................................................................................................

    /**
     * Returns all the columns for the given column number.
     */
    public final List<CharSequence> column(final int column) {
        this.checkColumn(column);

        return column >= this.width() ?
                Lists.empty() :
                this.column0(column);
    }

    abstract List<CharSequence> column0(final int column);

    /**
     * Would be setter that replaces an existing column. Any existing cells are replaced
     */
    public final Table setColumn(final int column, final List<CharSequence> text) {
        this.checkColumn(column);

        return this.setColumn0(
                column,
                Lists.immutable(text)
        );
    }

    private Table setColumn0(final int column,
                             final List<CharSequence> text) {
        return text.isEmpty() && column >= this.width() ?
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
     * The maximum number of columns
     */
    public abstract int width();

    // row..............................................................................................................

    /**
     * Returns all the rows for the given row number.
     */
    public final List<CharSequence> row(final int row) {
        this.checkRow(row);

        return row >= this.height() ?
                Lists.empty() :
                this.row0(row);
    }

    abstract List<CharSequence> row0(final int row);

    /**
     * Would be setter that replaces an existing row.
     */
    public final Table setRow(final int row,
                              final List<CharSequence> text) {
        this.checkRow(row);

        return this.setRow0(
                row,
                copyRowText(text)
        );
    }

    private Table setRow0(final int row,
                          final List<CharSequence> text) {
        return (null == text || text.isEmpty()) && row >= this.height() ?
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
     * The last valid row number. 0 indicates an empty table
     */
    public final int height() {
        return this.asList().size();
    }

    // Collector........................................................................................................

    /**
     * Returns a {@link Collector} that adds columns to this {@link Table} starting at the given column.
     */
    public final Collector<List<CharSequence>, Table, Table> collectColumn(final int column) {
        return TableCollectorColumn.with(this, column);
    }

    /**
     * Returns a {@link Collector} that adds rows to this {@link Table} starting at the given row.
     */
    public final Collector<List<CharSequence>, Table, Table> collectRow(final int row) {
        return TableCollectorRow.with(this, row);
    }

    // internal.........................................................................................................

    abstract List<List<CharSequence>> asList();
}