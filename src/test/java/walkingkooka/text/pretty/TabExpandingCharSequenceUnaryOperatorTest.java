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
import walkingkooka.util.FunctionTesting;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TabExpandingCharSequenceUnaryOperatorTest extends TextPrettyTestCase<TabExpandingCharSequenceUnaryOperator>
        implements FunctionTesting<TabExpandingCharSequenceUnaryOperator, CharSequence, CharSequence>,
        ToStringTesting<TabExpandingCharSequenceUnaryOperator> {

    @Test
    public void testWithNullTabStopsFails() {
        assertThrows(NullPointerException.class, () -> TabExpandingCharSequenceUnaryOperator.with(null));
    }

    @Test
    public void testEmptyString() {
        this.applyAndCheckSame("");
    }

    @Test
    public void testWithoutTabs() {
        this.applyAndCheckSame("abc");
    }

    @Test
    public void testSpaceIgnored() {
        this.applyAndCheckSame("         ");
    }

    @Test
    public void testSpaceIgnored2() {
        this.applyAndCheckSame("1 2 3 4 5 6 7 8 9 ");
    }

    @Test
    public void testTab() {
        this.applyAndCheck2("01\t56", "01   56");
    }

    @Test
    public void testTab2() {
        this.applyAndCheck2("\t", "     ");
    }

    @Test
    public void testMultipleTabs() {
        this.applyAndCheck2("\t\t", "          ");
    }

    @Test
    public void testMultipleTabs2() {
        this.applyAndCheck2("\t\tabc\tf", "          abc  f");
    }

    @Test
    public void testTabAfterFirstTabStop() {
        this.applyAndCheck2("0123456\t0", "0123456   0");
    }

    @Test
    public void testCrThenTab() {
        this.applyAndCheck2("abc\r012\t56", "abc\r012  56");
    }

    @Test
    public void testCrNlThenTab() {
        this.applyAndCheck2("abc\r\n012\t56", "abc\r\n012  56");
    }

    @Test
    public void testNlThenTab() {
        this.applyAndCheck2("abc\n012\t56", "abc\n012  56");
    }

    private void applyAndCheckSame(final CharSequence chars) {
        assertSame(chars,
                this.createFunction().apply(chars),
                () -> " apply " + CharSequences.quoteAndEscape(chars));
    }

    private void applyAndCheck2(final CharSequence chars,
                                final CharSequence expected) {
        assertEquals(expected.toString(),
                this.createFunction().apply(chars).toString(),
                () -> " apply " + CharSequences.quoteAndEscape(chars) + " expected " + CharSequences.quoteAndEscape(expected));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createFunction(), "Tab expanding");
    }

    @Override
    public TabExpandingCharSequenceUnaryOperator createFunction() {
        return TabExpandingCharSequenceUnaryOperator.with(i -> (i / 5 * 5 + 5));
    }

    @Override
    public Class<TabExpandingCharSequenceUnaryOperator> type() {
        return TabExpandingCharSequenceUnaryOperator.class;
    }

    @Override
    public String typeNameSuffix() {
        return CharSequence.class.getSimpleName() + UnaryOperator.class.getSimpleName();
    }
}
