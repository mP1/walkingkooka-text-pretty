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

import java.util.List;
import java.util.stream.Collector;

/**
 * A {@link Collector} that adds row starting at the requested starting row.
 */
final class TableCollectorRow extends TableCollector {

    static TableCollectorRow with(final Table table,
                                  final int startRow) {
        if(startRow < 0) {
            throw new IllegalArgumentException("Invalid start row " + startRow + " < 0");
        }
        return new TableCollectorRow(table, startRow);
    }

    private TableCollectorRow(final Table table,
                              final int startRow) {
        super(table, startRow);
    }

    @Override
    void accumulator(final Table table,
                     final List<CharSequence> row) {
        this.table = this.table.setRow(this.next++, row);
    }

    @Override
    String columnOrRow() {
        return "row";
    }
}
