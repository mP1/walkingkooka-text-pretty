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

public final class CharSequenceBiFunctionOverflowMaxWidthBreakTest extends CharSequenceBiFunctionOverflowTestCase<CharSequenceBiFunctionOverflowMaxWidthBreak> {

    @Test
    public void testOverflow() {
        this.applyAndCheck3("abcdef", 5, "abcde", "f");
    }

    @Test
    public void testOverflow2() {
        this.applyAndCheck3("abcdef", 3, "abc", "def");
    }

    @Test
    public void testOverflow3() {
        this.applyAndCheck3("abcdefghi", 3, "abc", "def", "ghi");
    }

    @Test
    public void testOverflow4() {
        this.applyAndCheck3("abcdefghij", 3, "abc", "def", "ghi", "j");
    }

    private void applyAndCheck3(final String text,
                                final int maxWidth,
                                final String... expected) {
        this.checkEquals(MultiLineCharSequence.with(Lists.of(expected), LineEnding.NL).toString(),
                this.createBiFunction().apply(text, maxWidth).toString(),
                () -> "apply " + CharSequences.quoteAndEscape(text) + " " + maxWidth);
    }

    @Override
    public CharSequenceBiFunctionOverflowMaxWidthBreak createBiFunction() {
        return CharSequenceBiFunctionOverflowMaxWidthBreak.INSTANCE;
    }

    @Override
    public Class<CharSequenceBiFunctionOverflowMaxWidthBreak> type() {
        return CharSequenceBiFunctionOverflowMaxWidthBreak.class;
    }

    @Override
    String overflow() {
        return "MaxWidthBreak";
    }
}
