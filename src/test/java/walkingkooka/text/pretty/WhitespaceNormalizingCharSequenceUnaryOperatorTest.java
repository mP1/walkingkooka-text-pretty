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
import walkingkooka.util.FunctionTesting;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WhitespaceNormalizingCharSequenceUnaryOperatorTest implements FunctionTesting<WhitespaceNormalizingCharSequenceUnaryOperator, CharSequence, CharSequence>,
        ToStringTesting<WhitespaceNormalizingCharSequenceUnaryOperator> {

    @Test
    public void testEmptyString() {
        this.applyAndCheck2("", "");
    }

    @Test
    public void testCarriageReturn() {
        this.applyAndCheck2("\r", " ");
    }

    @Test
    public void testNewLine() {
        this.applyAndCheck2("\n", " ");
    }

    @Test
    public void testCarriageReturnNewLine() {
        this.applyAndCheck2("\r\n", " ");
    }

    @Test
    public void testTab() {
        this.applyAndCheck2("\t", " ");
    }

    @Test
    public void testSequenceOfWhitespaceCharacters() {
        this.applyAndCheck2(" \t\r\n", " ");
    }

    @Test
    public void testWithoutWhitespace() {
        this.applyAndCheck2("123abc");
    }

    @Test
    public void testIncludesSingleWhitespaceSequences() {
        this.applyAndCheck2(" 123 456", " 123 456");
    }

    @Test
    public void testSeparatedCarriageReturn() {
        this.applyAndCheck2("123\r 456", "123 456");
    }

    @Test
    public void testSeparatedNewline() {
        this.applyAndCheck2("123\r 456", "123 456");
    }

    @Test
    public void testSeparatedTab() {
        this.applyAndCheck2("123\t 456", "123 456");
    }

    private void applyAndCheck2(final CharSequence chars) {
        this.applyAndCheck2(chars, chars);
    }

    private void applyAndCheck2(final CharSequence chars,
                                final CharSequence expected) {
        assertEquals(expected.toString(), WhitespaceNormalizingCharSequenceUnaryOperator.INSTANCE.apply(chars).toString());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(WhitespaceNormalizingCharSequenceUnaryOperator.INSTANCE, "WhitespaceNormalizing");
    }

    @Override
    public WhitespaceNormalizingCharSequenceUnaryOperator createFunction() {
        return WhitespaceNormalizingCharSequenceUnaryOperator.INSTANCE;
    }

    @Override
    public Class<WhitespaceNormalizingCharSequenceUnaryOperator> type() {
        return WhitespaceNormalizingCharSequenceUnaryOperator.class;
    }

    @Override
    public String typeNameSuffix() {
        return CharSequence.class.getSimpleName() + UnaryOperator.class.getSimpleName();
    }
}
