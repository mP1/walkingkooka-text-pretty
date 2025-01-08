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

import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

/**
 * A {@link UnaryOperator} that inserts spaces to advance the position each time a tab character is
 * encountered. The actual inserted space count is returned by the {@link IntUnaryOperator} which is in invoked with the
 * current column and returns the next tab stop column, upon which spaces are inserted. The {@link IntUnaryOperator} tab
 * stop must be greater than or equal to the current position otherwise a {@link IllegalArgumentException} will be thrown.
 */
final class TabExpandingCharSequenceUnaryOperator implements UnaryOperator<CharSequence> {

    /**
     * Creates a new {@link TabExpandingCharSequenceUnaryOperator}
     */
    static TabExpandingCharSequenceUnaryOperator with(final IntUnaryOperator tabStops) {
        Objects.requireNonNull(tabStops, "tabStops");

        return new TabExpandingCharSequenceUnaryOperator(tabStops);
    }

    /**
     * Private constructor
     */
    private TabExpandingCharSequenceUnaryOperator(final IntUnaryOperator tabStops) {
        super();
        this.tabStops = tabStops;
    }

    /**
     * Contains the core logic of spotting and expanding tabs into zero or more spaces. When it
     * finishes it updates the {@link #tabStops}.
     */
    @Override
    public CharSequence apply(final CharSequence chars) {
        Objects.requireNonNull(chars, "chars");

        int column = 0;
        StringBuilder out = null;

        final int length = chars.length();
        int first = 0;

        for (int i = 0; i < length; i++) {
            final char c = chars.charAt(i);
            switch (c) {
                case '\t':
                    // print everything before the tab.
                    if (null == out) {
                        out = new StringBuilder();
                    }
                    out.append(chars.subSequence(first, i));
                    final int next = this.tabStop(column);

                    insertSpaces(out, next - column);

                    first = i + 1;
                    column = next;
                    break;
                case '\r':
                case '\n':
                    column = 0;
                    break;
                default:
                    column++;
                    break;
            }
        }

        if (first > 0 && first < length) {
            out.append(chars, first, length);
        }

        return null != out ?
                ImmutableCharSequence.with(out) :
                chars;
    }


    /**
     * Called whenever a tab character is found and retrieves the number of characters to be
     * inserted performing numerous checks.
     */
    private int tabStop(final int column) {
        final int next = this.tabStops.applyAsInt(column);
        if (next < column) {
            throw new IllegalArgumentException("Next tab stop " + next + " < " + column);
        }
        return next;
    }

    /**
     * Inserts the requested number of spaces
     */
    private static void insertSpaces(final StringBuilder text,
                                     final int count) {
        text.append(CharSequences.repeating(' ', count));
    }

    /**
     * Takes the current column position and returns the desired tab stop. This value is used to
     * insert spaces.
     */
    private final IntUnaryOperator tabStops;

    @Override
    public String toString() {
        return "Tab expanding";
    }
}
