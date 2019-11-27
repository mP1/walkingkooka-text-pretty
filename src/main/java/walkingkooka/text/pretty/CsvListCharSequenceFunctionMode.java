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

import walkingkooka.InvalidCharacterException;
import walkingkooka.text.CharSequences;

enum CsvListCharSequenceFunctionMode {

//    /**
//     * About to start reading a new field.
//     */
//    FIELD_BEGIN {
//        @Override
//        CsvListCharSequenceFunctionMode handle(final int i,
//                                               final CharSequence text,
//                                               final CsvListCharSequenceFunctionRequest request) {
//            return CsvListCharSequenceFunctionMode.NON_ESCAPED.handle(i, text, request);
//        }
//
//        @Override
//        void endOfInput(final CsvListCharSequenceFunctionRequest request) {
//            // do nothing.
//        }
//    },
    /**
     * Reading non escaped character(s).
     */
    NON_ESCAPED {
        @Override
        CsvListCharSequenceFunctionMode handle(final int i,
                                               final CharSequence text,
                                               final CsvListCharSequenceFunctionRequest request) {
            final CsvListCharSequenceFunctionMode result;

            final char c = text.charAt(i);
            if (DOUBLE_QUOTE == c) {
                result = INSIDE;
            } else if (request.delimiter == c) {
                request.endOfField();
                result = NON_ESCAPED;
            } else {
                request.append(c);
                result = this;
            }
            return result;
        }

        @Override
        void endOfInput(final CsvListCharSequenceFunctionRequest request) {
            request.endOfField();
        }
    },
    /**
     * Inside a double quote and reading character(s) with support for two double quotes.
     */
    INSIDE {
        @Override
        CsvListCharSequenceFunctionMode handle(final int i,
                                               final CharSequence text,
                                               final CsvListCharSequenceFunctionRequest request) {
            final CsvListCharSequenceFunctionMode result;

            final char c = text.charAt(i);
            if (DOUBLE_QUOTE == c) {
                result = DOUBLE_QUOTE_INSIDE;
            } else {
                request.append(c);
                result = this;
            }
            return result;
        }

        @Override
        void endOfInput(final CsvListCharSequenceFunctionRequest request) {
            throw new IllegalArgumentException("Unterminated double quoted field " + CharSequences.quoteAndEscape(request.field));
        }
    },
    /**
     * Just after hitting a double quote when inside a double quoted string. This might be a terminating
     * double quote or an escaped double quote.
     */
    DOUBLE_QUOTE_INSIDE {
        @Override
        CsvListCharSequenceFunctionMode handle(final int i,
                                               final CharSequence text,
                                               final CsvListCharSequenceFunctionRequest request) {
            final CsvListCharSequenceFunctionMode result;

            final char c = text.charAt(i);
            if (DOUBLE_QUOTE == c) {
                request.append(c);
                result = INSIDE;
            } else {
                if (request.delimiter == c) {
                    request.endOfField();
                    result = NON_ESCAPED;
                } else {
                    throw new InvalidCharacterException(text.toString(), i);
                }
            }
            return result;
        }

        @Override
        void endOfInput(final CsvListCharSequenceFunctionRequest request) {
            request.endOfField();
        }
    };

    abstract CsvListCharSequenceFunctionMode handle(final int i,
                                                    final CharSequence text,
                                                    final CsvListCharSequenceFunctionRequest request);

    /**
     * Called after the last character has been read. Allows fields to be saved etc.
     */
    abstract void endOfInput(final CsvListCharSequenceFunctionRequest request);

    private final static char DOUBLE_QUOTE = '"';
}
