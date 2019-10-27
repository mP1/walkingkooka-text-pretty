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

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * A {@link BiFunction} that adds padding {@link CharSequence} so the column text is aligned on a character.
 */
final class CharSequenceBiFunctionAlignCharacter extends CharSequenceBiFunctionAlign {

    /**
     * Factory that creates a new {@link CharSequenceBiFunctionAlignCharacter}.
     */
    final static CharSequenceBiFunctionAlignCharacter with(final CharPredicate chars,
                                                           final int column) {
        Objects.requireNonNull(chars, "chars");
        if(column < 0) {
            throw new IllegalArgumentException("Column " + column + " < 0");
        }

        return new CharSequenceBiFunctionAlignCharacter(chars, column);
    }

    /**
     * Private ctor
     */
    private CharSequenceBiFunctionAlignCharacter(final CharPredicate chars,
                                                 final int column) {
        super();
        this.chars = chars;
        this.column = column;
    }

    @Override
    CharSequence notEmpty(final CharSequence text,
                          final int width) {
        this.checkWidth(width);

        CharSequence aligned = text;

        final int textLength = text.length();
        final int stop = Math.min(this.column, textLength);
        for (int i = 0; i < stop; i++) {
            if (this.chars.test(text.charAt(i))) {
                if(i > 0) {
                    int leftPadding = this.column - i;
                    final int right = (leftPadding + textLength) - width;
                    if(right > 0) {
                        leftPadding -= right;
                        if(leftPadding <= 0) {
                            break;
                        }
                    }

                    aligned = CharSequences.concat(
                            CharSequences.repeating(' ', leftPadding),
                            text);
                }
                break;
            }
        }

        return aligned;
    }

    private void checkWidth(final int width) {
        final int column  = this.column;
        if(column >= width) {
            throw new IllegalArgumentException("Column " + column + " >= width " + width);
        }
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.chars, this.column);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || (other instanceof CharSequenceBiFunctionAlignCharacter && this.equals0((CharSequenceBiFunctionAlignCharacter)other));
    }

    private boolean equals0(final CharSequenceBiFunctionAlignCharacter other) {
        return this.chars.equals(other.chars) &&
                this.column == other.column;
    }

    @Override
    public String toString() {
        return this.chars + " at " + this.column;
    }

    private final CharPredicate chars;
    private final int column;
}
