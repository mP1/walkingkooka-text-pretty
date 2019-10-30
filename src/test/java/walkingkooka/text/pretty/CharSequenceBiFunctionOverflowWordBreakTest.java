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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CharSequenceBiFunctionOverflowWordBreakTest extends CharSequenceBiFunctionOverflowTestCase<CharSequenceBiFunctionOverflowWordBreak> {

    @Test
    public void testOverflow() {
        this.applyAndCheck3("ab cde", 5, "ab", "cde");
    }

    @Test
    public void testOverflowExtraWhitespace() {
        this.applyAndCheck3("ab  cde", 5, "ab", "cde");
    }

    @Test
    public void testOverflow2() {
        this.applyAndCheck3("ab c de", 5, "ab c", "de");
    }

    @Test
    public void testOverflow3() {
        this.applyAndCheck3("a bcd e", 5, "a", "bcd e");
    }

    @Test
    public void testOverflow4() {
        this.applyAndCheck3("a bcde", 5, "a", "bcde");
    }

    @Test
    public void testOverflow5() {
        this.applyAndCheck3("ab c de fghi jkl", 5, "ab c", "de", "fghi", "jkl");
    }

    @Test
    public void testOverflow6() {
        this.applyAndCheck3("ab c de fghi jkl m", 5, "ab c", "de", "fghi", "jkl m");
    }

    @Test
    public void testOverflowExtraWhitespace2() {
        this.applyAndCheck3("ab c   de", 5, "ab c", "de");
    }

    @Test
    public void testOverflowLineBreaks() {
        this.applyAndCheck3("abcdefgh", 5, "abcde", "fgh");
    }

    @Test
    public void testOverflowLineBreaks2() {
        this.applyAndCheck3("abcdefghijklm", 5, "abcde", "fghij", "klm");
    }

    private void applyAndCheck3(final String text,
                                final int maxWidth,
                                final String... expected) {
        assertEquals(MultiLineCharSequence.with(Lists.of(expected), LineEnding.NL).toString(),
                this.createBiFunction().apply(text, maxWidth).toString(),
                () -> "apply " + CharSequences.quoteAndEscape(text) + " " + maxWidth);
    }

    @Override
    public CharSequenceBiFunctionOverflowWordBreak createBiFunction() {
        return CharSequenceBiFunctionOverflowWordBreak.INSTANCE;
    }

    @Override
    public Class<CharSequenceBiFunctionOverflowWordBreak> type() {
        return CharSequenceBiFunctionOverflowWordBreak.class;
    }

    @Override
    String overflow() {
        return "WordBreak";
    }
}
