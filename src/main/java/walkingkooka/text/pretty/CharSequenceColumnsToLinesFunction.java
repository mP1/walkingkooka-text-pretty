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
 * A {@link Function} that accepts a {@link List} of columns text which may include line breaks of any kind,
 * and combines each column line into the final text. All columns text have extra padding added so they are the same
 * width, and right-padding is added for each column except the last.
 * <br>
 * Support is included for columns of varying height, and lines are right trimmed so they wont have any extra trailing
 * space.
 */
final class CharSequenceColumnsToLinesFunction implements Function<List<CharSequence>, CharSequence> {

    static CharSequenceColumnsToLinesFunction with(final IntUnaryOperator rightPaddings,
                                                   final LineEnding lineEnding) {
        Objects.requireNonNull(rightPaddings, "rightPaddings");
        Objects.requireNonNull(lineEnding, "lineEnding");

        return new CharSequenceColumnsToLinesFunction(rightPaddings, lineEnding);
    }

    private CharSequenceColumnsToLinesFunction(final IntUnaryOperator rightPaddings,
                                               final LineEnding lineEnding) {
        super();
        this.rightPaddings = rightPaddings;
        this.lineEnding = lineEnding;
    }

    @Override
    public CharSequence apply(final List<CharSequence> columns) {
        Objects.requireNonNull(columns, "columns");

        // break up each column's CharSequence into individual lines,.
        final List<List<CharSequence>> columnToLines = Lists.array();
        final List<Integer> maxColumnWidths = Lists.array();

        for(final CharSequence column : columns) {
            final List<CharSequence> lines = Lists.array();
            final int length = column.length();
            char previous = 0;
            int start = 0;
            int i = 0;

            for(;;) {
                if(length == i) {
                    if(length != start) {
                        lines.add(column.subSequence(start, length));
                    }
                    break;
                }
                final char c = column.charAt(i);
                switch(c){
                    case '\n': // NL
                        if('\r' != previous) {
                            lines.add(column.subSequence(start, i)); // without the NL
                        } // else was CRNL

                        i++;
                        start = i; // after the NL

                        previous = c;
                        break;
                    case '\r': // CR
                        lines.add(column.subSequence(start, i)); // without the CR
                        i++;
                        start = i; // after the CR

                        previous = '\r';
                        break;
                    default:
                        previous = c;
                        i++;
                        break;
                }
            }

            columnToLines.add(lines);
            maxColumnWidths.add(lines.stream().mapToInt(CharSequence::length).max().orElse(0));
        }

        // join line by line from each column..........
        final int maxRows = columnToLines.stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        final int columnCount = columnToLines.size();
        final int lastColumn = columnCount -1;

        final StringBuilder all = new StringBuilder();

        for(int r = 0; r < maxRows; r++) {

            final StringBuilder text = new StringBuilder();

            for(int c = 0; c < columnCount; c++) {
                final List<CharSequence> rows = columnToLines.get(c);
                final CharSequence columnText = r < rows.size() ?
                        rows.get(r) :
                        "";
                // only add padding to columns that are not the last.
                if(c < lastColumn) {
                    final CharSequence padded = CharSequences.padRight(columnText,
                            maxColumnWidths.get(c),
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
        return "CharSequenceColumnsToLine";
    }
}
