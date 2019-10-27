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
 * Base class for all {@link BiFunction} that routes the text after testing the width.
 */
abstract class CharSequenceBiFunction implements BiFunction<CharSequence, Integer, CharSequence> {

    /**
     * Package private ctor to limit sub classing.
     */
    CharSequenceBiFunction() {
        super();
    }

    @Override
    public final CharSequence apply(final CharSequence text,
                                    final Integer width) {
        final int length = text.length();
        return length == 0 ?
                this.empty(width) :
                length <= width ?
                        length == width ?
                                this.full(text, width) :
                                this.notEmpty(text, width) :
                        this.overflowed(text, width);
    }

    abstract CharSequence empty(final int width);

    abstract CharSequence notEmpty(final CharSequence text,
                                   final int width);

    abstract CharSequence full(final CharSequence text,
                               final int width);

    abstract CharSequence overflowed(final CharSequence text,
                                     final int width);

    // Column...........................................................................................................

    /**
     * Helper used by {@link Column#add(BiFunction)} that helps overwrite a function already present in {@link Column#functions}.
     */
    abstract boolean isColumnReplace(final BiFunction<CharSequence, Integer, CharSequence> function);
}
