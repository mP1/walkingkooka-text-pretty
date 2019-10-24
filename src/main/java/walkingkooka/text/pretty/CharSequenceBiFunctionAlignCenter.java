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

import java.util.function.BiFunction;

/**
 * A {@link BiFunction} that adds padding {@link CharSequence} so it is centered.
 */
final class CharSequenceBiFunctionAlignCenter extends CharSequenceBiFunctionAlign {

    /**
     * Singleton
     */
    final static CharSequenceBiFunctionAlignCenter INSTANCE = new CharSequenceBiFunctionAlignCenter();

    /**
     * Private ctor
     */
    private CharSequenceBiFunctionAlignCenter() {
        super();
    }

    @Override
    CharSequence alignNotEmpty(final CharSequence text,
                               final int width) {
        final int beforeTextWidth = text.length();
        final int left = (width - beforeTextWidth) / 2;
        final int right = width - (beforeTextWidth + left);

        return CharSequences.concat(
                CharSequences.repeating(' ', left),
                CharSequences.concat(
                        text,
                        CharSequences.repeating(' ', right)));
    }

    @Override
    public String toString() {
        return "Center";
    }
}
