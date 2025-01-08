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
import walkingkooka.text.LineEnding;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;

/**
 * A {@link Function} that accepts the columns of a row and combines them into a single line adding right padding to each
 * column. Columns may have line breaks.
 *
 * <br>
 * Support is included for columns of varying height, and lines are right trimmed so they wont have any extra trailing
 * space.
 */
final class RowColumnsToLineCharSequenceFunction implements Function<List<CharSequence>, CharSequence> {

    static RowColumnsToLineCharSequenceFunction with(final IntUnaryOperator rightPaddings,
                                                     final LineEnding lineEnding) {
        Objects.requireNonNull(rightPaddings, "rightPaddings");
        Objects.requireNonNull(lineEnding, "lineEnding");

        return new RowColumnsToLineCharSequenceFunction(rightPaddings, lineEnding);
    }

    private RowColumnsToLineCharSequenceFunction(final IntUnaryOperator rightPaddings,
                                                 final LineEnding lineEnding) {
        super();
        this.rightPaddings = rightPaddings;
        this.lineEnding = lineEnding;
    }

    @Override
    public CharSequence apply(final List<CharSequence> columns) {
        Objects.requireNonNull(columns, "columns");

        // break up each column's CharSequence into individual lines,.
        final List<MultiLineCharSequence> columnToLines = Lists.array();

        for (final CharSequence column : columns) {
            columnToLines.add(MultiLineCharSequence.parse(column, this.lineEnding));
        }

        // join line by line from each column..........
        final int maxRows = columnToLines.stream()
                .mapToInt(MultiLineCharSequence::lineCount)
                .max()
                .orElse(0);

        final int columnCount = columnToLines.size();
        final int lastColumn = columnCount - 1;

        final StringBuilder all = new StringBuilder();

        for (int r = 0; r < maxRows; r++) {

            final StringBuilder text = new StringBuilder();

            for (int c = 0; c < columnCount; c++) {
                final MultiLineCharSequence rows = columnToLines.get(c);
                final CharSequence columnText = r < rows.lineCount() ?
                        rows.line(r) :
                        "";
                // only add padding to columns that are not the last.
                if (c < lastColumn) {
                    final CharSequence padded = CharSequences.padRight(columnText,
                            rows.maxWidth(),
                            ' ');
                    text.append(padded);
                    text.append(this.pad(c));
                } else {
                    text.append(columnText);
                }
            }

            all.append(CharSequences.trimRight(text))
                    .append(this.lineEnding);
        }

        return ImmutableCharSequence.with(all);
    }

    private CharSequence pad(final int column) {
        return CharSequences.repeating(' ', this.rightPaddings.applyAsInt(column));
    }

    /**
     * Accepts the column number and returns the right padding aka space count that should appear after that column's text.
     */
    private final IntUnaryOperator rightPaddings;

    /**
     * The {@link LineEnding} that is added after each row of text except the last.
     */
    private final LineEnding lineEnding;

    @Override
    public String toString() {
        return "RowColumn(s)->Line";
    }
}
