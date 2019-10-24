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
import walkingkooka.text.CharSequences;
import walkingkooka.util.BiFunctionTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RightAlignmentCharSequenceBiFunctionTest extends TextPrettyTestCase<RightAlignmentCharSequenceBiFunction>
        implements BiFunctionTesting<RightAlignmentCharSequenceBiFunction, CharSequence, Integer, CharSequence>,
        ToStringTesting<RightAlignmentCharSequenceBiFunction> {

    @Test
    public void testApplyEmpty() {
        this.applyAndCheck2("", 10, CharSequences.repeating(' ', 10));
    }

    @Test
    public void testApplyNotEmpty() {
        this.applyAndCheck2("abc123", 10, "    abc123");
    }

    @Test
    public void testApplyFullWidth() {
        this.applyAndCheck2("abc123", 6, "abc123");
    }

    private void applyAndCheck2(final CharSequence text,
                                final int width,
                                final CharSequence expected) {
        assertEquals(expected.toString(),
                RightAlignmentCharSequenceBiFunction.INSTANCE.apply(text, width).toString(),
                () -> " apply " + CharSequences.quoteAndEscape(text) + " width " + width);
    }

    @Test
    public void testApplyTextGreaterThanWidthFails() {
        assertThrows(IllegalArgumentException.class, () -> RightAlignmentCharSequenceBiFunction.INSTANCE.apply("abc123", 5));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(RightAlignmentCharSequenceBiFunction.INSTANCE, "RightAlignment");
    }

    @Override
    public RightAlignmentCharSequenceBiFunction createBiFunction() {
        return RightAlignmentCharSequenceBiFunction.INSTANCE;
    }

    @Override
    public Class<RightAlignmentCharSequenceBiFunction> type() {
        return RightAlignmentCharSequenceBiFunction.class;
    }
}
