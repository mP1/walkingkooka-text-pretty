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
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;

/**
 * A {link Table} is a two dimensional grid holding cells. Note that fetches for non existent rows or columns will return
 * {@link List empty lists} while columns/rows with less cells will return empty cells so all columns/rows appear to have
 * equal lengths.
 */
public abstract class Table implements TreePrintable {

    final static CharSequence MISSING_TEXT = CharSequences.empty();

    static void checkText(final List<CharSequence> text) {
        Objects.requireNonNull(text, "text");
    }

    static TableNotEmptyListRow copyRowText(final List<CharSequence> rowText) {
        TableNotEmptyListRow copy;

        if (null == rowText) {
            copy = TableNotEmptyListRow.empty();
        } else {
            if (rowText instanceof TableNotEmptyListRow) {
                final TableNotEmptyListRow listRow = (TableNotEmptyListRow) rowText;
                copy = listRow.copy();
            } else {
                copy = TableNotEmptyListRow.with(
                        TableNotEmptyList.computeCapacity(rowText.size())
                );
                copy.copy(rowText);
            }
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

    // setRows.........................................................................................................

    /**
     * A bulk operation that may be used to update a window within this {@link Table}.
     */
    public final Table setRows(final int startColumn,
                               final int startRow,
                               final List<List<CharSequence>> windowText) {
        // startColumn and startRow must
        checkColumn(startColumn);
        checkRow(startRow);
        checkWindowText(windowText);

        return windowText.isEmpty() ?
                0 == startColumn && 0 == startRow ?
                        this :
                        this.setRowsEmpty(
                                startColumn,
                                startRow
                        ) :
                this.setRowsNotEmpty(
                        startColumn,
                        startRow,
                        windowText
                );
    }

    private Table setRowsEmpty(final int startColumn,
                               final int startRow) {
        return this.setSize(
                startColumn,
                startRow
        );
    }

    private Table setRowsNotEmpty(final int startColumn,
                                  final int startRow,
                                  final List<List<CharSequence>> windowText) {
        final TableNotEmptyListRows rows = this.rows();
        final TableNotEmptyListRows newRows = TableNotEmptyListRows.with(
                rows.elements.length
        );

        final int height = this.height();

        // copy rows before...
        int row = 0;
        int newWidth = this.width();

        while (row < startRow) {
            TableNotEmptyListRow rowText = null;

            if (row < height) {
                rowText = rows.get(row)
                        .copy();
            }

            newRows.setAuto(
                    row,
                    rowText
            );

            row++;
        }

        // copy window rows...
        for (final List<CharSequence> rowText : windowText) {
            final TableNotEmptyListRow newTableRow = TableNotEmptyListRow.empty();

            int column = 0;

            // copy columns before
            TableNotEmptyListRow oldTableRow = null;
            if (row < height) {
                oldTableRow = rows.get(row);

                while (column < startColumn) {
                    newTableRow.setAuto(
                            column,
                            oldTableRow.get(column)
                    );
                    column++;
                }
            } else {
                column = startColumn;
            }

            // copy window row.
            if (null != rowText) {
                for (final CharSequence text : rowText) {
                    newTableRow.setAuto(
                            column,
                            text
                    );
                    column++;
                }
            }

            // copy the columns after window
            if (null != oldTableRow) {
                final int endOfRow = oldTableRow.size;

                while (column < endOfRow) {
                    newTableRow.setAuto(
                            column,
                            oldTableRow.get(column)
                    );
                    column++;
                }
            }

            newWidth = Math.max(
                    newWidth,
                    newTableRow.size
            );

            newRows.setAuto(
                    row,
                    newTableRow
            );

            row++;
        }

        // copy rows after...
        while (row < height) {
            newRows.setAuto(
                    row,
                    rows.get(row)
                            .copy()
            );

            row++;
        }

        final Table table;
        if (rows.isEmpty()) {
            table = empty();
        } else {
            newRows.setWidth(newWidth);
            table = TableNotEmpty.with(
                    newRows,
                    newWidth
            );
        }

        return table;
    }

    // setColumns.......................................................................................................

    /**
     * Converts the columns of text into rows of text and then calls {@link #setRows(int, int, List)}.
     */
    public final Table setColumns(final int startColumn,
                                  final int startRow,
                                  final List<List<CharSequence>> windowText) {
        checkColumn(startColumn);
        checkRow(startRow);
        checkWindowText(windowText);

        final List<List<CharSequence>> rowWindowText = Lists.autoExpandArray();

        int column = 0;

        for (final List<CharSequence> columnText : windowText) {

            if (null != columnText && !columnText.isEmpty()) {

                int row = 0;
                for (final CharSequence cellText : columnText) {
                    if (null != cellText) {
                        List<CharSequence> rowText = rowWindowText.get(row);
                        if (null == rowText) {
                            rowText = Lists.autoExpandArray();
                        }
                        rowWindowText.set(row, rowText);

                        rowText.set(
                                column,
                                cellText
                        );
                    }
                    row++;
                }
            }

            column++;
        }

        return this.setRows(
                startColumn,
                startRow,
                rowWindowText
        );
    }

    private static void checkWindowText(final List<List<CharSequence>> windowText) {
        Objects.requireNonNull(
                windowText,
                "windowText"
        );
    }

    // column...........................................................................................................

    /**
     * Returns all the columns for the given column number.
     */
    public final List<CharSequence> column(final int column) {
        checkColumn(column);

        final int width = this.width();
        if (column >= width) {
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

    // width............................................................................................................

    /**
     * The maximum number of columns with 0 indicating no columns.
     */
    public abstract int width();

    /**
     * Returns a {@link Table} with the given width. The width may increase (adding empty columns) or decrease the width,
     * causing the extra columns to be lost.
     */
    public final Table setWidth(final int width) {
        return this.setSize(
                width,
                this.height()
        );
    }

    // setSize..........................................................................................................

    public final Table setSize(final int width,
                               final int height) {
        checkWidth(width);
        checkHeight(height);

        return 0 == width && 0 == height ?
                Table.empty() :
                this.setSizeNotEmpty(
                        width,
                        height
                );
    }

    private Table setSizeNotEmpty(final int width,
                                  final int height) {
        final Table table;

        final int currentWidth = this.width();
        final int currentHeight = this.height();

        // unchanged width and height return this...
        if (width == currentWidth && height == currentHeight) {
            table = this;
        } else {
            final TableNotEmptyListRows rows = this.rows()
                    .copy();

            int newWidth = width;

            if (height != currentHeight) {
                rows.size = height;

                if (height < currentHeight) {
                    if (currentWidth == newWidth) {
                        rows.findAndSetWidth(); // width didnt change but height did, need to find new width
                    }
                }
            }

            if (currentWidth != newWidth) {
                rows.setWidth(newWidth);
            }

            table = TableNotEmpty.with(
                    rows,
                    width
            );
        }

        return table;
    }

    private static void checkWidth(final int width) {
        if (width < 0) {
            throw new IllegalArgumentException("Invalid width " + width + " < 0");
        }
    }

    private static void checkHeight(final int height) {
        if (height < 0) {
            throw new IllegalArgumentException("Invalid height " + height + " < 0");
        }
    }

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

    /**
     * Returns a {@link Table} with the given height.
     */
    public final Table setHeight(final int height) {
        return this.setSize(
                this.width(),
                height
        );
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

    // rows.............................................................................................................

    abstract TableNotEmptyListRows rows();

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.rows().toString();
    }

    @GwtIncompatible
    abstract String toStringTest();

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println("Table");

        printer.indent();
        {
            final int width = this.width();
            if (width > 0) {
                printer.println("width: " + width);
            }
            final int height = this.height();
            if (height > 0) {
                printer.println("height: " + height);
            }

            int row = 0;
            for (final List<CharSequence> rowText : this.rows()) {
                printer.print("row: ");
                printer.println(String.valueOf(row));

                printer.indent();
                {
                    for (final CharSequence text : rowText) {
                        printer.println(
                                CharSequences.quoteAndEscape(text)
                        );
                    }
                }

                printer.outdent();

                row++;
            }
        }
        printer.outdent();
    }
}