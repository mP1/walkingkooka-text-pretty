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
abstract class AlignmentCharSequenceBiFunction implements BiFunction<CharSequence, Integer, CharSequence> {

    /**
     * Package private ctor to limit sub classing.
     */
    AlignmentCharSequenceBiFunction() {
        super();
    }

    @Override
    public final CharSequence apply(final CharSequence chars,
                                    final Integer width) {
        final int length = chars.length();
        if (chars.length() > width) {
            throw new IllegalArgumentException("Text length " + length + " > " + width);
        }
        return length == 0 ?
                "" :
                alignNotEmpty(chars, width);
    }

    abstract CharSequence alignNotEmpty(final CharSequence text,
                                        final int width);
}
