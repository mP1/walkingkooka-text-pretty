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
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * A {@link Function} that returns another {@link CharSequence} removing multiple or redundant whitespace, carriage
 * return and new lines.
 */
final class WhitespaceNormalizingCharSequenceUnaryOperator implements UnaryOperator<CharSequence> {
    /**
     * Singleton
     */
    final static WhitespaceNormalizingCharSequenceUnaryOperator INSTANCE = new WhitespaceNormalizingCharSequenceUnaryOperator();

    private WhitespaceNormalizingCharSequenceUnaryOperator() {
        super();
    }

    @Override
    public CharSequence apply(final CharSequence chars) {
        Objects.requireNonNull(chars, "chars");

        return chars.length() == 0 ?
                chars :
                clean(chars);
    }

    private CharSequence clean(final CharSequence chars) {
        final int length = chars.length();

        final StringBuilder buffer = new StringBuilder();
        buffer.setLength(0);
        buffer.ensureCapacity(length);

        boolean wasWhitespace = false;

        for (int i = 0; i < length; i++) {
            final char c = chars.charAt(i);
            if (Character.isWhitespace(c)) {
                if (wasWhitespace) {
                    continue;
                }

                buffer.append(' ');
                wasWhitespace = true;
                continue;
            }

            buffer.append(c);
            wasWhitespace = false;
        }

        return ImmutableCharSequence.with(buffer);
    }

    @Override
    public String toString() {
        return "WhitespaceNormalizing";
    }
}
