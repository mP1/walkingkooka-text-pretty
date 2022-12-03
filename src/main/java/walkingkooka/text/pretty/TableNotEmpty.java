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
        final List<CharSequence> columns = Lists.array();
        setWithAutoExpandShrink(columns, column, text);

        final List<List<CharSequence>> rows = Lists.array();
        setWithAutoExpandShrink(rows, row, columns);

        return with(
                rows,
                column + 1
        );
    }

    /**
     * Factory called by {@link TableEmpty#setColumn1(int, List)}.
     */
    static TableNotEmpty withColumn(final int column,
                                    final List<CharSequence> columnText) {
        int last = columnText.size();
        while(last > 0) {
            last--;
            if(isNotEmpty(columnText.get(last))) {
                last++;
                break;
            }
        }

        final List<List<CharSequence>> rows = Lists.array();

        for(int i = 0; i < last; i++) {
            final CharSequence text = columnText.get(i);

            final List<CharSequence> rowText;
            if(isEmpty(text)) {
                rowText = null;
            } else {
                rowText = Lists.array();
                expand(
                        rowText,
                        column
                );
                rowText.add(text);
            }

            rows.add(rowText);
        }

        return with(
                rows,
                column + 1
        );
    }

    static TableNotEmpty withRow(final int row,
                                 final List<CharSequence> rowText) {
        final List<List<CharSequence>> rows = Lists.array();
        setWithAutoExpandShrink(rows, row, rowText);

        return with(
                rows,
                rowText.size()
        );
    }

    /**
     * Creates a new {@link TableNotEmpty} with the given cells and maximum column/row.
     */
    static TableNotEmpty with(final List<List<CharSequence>> rows,
                              final int width) {
        return new TableNotEmpty(rows, width);
    }

    /**
     * A helper that auto expands the row with null elements if necessary.
     */
    static <T> void setWithAutoExpandShrink(final List<T> list,
                                            final int index,
                                            final T element) {
        final int count = list.size();
        if (index < count) {
            list.set(index, element);
        } else {
            expand(
                    list,
                    index - list.size()
            );
            list.add(element);
        }

        int i = list.size();
        while (i > 0) {
            i--;
            if (null != list.get(i)) {
                break;
            }
            list.remove(i);
        }
    }

    /**
     * Private ctor use factory.
     */
    private TableNotEmpty(final List<List<CharSequence>> rows,
                          final int width) {
        super();
        this.rows = rows;
        this.width = width;
    }

    // setCell..........................................................................................................

    @Override
    Table setCell0(final int column,
                   final int row,
                   final CharSequence text) {
        final Table after;

        final CharSequence cell = this.cell(column, row);
        if(Objects.equals(text, cell)) {
            after = this;
        } else {
            final List<CharSequence> newRowText;

            final List<List<CharSequence>> rows = this.rows;
            if(row < rows.size()) {
                final List<CharSequence> oldRowText = rows.get(row);
                // copy old row of text and replace at column
                newRowText = Lists.array();
                if(null != oldRowText) {
                    newRowText.addAll(oldRowText);
                }
                setWithAutoExpandShrink(newRowText, column, text);
            } else {
                newRowText = Lists.array();
                setWithAutoExpandShrink(newRowText, column, text);
            }

            after = this.setRow(
                    row,
                    newRowText
            );
        }

        return after;
    }

    // column...........................................................................................................

    @Override
    List<CharSequence> column0(final int column) {
        return TableNotEmptyListColumn.with(
            column,
            this
        );
    }

    @Override
    Table setColumn1(final int column,
                     final List<CharSequence> columnText) {
        return columnText.equals(this.column(column)) ?
                this :
                this.replaceColumn(column, columnText);
    }

    private Table replaceColumn(final int column,
                                final List<CharSequence> columnText) {
        Table after = this;

        final int height = Math.max(
                this.height(),
                columnText.size()
        );
        final int columnTextCount = columnText.size();

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
        if(-1 == this.width) {
            this.width = this.rows.stream()
                    .filter(Objects::nonNull)
                    .mapToInt(List::size)
                    .max()
                    .orElse(0);
        }
        return this.width;
    }

    private int width;

    // row..............................................................................................................

    @Override
    List<CharSequence> row0(final int row) {
        return TableNotEmptyListRow.with(
                row,
                this
        );
    }

    @Override
    Table setRow1(final int row,
                  final List<CharSequence> rowText) {
        return Objects.equals(rowText, this.row(row)) ?
                this :
                this.replaceRow(row, rowText);
    }

    private Table replaceRow(final int row,
                             final List<CharSequence> rowText) {
        final List<List<CharSequence>> newRows = Lists.array();
        newRows.addAll(this.rows);
        setWithAutoExpandShrink(newRows, row, rowText);

        final Table after;

        if (newRows.isEmpty()) {
            after = empty();
        } else {
            int width = -1;
            if(null != rowText) {
                width = rowText.size();
                if(width < this.width) {
                    width = -1;
                }
            }

            after = new TableNotEmpty(
                    newRows,
                    width
            );
        }

        return after;
    }

    // internal..............................................................................................................)

    @Override
    List<List<CharSequence>> asList() {
        return this.rows;
    }

    /**
     * A {@link List} of rows, with empty rows being nulls.
     */
    final List<List<CharSequence>> rows;

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
        return this.asList().equals(other.asList());
    }

    @Override
    public String toString() {
        return this.rows.toString();
    }
}
