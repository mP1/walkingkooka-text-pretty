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

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.util.BiFunctionTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class CharSequenceBiFunctionTestCase<A extends CharSequenceBiFunction> extends TextPrettyTestCase<A>
        implements BiFunctionTesting<A, CharSequence, Integer, CharSequence>,
        ToStringTesting<A>,
        TypeNameTesting<A> {

    CharSequenceBiFunctionTestCase() {
        super();
    }

    final void applyAndCheck2(final CharSequence text,
                              final int width,
                              final CharSequence expected) {
        assertEquals(expected.toString(),
                this.createBiFunction().apply(text, width).toString(),
                () -> " apply " + CharSequences.quoteAndEscape(text) + " width " + width);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return CharSequenceBiFunction.class.getSimpleName();
    }
}