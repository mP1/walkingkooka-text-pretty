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
 * A {@link BiFunction} that adds padding {@link CharSequence} so it is right aligned. If the text is too wide
 * an exception will be thrown.
 */
final class CharSequenceBiFunctionAlignRight extends CharSequenceBiFunctionAlign {

    /**
     * Singleton
     */
    final static CharSequenceBiFunctionAlignRight INSTANCE = new CharSequenceBiFunctionAlignRight();

    /**
     * Private ctor
     */
    private CharSequenceBiFunctionAlignRight() {
        super();
    }

    @Override
    CharSequence notEmpty(final CharSequence chars,
                          final int width) {
        return CharSequences.padLeft(chars,
                width,
                ' ');
    }

    @Override
    public String toString() {
        return "Right";
    }
}
