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

import java.util.List;
import java.util.Objects;

/**
 * A {@link Table} that is not empty and has at least one cell. Note for purposes of equality cells are equal if they have
 * the same coordinates and same character content, {@link CharSequence} type is not important.
 */
final class TableNotEmpty extends Table {

    /**
     * Factory called by {@link TableEmpty#setColumn1(int, List)}.
     */
    static TableNotEmpty withCell(final int column,
                                  final int row,
                                  final CharSequence text) {
        final TableNotEmptyListRow rowText = TableNotEmptyListRow.with(column + 1);
        rowText.setAuto(
                column,
                text
        );

        return withRow(
                row,
                rowText
        );
    }

    /**
     * Factory called by {@link TableEmpty#setColumn1(int, List)}.
     */
    static TableNotEmpty withColumn(final int column,
                                    final List<CharSequence> columnText) {
        final TableNotEmptyListRows rows = TableNotEmptyListRows.empty();

        final int width = column + 1;
        int row = 0;

        for(final CharSequence text : columnText) {
            if(isNotEmpty(text)) {
                final TableNotEmptyListRow rowText = TableNotEmptyListRow.with(width);
                rowText.setAuto(
                        column,
                        text
                );

                rows.setAuto(
                        row,
                        rowText
                );
            }

            row++;
        }

        rows.setWidth(width);

        return with(
                rows,
                width
        );
    }

    static TableNotEmpty withRow(final int row,
                                 final TableNotEmptyListRow rowText) {
        final TableNotEmptyListRows rows = TableNotEmptyListRows.with(row + 1);

        rows.setAuto(row, rowText);

        final int width = rowText.size;
        rowText.setWidth(width);

        return with(
                rows,
                width
        );
    }

    /**
     * Creates a new {@link TableNotEmpty} with the given cells and maximum column/row.
     */
    static TableNotEmpty with(final TableNotEmptyListRows rows,
                              final int width) {
        return new TableNotEmpty(rows, width);
    }

    /**
     * Private ctor use factory.
     */
    private TableNotEmpty(final TableNotEmptyListRows rows,
                          final int width) {
        super();

        if(width < 0) {
            throw new IllegalArgumentException("Invalid width " + width + " < 0");
        }

        this.rows = rows;
        rows.missing.width = width;
        this.width = width;
    }

    // cell.............................................................................................................

    /**
     * Fetches the cell at the given coordinates. Coordinates that are out of bounds will result in a {@link IndexOutOfBoundsException}.
     */
    @Override
    CharSequence cell0(final int column,
                       final int row) {
        return this.rows.get(row)
                .get(column);
    }

    // setCell..........................................................................................................

    @Override
    Table setCell0(final int column,
                   final int row,
                   final CharSequence text) {
        final Table after;

        final int width = this.width();
        final int height = this.height();

        if(column >- width) {
            if(row >= height) {
                after = this.addRow(
                        column,
                        row,
                        text
                );
            } else {
                after = this.addCell(
                        column,
                        row,
                        text
                );
            }
        } else {
            after = this.replaceCell(
                    column,
                    row,
                    text
            );
        }

        return after;
    }

    private Table addRow(final int column,
                         final int row,
                         final CharSequence text) {
        final TableNotEmptyListRow rowText = TableNotEmptyListRow.with(
                TableNotEmptyList.computeCapacity(column)
        );
        rowText.setAuto(column, text);

        return this.addRow0(
                row,
                rowText
        );
    }

    private Table replaceCell(final int column,
                              final int row,
                              final CharSequence text) {
        final Table after;

        final CharSequence cell = this.cell(column, row);
        if(Objects.equals(text, cell)) {
            after = this;
        } else {
            after = this.addCell(
                    column,
                    row,
                    text
            );
        }

        return after;
    }

    private Table addCell(final int column,
                              final int row,
                              final CharSequence text) {
            TableNotEmptyListRow rowText = this.rows.get(row)
                    .copy();

            rowText.setAuto(
                    column,
                    text
            );

            return this.setRow0(
                    row,
                    0 == rowText.elementCount ? null : rowText
            );
    }

    // column...........................................................................................................

    @Override
    List<CharSequence> column0(final int column) {
        return TableNotEmptyColumnList.with(
            column,
            this
        );
    }

    @Override
    Table setColumn1(final int column,
                     final List<CharSequence> columnText) {
        Table after = this;

        final int height = Math.max(
                this.height(),
                columnText.size()
        );
        final int columnTextCount = columnText.size();

        // TODO implement a bulk update form.
        for(int row = 0; row < height; row++) {
            after = after.setCell(
                    column,
                    row,
                    row < columnTextCount ?
                            columnText.get(row) :
                            null
            );
        }

        return after;
    }

    @Override
    public int width() {
        return this.width;
    }

    // the width should never be 0.
    private final int width;

    // row..............................................................................................................

    @Override
    Table addRow0(final int row,
                  final TableNotEmptyListRow rowText) {
        final TableNotEmptyListRows newRows = this.rows.copy();
        newRows.setAuto(
                row,
                rowText
        );

        final int rowTextWidth = rowText.size;
        int width = this.width;
        if(rowTextWidth > width) {
            newRows.setWidth(rowTextWidth);
            width = rowTextWidth;
        } else {
            rowText.setWidth(width);
        }

        return with(
                newRows,
                width
        );
    }

    @Override
    Table replaceRow(final int row,
                     final TableNotEmptyListRow rowText) {
        final Table after;

        final TableNotEmptyListRows rows = this.rows();
        final TableNotEmptyListRow previous = rows.get(row);
        if (Objects.equals(rowText, previous)) {
            after = this;
        } else {
            final TableNotEmptyListRows newRows = rows.copy();
            newRows.setAuto(row, rowText);

            if (0 == newRows.elementCount) {
                after = empty();
            } else {
                // different row text...
                final int currentWidth = this.width();
                int width;

                if (null == rowText) {
                    width = currentWidth;
                } else {
                    width = rowText.size;
                    if (width >= currentWidth) {
                        width = rowText.size;
                        newRows.setWidth(width);
                    } else {
                        // previous row text was width
                        if (previous.size == currentWidth) {
                            width = newRows.findAndSetWidth();
                        } else {
                            width = currentWidth;
                            rowText.setWidth(width);
                        }
                    }
                }

                after = new TableNotEmpty(
                        newRows,
                        width
                );
            }
        }

        return after;
    }

    // height...........................................................................................................

    @Override
    public int height() {
        return this.rows.size;
    }

    @Override
    TableNotEmptyListRows rows() {
        return this.rows;
    }

    /**
     * A {@link List} of rows, with empty rows being nulls.
     */
    TableNotEmptyListRows rows;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.rows.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof Table &&
                        this.equals0((Table) other);
    }

    private boolean equals0(final Table other) {
        return this.rows().equals(other.rows());
    }

    @GwtIncompatible
    @Override
    String toStringTest() {
        return this.rows.toStringTest();
    }
}
