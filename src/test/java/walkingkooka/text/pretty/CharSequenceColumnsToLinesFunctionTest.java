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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.LineEnding;
import walkingkooka.util.FunctionTesting;

import java.util.List;
import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CharSequenceColumnsToLinesFunctionTest extends TextPrettyTestCase<CharSequenceColumnsToLinesFunction>
        implements FunctionTesting<CharSequenceColumnsToLinesFunction, List<CharSequence>, CharSequence>,
        ToStringTesting<CharSequenceColumnsToLinesFunction> {

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    @Test
    public void testWithNullRightPaddingsFails() {
        assertThrows(NullPointerException.class, () -> CharSequenceColumnsToLinesFunction.with(null, LINE_ENDING));
    }

    @Test
    public void testWithNullLineEndingFails() {
        assertThrows(NullPointerException.class, () -> CharSequenceColumnsToLinesFunction.with(this.rightPaddings(), null));
    }

    @Test
    public void testApplyNullFails() {
        assertThrows(NullPointerException.class, () -> this.createFunction().apply(null));
    }

    @Test
    public void testEmptyColumns() {
        this.applyAndCheck2(Lists.of("", "", ""), "");
    }

    @Test
    public void testOneLineColumns() {
        this.applyAndCheck2(Lists.of("a1", "b2", "c3"), "a1 b2  c3\n");
    }

    @Test
    public void testOneLineColumnsIncludesEmptyColumn() {
        this.applyAndCheck2(Lists.of("", "b2", "c3"), " b2  c3\n");
    }

    @Test
    public void testOneLineColumnsTrailingCr() {
        this.applyAndCheck2(Lists.of("a1\r", "b2", "c3"), "a1 b2  c3\n");
    }

    @Test
    public void testOneLineColumnsTrailingNl() {
        this.applyAndCheck2(Lists.of("a1\n", "b2", "c3"), "a1 b2  c3\n");
    }

    @Test
    public void testOneLineColumnsTrailingCrNl() {
        this.applyAndCheck2(Lists.of("a1\r\n", "b2", "c3"), "a1 b2  c3\n");
    }

    @Test
    public void testMultiLineColumnsNl() {
        this.applyAndCheck2(Lists.of("a1", "b2", "c3\nd4"),
                "a1 b2  c3\n" +
                        "       d4\n");
    }

    @Test
    public void testMultiLineColumnsCr() {
        this.applyAndCheck2(Lists.of("a1", "b2", "c3\rd4"),
                "a1 b2  c3\n" +
                        "       d4\n");
    }

    @Test
    public void testMultiLineColumnsCrNl() {
        this.applyAndCheck2(Lists.of("a1", "b2", "c3\r\nd4"),
                "a1 b2  c3\n" +
                        "       d4\n");
    }

    @Test
    public void testMultiLineColumns2() {
        this.applyAndCheck2(Lists.of("a1\n\na13", "b21\nb22", "c3\nd4"),
                "a1  b21  c3\n" +
                        "    b22  d4\n" +
                        "a13\n");
    }

    @Test
    public void testMultiLineColumnsPadded() {
        this.applyAndCheck2(Lists.of("a11\na1", "b2", "c3\nd4"),
                "a11 b2  c3\n" +
                        "a1      d4\n");
    }

    @Test
    public void testMultiLineLastColumnDifferentWidths() {
        this.applyAndCheck2(Lists.of("a1", "b2", "c33\nd4"),
                "a1 b2  c33\n" +
                        "       d4\n");
    }

    @Test
    public void testMultiLineLastColumnDifferentWidths2() {
        this.applyAndCheck2(Lists.of("a1", "b2", "c3\nd4"),
                "a1 b2  c3\n" +
                        "       d4\n");
    }

    @Test
    public void testMultiLineColumns() {
        this.applyAndCheck2(Lists.of("a123\n\na23", "b234\rb3\rb456", "c3"),
                "a123 b234  c3\n" +
                        "     b3\n" +
                        "a23  b456\n");
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createFunction(), "CharSequenceColumnsToLine");
    }

    // helpers..........................................................................................................

    @Override
    public CharSequenceColumnsToLinesFunction createFunction() {
        return CharSequenceColumnsToLinesFunction.with(this.rightPaddings(), LINE_ENDING);
    }

    private IntUnaryOperator rightPaddings() {
        return (c) -> c + 1;
    }

    private void applyAndCheck2(final List<CharSequence> columns, final CharSequence text) {
        assertEquals(text.toString(),
                this.createFunction().apply(columns).toString(),
                () -> columns.toString());
    }

    @Override
    public Class<CharSequenceColumnsToLinesFunction> type() {
        return CharSequenceColumnsToLinesFunction.class;
    }
}
