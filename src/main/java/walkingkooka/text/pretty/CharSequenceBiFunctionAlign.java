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

import java.util.function.BiFunction;

/**
 * Base class for all alignment {@link BiFunction} that complains if the text is greater than the width.
 */
abstract class CharSequenceBiFunctionAlign extends CharSequenceBiFunction {

    /**
     * Package private ctor to limit sub classing.
     */
    CharSequenceBiFunctionAlign() {
        super();
    }

    @Override final CharSequence empty(final int width) {
        return "";
    }

    @Override final CharSequence full(final CharSequence text,
                                      final int width) {
        return text;
    }

    @Override final CharSequence overflowed(final CharSequence text,
                                            final int width) {
        throw new IllegalArgumentException("Text length " + text.length() + " > " + width + " while " + this);
    }

    /**
     * Any {@link CharSequenceBiFunctionAlign} replaces any other {@link CharSequenceBiFunctionAlign}.
     */
    final boolean isColumnReplace(final BiFunction<CharSequence, Integer, CharSequence> function) {
        return function instanceof CharSequenceBiFunctionAlign;
    }
}
