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

import java.util.Objects;

/**
 * The coordinates to a cell within a the private map inside a {@link TableNotEmpty}.
 */
final class TableCellCoordinates implements Comparable<TableCellCoordinates> {

    static TableCellCoordinates with(final int column,
                                     final int row) {
        return new TableCellCoordinates(column, row);
    }

    private TableCellCoordinates(final int column,
                                 final int row) {
        super();
        this.column = column;
        this.row = row;
    }

    final int column;
    final int row;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.column, this.row);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof TableCellCoordinates && this.equals0((TableCellCoordinates)other);
    }

    private boolean equals0(final TableCellCoordinates other) {
        return this.column == other.column && this.row == other.row;
    }

    @Override
    public String toString() {
        return this.column + "," + this.row;
    }

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final TableCellCoordinates other) {
        final int result = this.row - other.row;
        return 0 != result ?
                result :
                this.column - other.column;
    }
}
