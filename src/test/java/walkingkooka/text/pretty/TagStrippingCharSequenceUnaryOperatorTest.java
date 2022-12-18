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

public final class TagStrippingCharSequenceUnaryOperatorTest extends TextPrettyTestCase<TagStrippingCharSequenceUnaryOperator>
        implements FunctionTesting<TagStrippingCharSequenceUnaryOperator, CharSequence, CharSequence>,
        ToStringTesting<TagStrippingCharSequenceUnaryOperator> {

    @Test
    public void testEmptyString() {
        this.applyAndCheck2("");
    }

    @Test
    public void testText() {
        this.applyAndCheck2("abc");
    }

    @Test
    public void testUnclosedTag() {
        this.applyAndCheck2("<x", "");
    }

    @Test
    public void testUnclosedTag2() {
        this.applyAndCheck2("<x ", "");
    }

    @Test
    public void testOnlyTag() {
        this.applyAndCheck2("<x>", "");
    }

    @Test
    public void testOnlyTag2() {
        this.applyAndCheck2("<xy>", "");
    }

    @Test
    public void testOnlyTag3() {
        this.applyAndCheck2("<xyz>", "");
    }

    @Test
    public void testOnlyEndTag() {
        this.applyAndCheck2("</x>", "");
    }

    @Test
    public void testOnlyEmptyTag() {
        this.applyAndCheck2("<x/>", "");
    }

    @Test
    public void testOnlyEmptyTag2() {
        this.applyAndCheck2("<x />", "");
    }

    @Test
    public void testOnlyTagWithAttributes() {
        this.applyAndCheck2("<x attribute=123>", "");
    }

    @Test
    public void testOnlyTagWithAttributes2() {
        this.applyAndCheck2("<xy attribute=123>", "");
    }

    @Test
    public void testOnlyTagWithSingleQuotedAttributes() {
        this.applyAndCheck2("<x attribute='123'>", "");
    }

    @Test
    public void testOnlyTagWithDoubleQuotedAttributes() {
        this.applyAndCheck2("<x attribute=\"123\">", "");
    }

    @Test
    public void testAttributeDoubleQuoteEscaping() {
        this.applyAndCheck2("<x attribute=\"1\\\"23\">", "");
    }

    @Test
    public void testAttributeDoubleQuoteEscapingText() {
        this.applyAndCheck2("<x attribute=\"1\\\"23\">z", "z");
    }

    @Test
    public void testAttributeText() {
        this.applyAndCheck2("<x attribute=123>z", "z");
    }

    @Test
    public void testAttributeSingleQuoteText() {
        this.applyAndCheck2("<x attribute='123'>z", "z");
    }

    @Test
    public void testAttributeDoubleQuoteText() {
        this.applyAndCheck2("<x attribute=\"123\">z", "z");
    }

    @Test
    public void testAttributeSingleQuoteEscaping() {
        this.applyAndCheck2("<x attribute='1\\'23'>", "");
    }

    @Test
    public void testAttributeSingleQuoteEscapingText() {
        this.applyAndCheck2("<x attribute='1\\'23'>z", "z");
    }

    @Test
    public void testUnclosedSingleQuotedAttribute() {
        this.applyAndCheck2("<x attribute='123", "");
    }

    @Test
    public void testUnclosedDoubleQuotedAttribute() {
        this.applyAndCheck2("<x attribute=\"123", "");
    }

    @Test
    public void testOnlyTagWithAttributes3() {
        this.applyAndCheck2("<x c=123 d=456>", "");
    }

    @Test
    public void testTextTag() {
        this.applyAndCheck2("a<x>", "a");
    }

    @Test
    public void testTextTagText() {
        this.applyAndCheck2("a<x>b", "a b");
    }

    @Test
    public void testTagText() {
        this.applyAndCheck2("<x>z", "z");
    }

    @Test
    public void testComment() {
        this.applyAndCheck2("<!-- comment -->", "");
    }

    @Test
    public void testComment2() {
        this.applyAndCheck2("<!-- comment\n abc def -->", "");
    }

    @Test
    public void testTextComment() {
        this.applyAndCheck2("a<!-- comment -->", "a");
    }

    @Test
    public void testTextComment2() {
        this.applyAndCheck2("ab<!-- comment -->", "ab");
    }

    @Test
    public void testTextComment3() {
        this.applyAndCheck2("ab c d<!-- comment -->", "ab c d");
    }

    @Test
    public void testCommentText() {
        this.applyAndCheck2("<!-- comment -->x", "x");
    }

    @Test
    public void testCommentText2() {
        this.applyAndCheck2("<!-- comment -->xy", "xy");
    }

    @Test
    public void testCommentText3() {
        this.applyAndCheck2("<!-- comment - -->xy", "xy");
    }


    @Test
    public void testTextCommentText() {
        this.applyAndCheck2("a<!-- comment -->b", "a b");
    }

    @Test
    public void testTextTagTextCommentText() {
        this.applyAndCheck2("a<x>b<!-- comment -->c", "a b c");
    }

    @Test
    public void testTextTagTextCommentText2() {
        this.applyAndCheck2("a <x x=y>bc <!-- comment -->defgh ijk lmno", "a bc defgh ijk lmno");
    }

    @Test
    public void testOpenCloseTag() {
        this.applyAndCheck2("<a>b</a>", "b");
    }

    @Test
    public void testOpenCloseTag2() {
        this.applyAndCheck2("<a>text</a>", "text");
    }

    @Test
    public void testBoldText() {
        this.applyAndCheck2("<b>text</b>", "**text**");
    }

    @Test
    public void testBoldText2() {
        this.applyAndCheck2("<B>text</B>", "**text**");
    }

    @Test
    public void testItalicsText() {
        this.applyAndCheck2("<i>text</i>", "*text*");
    }

    @Test
    public void testItalicsText2() {
        this.applyAndCheck2("<I>text</I>", "*text*");
    }

    @Test
    public void testUnderlinedText() {
        this.applyAndCheck2("<u>text</u>", "_text_");
    }

    @Test
    public void testUnderlinedText2() {
        this.applyAndCheck2("<U>text</U>", "_text_");
    }

    private void applyAndCheck2(final CharSequence chars) {
        this.applyAndCheck2(chars, chars);
    }

    private void applyAndCheck2(final CharSequence chars,
                                final CharSequence expected) {
        this.checkEquals(expected.toString(), TagStrippingCharSequenceUnaryOperator.INSTANCE.apply(chars).toString());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(TagStrippingCharSequenceUnaryOperator.INSTANCE, "TagStripping");
    }

    @Override
    public TagStrippingCharSequenceUnaryOperator createFunction() {
        return TagStrippingCharSequenceUnaryOperator.INSTANCE;
    }

    @Override
    public Class<TagStrippingCharSequenceUnaryOperator> type() {
        return TagStrippingCharSequenceUnaryOperator.class;
    }

    @Override
    public String typeNameSuffix() {
        return CharSequence.class.getSimpleName() + UnaryOperator.class.getSimpleName();
    }
}
