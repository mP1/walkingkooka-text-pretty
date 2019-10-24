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

/**
 * Truncates text that is greater than the given width.
 */
final class CharSequenceBiFunctionOverflowTruncate extends CharSequenceBiFunctionOverflow {

    /**
     * Singleton
     */
    final static CharSequenceBiFunctionOverflowTruncate INSTANCE = new CharSequenceBiFunctionOverflowTruncate();

    private CharSequenceBiFunctionOverflowTruncate() {
        super();
    }

    @Override
    CharSequence overflowed(final CharSequence text,
                            final int width) {
        return text.subSequence(0, width);
    }

    @Override
    public String toString() {
        return "Truncate";
    }
}
