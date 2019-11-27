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

import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Reads a {@link CharSequence} holding a csv or any other character delimiter, with support for double quoting to allow inclusion of the field delimiter character.
 * <a href="https://tools.ietf.org/html/rfc4180">rfc4180</a>
 */
final class CsvListCharSequenceFunction implements Function<CharSequence, List<CharSequence>> {

    static CsvListCharSequenceFunction with(final char delimiter) {
        return new CsvListCharSequenceFunction(delimiter);
    }

    private CsvListCharSequenceFunction(final char delimiter) {
        super();
        this.delimiter = delimiter;
    }

    public List<CharSequence> apply(final CharSequence text) {
        Objects.requireNonNull(text, "text");

        return 0 == text.length() ?
                Lists.empty() :
                applyNonEmpty(text);
    }

    private List<CharSequence> applyNonEmpty(final CharSequence text) {
        final int length = text.length();

        final CsvListCharSequenceFunctionRequest request = CsvListCharSequenceFunctionRequest.with(this.delimiter);

        CsvListCharSequenceFunctionMode mode = CsvListCharSequenceFunctionMode.NON_ESCAPED;

        for (int i = 0; i < length; i++) {
            mode = mode.handle(i, text, request);
        }
        mode.endOfInput(request);

        return Lists.readOnly(request.fields);
    }

    private final char delimiter;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return CharSequences.quoteAndEscape(this.delimiter).toString();
    }
}
