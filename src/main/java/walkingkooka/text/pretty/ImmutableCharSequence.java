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

/**
 * An immutable {@link CharSequence} wrapper around a {@link StringBuilder}
 */
final class ImmutableCharSequence implements CharSequence {

    static ImmutableCharSequence with(final CharSequence chars) {
        return new ImmutableCharSequence(chars);
    }

    private ImmutableCharSequence(final CharSequence chars) {
        super();
        this.chars = chars;
    }

    @Override
    public char charAt(final int index) {
        return this.chars.charAt(index);
    }

    @Override
    public int length() {
        return this.chars.length();
    }

    @Override
    public CharSequence subSequence(final int start, final int end) {
        return 0 == start && this.length() == end ?
                this :
                new ImmutableCharSequence(this.chars.subSequence(start, end));
    }

    private final CharSequence chars;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return CharSequences.hash(this.chars);
    }

    public boolean equals(final Object other) {
        return this == other ||
                (other instanceof CharSequence && CharSequences.equals(this.chars, (CharSequence) other));

    }

    @Override
    public String toString() {
        return this.chars.toString();
    }
}
