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

import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;

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
        final NavigableMap<TableCellCoordinates, CharSequence> table = map();
        table.put(TableCellCoordinates.with(column, row), text);
        return with(table,
                column + 1,
                row + 1);
    }

    /**
     * Factory called by {@link TableEmpty#setColumn1(int, List)}.
     */
    static TableNotEmpty withColumn(final int column,
                                    final List<CharSequence> text) {
        final NavigableMap<TableCellCoordinates, CharSequence> table = map();
        setColumn(column, text, table);
        return with(table,
                column + 1,
                text.size());
    }

    /**
     * Accepts a table and sets the cells for the given column.
     */
    private static void setColumn(final int column,
                                  final List<CharSequence> text,
                                  final NavigableMap<TableCellCoordinates, CharSequence> table) {
        int r = 0;
        for (final CharSequence t : text) {

            // skip saving/copying empty cells.
            if(t.length() > 0) {
                table.put(TableCellCoordinates.with(column, r), t);
            }
            r++;
        }
    }

    static TableNotEmpty withRow(final int row,
                                 final List<CharSequence> text) {
        final NavigableMap<TableCellCoordinates, CharSequence> table = map();
        setRow(row, text, table);
        return with(table,
                text.size(),
                row + 1);
    }

    /**
     * Accepts a table and sets the cells for the given row.
     */
    private static void setRow(final int row,
                               final List<CharSequence> text,
                               final NavigableMap<TableCellCoordinates, CharSequence> table) {
        int c = 0;
        for (final CharSequence t : text) {
            // skip "saving" empty rows.
            if(t.length() > 0) {
                table.put(TableCellCoordinates.with(c, row), t);
            }
            c++;
        }
    }

    /**
     * Creates a new {@link TableNotEmpty} with the given cells and maximum column/row.
     */
    static TableNotEmpty with(final NavigableMap<TableCellCoordinates, CharSequence> table,
                              final int column,
                              final int row) {
        return new TableNotEmpty(table, column, row);
    }

    /**
     * Private ctor use factory.
     */
    private TableNotEmpty(final NavigableMap<TableCellCoordinates, CharSequence> table,
                          final int maxColumn,
                          final int maxRow) {
        super();
        this.table = table;
        this.maxColumn = maxColumn;
        this.maxRow = maxRow;
    }

    // cell.............................................................................................................

    /**
     * Attempts to locate the cell at the given coords or returns {@link CharSequences#empty()}.
     */
    @Override
    CharSequence cell0(final int column,
                       final int row) {
        return this.cell1(TableCellCoordinates.with(column, row));
    }

    CharSequence cell1(final TableCellCoordinates cell) {
        return this.table.getOrDefault(cell, CharSequences.empty());
    }

    // setCell..........................................................................................................

    @Override
    Table setCell0(final int column,
                   final int row,
                   final CharSequence text) {
        final TableCellCoordinates coordinates = TableCellCoordinates.with(column, row);
        final CharSequence previous = this.table.get(coordinates);
        return text.equals(previous) ?
                this :
                this.replaceCell(coordinates, text);
    }

    /**
     * Replaces the cell at the given coordinates, if the new cell is empty and the current table is empty a
     * {@link #empty()} will be returned. This assumes that the new cell is different from the old.
     */
    private Table replaceCell(final TableCellCoordinates coordinates,
                              final CharSequence text) {
        final NavigableMap<TableCellCoordinates, CharSequence> table = map();
        if (text.length() > 0) {
            table.put(coordinates, text);
        }

        int maximumRow = coordinates.row + 1;

        for (final Entry<TableCellCoordinates, CharSequence> cellAndText : this.table.entrySet()) {
            final TableCellCoordinates cellCoordinates = cellAndText.getKey();
            if (false == coordinates.equals(cellCoordinates)) {
                table.put(cellCoordinates, cellAndText.getValue());
                maximumRow = Math.max(maximumRow, cellCoordinates.row + 1);
            }
        }

        return table.isEmpty() ?
                empty() :
                with(table,
                        table.lastEntry().getKey().column + 1,
                        maximumRow);
    }

    // column...........................................................................................................

    @Override
    List<CharSequence> column0(final int column) {
        return TableNotEmptyListColumn.with(column, this);
    }

    @Override
    Table setColumn1(final int column,
                     final List<CharSequence> text) {
        return text.equals(this.column(column)) ?
                this :
                this.replaceColumn(column, text);
    }

    private Table replaceColumn(final int column,
                                final List<CharSequence> text) {
        final NavigableMap<TableCellCoordinates, CharSequence> table = map();

        int maximumRow = 0;

        for (final Entry<TableCellCoordinates, CharSequence> cellAndText : this.table.entrySet()) {
            final TableCellCoordinates cellCoordinates = cellAndText.getKey();
            if (cellCoordinates.column != column) {
                table.put(cellCoordinates, cellAndText.getValue());
                maximumRow = Math.max(maximumRow, cellCoordinates.row + 1);
            }
        }
        setColumn(column, text, table);

        return table.isEmpty() ?
                empty() :
                with(table,
                        table.lastEntry().getKey().column + 1,
                        Math.max(maximumRow, text.size()));
    }

    @Override
    public int maxColumn() {
        return this.maxColumn;
    }

    private final int maxColumn;

    // row..............................................................................................................

    @Override
    List<CharSequence> row0(final int row) {
        return TableNotEmptyListRow.with(row, this);
    }

    @Override
    Table setRow1(final int row, final List<CharSequence> text) {
        return text.equals(this.row(row)) ?
                this :
                this.replaceRow(row, text);
    }

    private Table replaceRow(final int row,
                             final List<CharSequence> text) {
        final NavigableMap<TableCellCoordinates, CharSequence> table = map();

        int maximumRow = text.isEmpty() ?
                0 :
                row + 1;

        for (final Entry<TableCellCoordinates, CharSequence> cellAndText : this.table.entrySet()) {
            final TableCellCoordinates cellCoordinates = cellAndText.getKey();
            if (cellCoordinates.row != row) {
                table.put(cellCoordinates, cellAndText.getValue());
                maximumRow = Math.max(maximumRow, cellCoordinates.row + 1);
            }
        }
        setRow(row, text, table);

        return table.isEmpty() ?
                empty() :
                with(table,
                        table.lastEntry().getKey().column + 1,
                        maximumRow);
    }

    @Override
    public int maxRow() {
        return this.maxRow;
    }

    private final int maxRow;

    // map..............................................................................................................

    private static NavigableMap<TableCellCoordinates, CharSequence> map() {
        return Maps.navigable();
    }

    /**
     * A {@link NavigableMap} that is used to hold all cells.
     */
    final NavigableMap<TableCellCoordinates, CharSequence> table;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.table.keySet().hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof TableNotEmpty && this.equals0((TableNotEmpty) other);
    }

    private boolean equals0(final TableNotEmpty other) {
        return this.equals1(other.table);
    }

    // compare values on content so any CharSequence regardless of type with the same chars is equals
    private boolean equals1(final NavigableMap<TableCellCoordinates, CharSequence> other) {
        boolean equals = false;

        final NavigableMap<TableCellCoordinates, CharSequence> table = this.table;

        final int size = this.table.size();
        if (size == other.size()) {
            final Iterator<Entry<TableCellCoordinates, CharSequence>> iterator = other.entrySet().iterator();
            for (final Entry<TableCellCoordinates, CharSequence> coordAndText : table.entrySet()) {
                final Entry<TableCellCoordinates, CharSequence> otherCoordAndText = iterator.next();
                equals = coordAndText.getKey().equals(otherCoordAndText.getKey()) &&
                        CharSequences.equals(coordAndText.getValue(), otherCoordAndText.getValue());
                if (false == equals) {
                    break;
                }
            }
        }

        return equals;
    }

    @Override
    public String toString() {
        return this.table.toString();
    }
}
