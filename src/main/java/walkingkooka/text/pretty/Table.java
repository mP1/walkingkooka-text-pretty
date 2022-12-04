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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;

/**
 * A {link Table} is a two dimensional grid holding cells. Note that fetches for non existent rows or columns will return
 * {@link List empty lists} while columns/rows with less cells will return empty cells so all columns/rows appear to have
 * equal lengths.
 */
public abstract class Table {

    final static CharSequence MISSING_TEXT = CharSequences.empty();

    static void checkText(final List<CharSequence> text) {
        Objects.requireNonNull(text, "text");
    }

    static TableNotEmptyListRow copyRowText(final List<CharSequence> rowText) {
        TableNotEmptyListRow copy;

        if(rowText instanceof List) {
            if(rowText instanceof TableNotEmptyListRow) {
                final TableNotEmptyListRow listRow = (TableNotEmptyListRow)rowText;
                copy = listRow.copy();
            } else {
                copy = TableNotEmptyListRow.with(
                    TableNotEmptyList.computeCapacity(rowText.size())
                );
                copy.copy(rowText);
            }
        } else {
            copy = TableNotEmptyListRow.empty();
        }

        return copy;
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

    final public CharSequence cell(final int column,
                                   final int row) {
        checkColumn(column);
        checkRow(row);

        return this.cell0(
                column,
                row
        );
    }

    abstract CharSequence cell0(final int column,
                                final int row);

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
        checkColumn(column);

        final int width = this.width();
        if(column >= width) {
            throw new IndexOutOfBoundsException("Invalid column " + column + " >= " + width);
        }

        return this.column0(column);
    }

    abstract List<CharSequence> column0(final int column);

    /**
     * Would be setter that replaces an existing column. Any existing cells are replaced
     */
    public final Table setColumn(final int column,
                                 final List<CharSequence> text) {
        checkColumn(column);
        checkText(text);

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

    static void checkColumn(final int column) {
        if (column < 0) {
            throw new IndexOutOfBoundsException("Invalid column " + column);
        }
    }

    /**
     * The maximum number of columns with 0 indicating no columns.
     */
    public abstract int width();

    // row..............................................................................................................

    /**
     * Returns all the rows for the given row number.
     */
    public final List<CharSequence> row(final int row) {
        return this.rows()
                .get(row);
    }

    /**
     * Would be setter that replaces an existing row.
     */
    public final Table setRow(final int row,
                              final List<CharSequence> text) {
        checkRow(row);
        checkText(text);

        return this.setRow0(
                row,
                copyRowText(text)
        );
    }

    Table setRow0(final int row,
                  final TableNotEmptyListRow rowText) {
        return row >= height() ?
                this.addRow(
                        row,
                        rowText
                ) :
                this.replaceRow(
                        row,
                        rowText
                );
    }

    private Table addRow(final int row,
                         final TableNotEmptyListRow rowText) {
        return null == rowText ?
                this :
                this.addRow0(
                        row,
                        rowText
                );
    }

    abstract Table addRow0(final int row,
                           final TableNotEmptyListRow rowText);

    abstract Table replaceRow(final int row,
                              final TableNotEmptyListRow rowText);

    static void checkRow(final int row) {
        if (row < 0) {
            throw new IndexOutOfBoundsException("Invalid row " + row);
        }
    }

    // height...........................................................................................................

    /**
     * The number of rows with 0 indicating an empty table
     */
    abstract public int height();

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

    // rows.............................................................................................................

    abstract TableNotEmptyListRows rows();

    @GwtIncompatible
    abstract String toStringTest();
}