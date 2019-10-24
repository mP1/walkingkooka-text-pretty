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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertEquals(MultiLineCharSequence.with(chars.lines, eol),
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
        assertEquals(MultiLineCharSequence.with(Lists.of(lines), lineEnding),
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
        assertEquals(maxWidth, chars.maxWidth(), () -> chars + " maxWidth");
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
        assertEquals(expected.toString(), chars.line(lineNumber), () -> chars + " line " + lineNumber);
    }

    // setLine.............................................................................................................

    @Test
    public void testSetLineNegativeLineNumberFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createCharSequence().setLine(-1, DIFFERENT));
    }

    @Test
    public void testSetLineInvalidLineNumberFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createCharSequence().setLine(this.lines().size(), DIFFERENT));
    }

    @Test
    public void testSetLineWithNullFails() {
        assertThrows(NullPointerException.class, () -> this.createCharSequence().setLine(0, null));
    }

    @Test
    public void testSetLineIncludesCrFails() {
        this.setLineFails("line\r");
    }

    @Test
    public void testSetLineIncludesCrNlFails() {
        this.setLineFails("line\r\n");
    }

    @Test
    public void testSetLineIncludesNlFails() {
        this.setLineFails("line\n");
    }

    private void setLineFails(final String line) {
        assertThrows(IllegalArgumentException.class, () -> this.createCharSequence().setLine(0, line));
    }

    @Test
    public void testSetLineSame() {
        this.setLineAndCheckSame(0, LINE1);
    }

    @Test
    public void testSetLineSame2() {
        this.setLineAndCheckSame(1, LINE2);
    }

    @Test
    public void testSetLineSame3() {
        this.setLineAndCheckSame(2, LINE3);
    }

    private void setLineAndCheckSame(final int lineNumber, final String line) {
        final MultiLineCharSequence chars = this.createCharSequence();
        assertSame(chars, chars.setLine(lineNumber, line), () -> chars + " setLine " + lineNumber + " line " + CharSequences.quoteIfChars(line));
    }

    @Test
    public void testSetLineDifferentFirstLine() {
        this.setLineDifferentAndCheck(0);
    }

    @Test
    public void testSetLineDifferentMiddleLine() {
        this.setLineDifferentAndCheck(1);
    }

    @Test
    public void testSetLineDifferentLastLine() {
        this.setLineDifferentAndCheck(2);
    }

    private void setLineDifferentAndCheck(final int lineNumber) {
        final MultiLineCharSequence chars = this.createCharSequence();
        final MultiLineCharSequence different = chars.setLine(lineNumber, DIFFERENT);
        final List<CharSequence> expected = Lists.array();
        expected.addAll(this.lines());
        expected.set(lineNumber, DIFFERENT);

        this.checkEquals(different, MultiLineCharSequence.with(expected, EOL));
    }

    // subSequence.......................................................................................................

    @Override
    public void testNegativeSubSequenceFromIndexFails() {
    }

    @Override
    public void testNegativeSubSequenceToFails() {
    }

    @Override
    public void testEmptySubSequence() {
    }

    @Override
    public void testEmptySubSequence2() {
    }

    @Override
    public void testInvalidSubSequenceFromIndexFails() {
    }

    @Override
    public void testSubSequenceWithSameFromAndToReturnsThis() {
    }

    @Override
    public void testSubSequenceFromAfterToFails() {
    }

    @Override
    public void testSubsequenceInvalidToIndexFails() {
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

    @Override
    public void testToStringCached() {
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
