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
import walkingkooka.util.FunctionTesting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CsvListCharSequenceFunctionTest extends CsvListCharSequenceFunctionTestCase<CsvListCharSequenceFunction> implements FunctionTesting<CsvListCharSequenceFunction,
        CharSequence,
        List<CharSequence>>,
        ToStringTesting<CsvListCharSequenceFunction> {

    // Function.........................................................................................................

    @Test
    public void testEmpty() {
        this.applyAndCheck2("");
    }

    @Test
    public void testUnescaped() {
        this.applyAndCheck2("a", "a");
    }

    @Test
    public void testUnescaped2() {
        this.applyAndCheck2("ab", "ab");
    }

    @Test
    public void testUnescaped3() {
        this.applyAndCheck2("abc", "abc");
    }

    @Test
    public void testUnescapedClosingDelimiter() {
        this.applyAndCheck2("a;", "a", "");
    }

    @Test
    public void testUnescapedClosingDelimiter2() {
        this.applyAndCheck2("abc;", "abc", "");
    }

    @Test
    public void testUnclosedDoubleQuoteFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createFunction().apply("abc\""));
    }

    @Test
    public void testUnescapedFields() {
        this.applyAndCheck2("a;b", "a", "b");
    }

    @Test
    public void testUnescapedFields2() {
        this.applyAndCheck2("abc;de", "abc", "de");
    }

    @Test
    public void testUnescapedFieldsIncludesEmpty() {
        this.applyAndCheck2("abc;;de", "abc", "", "de");
    }

    @Test
    public void testUnescapedFieldsIncludesEmpty2() {
        this.applyAndCheck2("abc;;de;;f", "abc", "", "de", "", "f");
    }

    @Test
    public void testUnescapedFields3() {
        this.applyAndCheck2("abc;de;f;ghi", "abc", "de", "f", "ghi");
    }

    @Test
    public void testUnescapedFieldsClosingDelimiter() {
        this.applyAndCheck2("abc;de;", "abc", "de", "");
    }

    @Test
    public void testDoubleQuotedField() {
        this.applyAndCheck2("\"a\"", "a");
    }

    @Test
    public void testDoubleQuotedField2() {
        this.applyAndCheck2("\"abc\"", "abc");
    }

    @Test
    public void testDoubleQuotedField3() {
        this.applyAndCheck2("abc\"def\"", "abcdef");
    }

    @Test
    public void testIncludesEscapedDoubleQuote() {
        this.applyAndCheck2("\"a\"\"bc\"", "a\"bc");
    }

    @Test
    public void testIncludesEscapedDoubleQuote2() {
        this.applyAndCheck2("a\"b\"\"c\"", "ab\"c");
    }

    @Test
    public void testDoubleQuotedDelimiter() {
        this.applyAndCheck2("\"abc\";", "abc", "");
    }

    @Test
    public void testSeveralDoubleQuotedFields() {
        this.applyAndCheck2("\"a\";\"bc\"", "a", "bc");
    }

    @Test
    public void testSeveralDoubleQuotedFields2() {
        this.applyAndCheck2("\"ab\";\"cd\"", "ab", "cd");
    }

    @Test
    public void testMixed() {
        this.applyAndCheck2("\"ab\";cd;\"ef\";g",
                "ab", "cd", "ef", "g");
    }

    @Test
    public void testMixed2() {
        this.applyAndCheck2(CsvListCharSequenceFunction.with(','),
                "\"ab\",cd,\"ef\",g",
                "ab", "cd", "ef", "g");
    }

    @Test
    public void testMixed3() {
        this.applyAndCheck2(CsvListCharSequenceFunction.with(','),
                "\"ab\",cd,\"ef\",g,,hijklm",
                "ab", "cd", "ef", "g", "", "hijklm");
    }

    private void applyAndCheck2(final CharSequence input,
                                final CharSequence... expected) {
        this.applyAndCheck2(this.createFunction(),
                input,
                expected);
    }

    private void applyAndCheck2(final CsvListCharSequenceFunction function,
                                final CharSequence input,
                                final CharSequence... expected) {
        this.applyAndCheck(function,
                input,
                Lists.of(expected));
    }

    // Object...........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createFunction(), "';'");
    }

    @Override
    public CsvListCharSequenceFunction createFunction() {
        return CsvListCharSequenceFunction.with(';');
    }

    @Override
    public Class<CsvListCharSequenceFunction> type() {
        return CsvListCharSequenceFunction.class;
    }
}
