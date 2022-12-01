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
import walkingkooka.text.CharSequenceTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MultiLineCharSequenceTest extends TextPrettyTestCase<MultiLineCharSequence>
        implements CharSequenceTesting<MultiLineCharSequence> {

    private final static String LINE1 = "1a";
    private final static String LINE2 = "2bb";
    private final static String LINE3 = "3ccc";

    private final static LineEnding EOL = LineEnding.NL;

    private final static String TOSTRING = LINE1 + EOL + LINE2 + EOL + LINE3 + EOL;

    private final static String DIFFERENT = "different";

    // parse............................................................................................................

    @Test
    public void testParseNullTextFails() {
        assertThrows(NullPointerException.class, () -> MultiLineCharSequence.parse(null, EOL));
    }

    @Test
    public void testParseNullLineEndingFails() {
        assertThrows(NullPointerException.class, () -> MultiLineCharSequence.parse("abc", null));
    }

    @Test
    public void testParseMultiLineCharSequenceCr() {
        this.parseAndCheck(this.createCharSequence(), LineEnding.CR);
    }

    @Test
    public void testParseMultiLineCharSequenceCrNl() {
        this.parseAndCheck(this.createCharSequence(), LineEnding.CRNL);
    }

    @Test
    public void testParseMultiLineCharSequenceNl() {
        this.parseAndCheck(this.createCharSequence(), LineEnding.NL);
    }

    private void parseAndCheck(final MultiLineCharSequence chars,
                               final LineEnding eol) {
        this.checkEquals(MultiLineCharSequence.with(chars.lines, eol),
                MultiLineCharSequence.parse(chars, eol),
                () -> "parse " + CharSequences.quoteAndEscape(chars) + " lineEnding " + CharSequences.quoteAndEscape(eol));
    }

    @Test
    public void testParseEmpty() {
        this.parseAndCheck("", LineEnding.CRNL);
    }

    @Test
    public void testParseSingleLine() {
        this.parseAndCheck("abc", LineEnding.CRNL, "abc");
    }

    @Test
    public void testParseSingleLineCr() {
        this.parseAndCheck("abc\r", LineEnding.CRNL, "abc");
    }

    @Test
    public void testParseSingleLineCrNl() {
        this.parseAndCheck("abc\r\n", LineEnding.CRNL, "abc");
    }

    @Test
    public void testParseSingleLineNl() {
        this.parseAndCheck("abc\n", LineEnding.CRNL, "abc");
    }

    @Test
    public void testParseMultiLineCr() {
        this.parseAndCheck("a1\rb2", LineEnding.CRNL, "a1", "b2");
    }

    @Test
    public void testParseMultiLineCrNl() {
        this.parseAndCheck("a1\r\nb2", LineEnding.CRNL, "a1", "b2");
    }

    @Test
    public void testParseMultiLineNl() {
        this.parseAndCheck("a1\nb2", LineEnding.CRNL, "a1", "b2");
    }

    @Test
    public void testParseMultiLineIncludesEmpty() {
        this.parseAndCheck("\nb2", LineEnding.CRNL, "", "b2");
    }

    @Test
    public void testParseMultiLineIncludesEmpty2() {
        this.parseAndCheck("a1\n\n", LineEnding.CRNL, "a1", "");
    }

    @Test
    public void testParseMultiLine() {
        this.parseAndCheck("a1\nb2\nc333", LineEnding.CRNL, "a1", "b2", "c333");
    }

    @Test
    public void testParseMultiLine2() {
        this.parseAndCheck("a1\nb2\nc333\rd4444", LineEnding.CRNL, "a1", "b2", "c333", "d4444");
    }

    private void parseAndCheck(final CharSequence text,
                               final LineEnding lineEnding,
                               final CharSequence... lines) {
        this.checkEquals(MultiLineCharSequence.with(Lists.of(lines), lineEnding),
                MultiLineCharSequence.parse(text, lineEnding),
                () -> "parse " + CharSequences.quoteAndEscape(text) + " lineEnding: " + CharSequences.quoteAndEscape(lineEnding));
    }

    // with.............................................................................................................

    @Test
    public void testWithNullLinesFails() {
        assertThrows(NullPointerException.class, () -> MultiLineCharSequence.with(null, EOL));
    }

    @Test
    public void testWithLineIncludesCrFails() {
        assertThrows(IllegalArgumentException.class, () -> MultiLineCharSequence.with(Lists.of("line 1a", "line 1a\r"), EOL));
    }

    @Test
    public void testWithLineIncludesCrNlFails() {
        assertThrows(IllegalArgumentException.class, () -> MultiLineCharSequence.with(Lists.of("line 1a", "line 1a\r\n"), EOL));
    }

    @Test
    public void testWithLineIncludesNlFails() {
        assertThrows(IllegalArgumentException.class, () -> MultiLineCharSequence.with(Lists.of("line 1a", "line 2b\n"), EOL));
    }

    @Test
    public void testWithNullLineEndingFails() {
        assertThrows(NullPointerException.class, () -> MultiLineCharSequence.with(this.lines(), null));
    }

    // charAt...........................................................................................................

    @Test
    public void testAtFirstLineBegin() {
        final int index = 0;
        this.checkCharAt(index, LINE1.charAt(index));
    }

    @Test
    public void testAtFirstLineMiddle() {
        final int index = 1;
        this.checkCharAt(index, LINE1.charAt(index));
    }

    @Test
    public void testAtFirstLineEnd() {
        final int index = LINE1.length() - 1;
        this.checkCharAt(index, LINE1.charAt(index));
    }

    @Test
    public void testAtFirstLineEnding() {
        final int index = 0;
        this.checkCharAt(LINE1.length() + index, EOL.charAt(index));
    }

    @Test
    public void testAtSecondLineBegin() {
        final int index = 1;
        this.checkCharAt((LINE1 + EOL).length() + index, LINE2.charAt(index));
    }

    @Test
    public void testAtSecondLineMiddle() {
        final int index = +1;
        this.checkCharAt((LINE1 + EOL).length() + index, LINE2.charAt(index));
    }

    @Test
    public void testAtSecondLineEnd() {
        final int index = LINE2.length() - 1;
        this.checkCharAt((LINE1 + EOL).length() + index, LINE2.charAt(index));
    }

    @Test
    public void testAtSecondLineEnding() {
        this.checkCharAt((LINE1 + EOL + LINE2).length(), EOL.charAt(0));
    }

    @Test
    public void testAtThirdLineBegin() {
        final int index = 0;
        this.checkCharAt((LINE1 + EOL + LINE2 + EOL).length() + index, LINE3.charAt(index));
    }

    @Test
    public void testAtThirdLineMiddle() {
        final int index = 1;
        this.checkCharAt((LINE1 + EOL + LINE2 + EOL).length() + index, LINE3.charAt(index));
    }

    @Test
    public void testAtThirdLineEnd() {
        final int index = LINE3.length() - 1;
        this.checkCharAt((LINE1 + EOL + LINE2 + EOL).length() + index, LINE3.charAt(index));
    }

    @Test
    public void testAtThirdLineEnding() {
        final int index = 0;
        this.checkCharAt((LINE1 + EOL + LINE2 + EOL + LINE3).length() + index, EOL.charAt(index));
    }

    @Test
    public void testAtThirdLineEndingCrNl() {
        final int index = 1;
        final LineEnding lineEnding = LineEnding.CRNL;

        this.checkCharAt(MultiLineCharSequence.with(Lists.of(LINE1, LINE2, LINE3), lineEnding),
                (LINE1 + lineEnding + LINE2 + lineEnding + LINE3).length() + index,
                lineEnding.charAt(index));
    }

    @Test
    public void testAtAfterEmptyLine() {
        final int index = 0;
        this.checkCharAt(MultiLineCharSequence.with(Lists.of("", LINE2, LINE3), EOL),
                index,
                EOL.charAt(index));
    }

    // maxWidth.........................................................................................................

    @Test
    public void testMaxWidthEmpty() {
        this.maxWidthAndCheck(MultiLineCharSequence.with(Lists.empty(), LineEnding.CRNL), 0);
    }

    @Test
    public void testMaxWidth() {
        this.maxWidthAndCheck(MultiLineCharSequence.with(Lists.of("", "bbb", "c"), LineEnding.CRNL), 3);
    }

    private void maxWidthAndCheck(final MultiLineCharSequence chars,
                                  final int maxWidth) {
        this.checkEquals(maxWidth, chars.maxWidth(), () -> chars + " maxWidth");
    }

    // length...........................................................................................................

    @Test
    public void testEmpty() {
        this.checkLength(MultiLineCharSequence.with(Lists.empty(), EOL), 0);
    }

    @Test
    public void testLength() {
        this.checkLength(TOSTRING.length());
    }

    // line.............................................................................................................

    @Test
    public void testLineFirst() {
        this.lineAndCheck(0, LINE1);
    }

    @Test
    public void testLineMiddle() {
        this.lineAndCheck(0, LINE1);
    }

    @Test
    public void testLineLast() {
        this.lineAndCheck(0, LINE1);
    }

    private void lineAndCheck(final int lineNumber,
                              final CharSequence expected) {
        this.lineAndCheck(this.createCharSequence(), lineNumber, expected);
    }

    private void lineAndCheck(final MultiLineCharSequence chars,
                              final int lineNumber,
                              final CharSequence expected) {
        this.checkEquals(expected.toString(), chars.line(lineNumber), () -> chars + " line " + lineNumber);
    }

    // setText.............................................................................................................

    @Test
    public void testSetTextNegativeLineNumberFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createCharSequence().setText(-1, DIFFERENT));
    }

    @Test
    public void testSetTextInvalidLineNumberFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createCharSequence().setText(this.lines().size(), DIFFERENT));
    }

    @Test
    public void testSetTextWithNullFails() {
        assertThrows(NullPointerException.class, () -> this.createCharSequence().setText(0, null));
    }

    @Test
    public void testSetTextSame() {
        this.setTextAndCheckSame(0, LINE1);
    }

    @Test
    public void testSetTextSame2() {
        this.setTextAndCheckSame(1, LINE2);
    }

    @Test
    public void testSetTextSame3() {
        this.setTextAndCheckSame(2, LINE3);
    }

    @Test
    public void testSetTextSameIgnoresCr() {
        this.setTextAndCheckSame(0, LINE1 + LineEnding.CR);
    }

    @Test
    public void testSetTextSameIgnoresCrLf() {
        this.setTextAndCheckSame(1, LINE2 + LineEnding.CRNL);
    }

    @Test
    public void testSetTextSameIgnoresNl() {
        this.setTextAndCheckSame(2, LINE3 + LineEnding.NL);
    }

    private void setTextAndCheckSame(final int lineNumber, final String line) {
        final MultiLineCharSequence chars = this.createCharSequence();
        assertSame(chars, chars.setText(lineNumber, line), () -> chars + " setText " + lineNumber + " line " + CharSequences.quoteIfChars(line));
    }

    @Test
    public void testSetTextDifferentFirstLine() {
        this.setTextDifferentAndCheck(0);
    }

    @Test
    public void testSetTextDifferentMiddleLine() {
        this.setTextDifferentAndCheck(1);
    }

    @Test
    public void testSetTextDifferentLastLine() {
        this.setTextDifferentAndCheck(2);
    }

    private void setTextDifferentAndCheck(final int lineNumber) {
        final List<CharSequence> expected = Lists.array();
        expected.addAll(this.lines());
        expected.set(lineNumber, DIFFERENT);

        this.setTextDifferentAndCheck(lineNumber, DIFFERENT, expected);
    }

    @Test
    public void testSetTextMultiLineFirstLine() {
        this.setTextDifferentAndCheck(0, "x\ry", "x", "y", LINE2, LINE3);
    }

    @Test
    public void testSetTextMultiLineMiddleLine() {
        this.setTextDifferentAndCheck(1, "x\ry", LINE1, "x", "y", LINE3);
    }

    @Test
    public void testSetTextMultiLineLastLine() {
        this.setTextDifferentAndCheck(2, "x\ry", LINE1, LINE2, "x", "y");
    }

    private void setTextDifferentAndCheck(final int lineNumber,
                                          final CharSequence line,
                                          final CharSequence... expected) {
        this.setTextDifferentAndCheck(lineNumber, line, Lists.of(expected));
    }

    private void setTextDifferentAndCheck(final int lineNumber,
                                          final CharSequence line,
                                          final List<CharSequence> expected) {
        final MultiLineCharSequence chars = this.createCharSequence();
        final MultiLineCharSequence different = chars.setText(lineNumber, line);

        this.checkEquals(different, MultiLineCharSequence.with(expected, EOL));
    }

    // subSequence.......................................................................................................

    @Test
    public void testSubSequenceEmpty() {
        final MultiLineCharSequence chars = this.createCharSequence();
        IntStream.range(0, chars.length())
                .forEach(at -> this.subSequenceAndCheckSame(at, at, ""));
    }

    @Test
    public void testSubSequenceFirstLine() {
        this.subSequenceAndCheckSame(0,
                LINE1.length(),
                LINE1);
    }

    @Test
    public void testSubSequenceFirstEol() {
        this.subSequenceAndCheckSame(LINE1,
                LINE1 + EOL,
                EOL);
    }

    @Test
    public void testSubSequenceSecondLine() {
        this.subSequenceAndCheckSame(LINE1 + EOL,
                LINE1 + EOL + LINE2,
                LINE2);
    }

    @Test
    public void testSubSequenceSecondEol() {
        this.subSequenceAndCheckSame(LINE1 + EOL + LINE2,
                LINE1 + EOL + LINE2 + EOL,
                EOL);
    }

    @Test
    public void testSubSequenceLastLine() {
        this.subSequenceAndCheckSame(LINE1 + EOL + LINE2 + EOL,
                LINE1 + EOL + LINE2 + EOL + LINE3,
                LINE3);
    }

    @Test
    public void testSubSequenceLastEol() {
        this.subSequenceAndCheckSame(LINE1 + EOL + LINE2 + EOL + LINE3,
                LINE1 + EOL + LINE2 + EOL + LINE3 + EOL,
                EOL);
    }

    private void subSequenceAndCheckSame(final CharSequence start,
                                         final CharSequence end,
                                         final CharSequence expected) {
        this.subSequenceAndCheckSame(start.length(),
                end.length(),
                expected);
    }

    private void subSequenceAndCheckSame(final int start,
                                         final int end,
                                         final CharSequence expected) {
        this.subSequenceAndCheckSame(this.createCharSequence(),
                start,
                end,
                expected);
    }

    private void subSequenceAndCheckSame(final MultiLineCharSequence chars,
                                         final int start,
                                         final int end,
                                         final CharSequence expected) {
        assertSame(expected,
                chars.subSequence(start, end),
                () -> CharSequences.quoteAndEscape(chars) + " subSequence " + start + "," + end);
    }

    @Test
    public void testSubSequence() {
        this.checkEquals("1", this.createCharSequence().subSequence(0, 1));
    }

    @Test
    public void testSubSequence2() {
        this.checkEquals("a", this.createCharSequence().subSequence(1, 2));
    }

    @Test
    public void testSubSequence3() {
        this.checkEquals("a" + EOL, this.createCharSequence().subSequence(1, (LINE1 + EOL).length()));
    }

    @Test
    public void testSubSequence4() {
        this.checkEquals("a" + EOL + "2", this.createCharSequence().subSequence(1, (LINE1 + EOL).length() + 1));
    }

    @Test
    public void testSubSequence5() {
        final MultiLineCharSequence chars = this.createCharSequence();

        for (int i = 0; i < chars.length() - 2; i++) {
            final int start = i;
            final int end = i + 2;
            this.checkEquals(TOSTRING.substring(start, end), chars.subSequence(start, end));
        }
    }

    // equals...........................................................................................................

    @Test
    public void testDifferentLines() {
        this.checkNotEquals(MultiLineCharSequence.with(Lists.of(LINE1, LINE2, LINE3, "different"), LineEnding.CR));
    }

    @Test
    public void testDifferentLineEnding() {
        this.checkNotEquals(MultiLineCharSequence.with(this.lines(), LineEnding.CR));
    }

    // toString...........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharSequence(), TOSTRING);
    }

    // CharSequenceTesting..............................................................................................

    @Override
    public MultiLineCharSequence createCharSequence() {
        return MultiLineCharSequence.with(this.lines(), EOL);
    }

    private List<CharSequence> lines() {
        return Lists.of(LINE1, LINE2, LINE3);
    }

    @Override
    public MultiLineCharSequence createObject() {
        return this.createCharSequence();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<MultiLineCharSequence> type() {
        return MultiLineCharSequence.class;
    }
}
