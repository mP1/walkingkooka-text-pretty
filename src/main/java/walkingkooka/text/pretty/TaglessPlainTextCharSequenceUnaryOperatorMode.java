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

/**
 * Enums representing the various modes that the state machine can be in.
 */
enum TaglessPlainTextCharSequenceUnaryOperatorMode {

    INSERT_SPACE_BEFORE_TEXT {
        @Override
        void addChar(final char c,
                     final StringBuilder builder) {
            final int length = builder.length();
            if (length > 0 && false == Character.isWhitespace(builder.charAt(length - 1))) {
                builder.append(' ');
            }
            builder.append(c);
        }

        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return c == '<' ?
                    TAG_NAME_COMMENT_ETC :
                    TEXT;
        }
    },
    //
    TEXT {
        @Override
        void addChar(final char c,
                     final StringBuilder builder) {
            builder.append(c);
        }

        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return c == '<' ?
                    TAG_NAME_COMMENT_ETC :
                    this;
        }
    },
    //
    TAG_NAME_COMMENT_ETC {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return (c == 'B') || (c == 'b') ? BOLD //
                    : (c == 'I') || (c == 'i') ? ITALICS //
                    : (c == 'U') || (c == 'u') ? UNDERLINE //
                    : c == '!' ? COMMENT //
                    : c == '/' ? TAG_NAME_COMMENT_ETC //
                    : TAG_NAME;
        }
    },
    //
    BOLD {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return this.handleInstead(c, buffer, "**");
        }
    },
    //
    ITALICS {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return this.handleInstead(c, buffer, "*");
        }
    },
    //
    UNDERLINE {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return this.handleInstead(c, buffer, "_");
        }
    },
    //
    TAG_NAME {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return c == '>' ?
                    INSERT_SPACE_BEFORE_TEXT :
                    c == '/' ?
                            GREATER_THAN :
                            Character.isWhitespace(c) ?
                                    INSIDE_TAG :
                                    TAG_NAME;
        }
    },
    //
    INSIDE_TAG {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return c == '>' ?
                    INSERT_SPACE_BEFORE_TEXT :
                    c == '\'' ?
                            INSIDE_SINGLE_QUOTES :
                            c == '"' ?
                                    DOUBLE_QUOTES :
                                    this;
        }
    },
    //
    INSIDE_SINGLE_QUOTES {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return c == '\'' ?
                    INSIDE_TAG :
                    c == '\\' ?
                            ESCAPING_INSIDE_SINGLE_QUOTES :
                            this;
        }
    },
    //
    ESCAPING_INSIDE_SINGLE_QUOTES {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return INSIDE_SINGLE_QUOTES;
        }
    },
    //
    DOUBLE_QUOTES {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return c == '"' ?
                    INSIDE_TAG :
                    c == '\\' ?
                            ESCAPING_INSIDE_DOUBLE_QUOTES :
                            this;
        }
    },
    //
    ESCAPING_INSIDE_DOUBLE_QUOTES {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return DOUBLE_QUOTES;
        }
    },
    //
    COMMENT {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return c == '-' ?
                    COMMENT_DASH :
                    INSIDE_TAG;
        }
    },
    //
    COMMENT_DASH {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return c == '-' ?
                    INSIDE_COMMENT :
                    INSIDE_TAG;
        }
    },
    //
    INSIDE_COMMENT {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return c == '-' ?
                    END_COMMENT_DASH_DASH :
                    this;
        }
    },
    //
    END_COMMENT_DASH_DASH {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return c == '-' ?
                    GREATER_THAN :
                    INSIDE_COMMENT;
        }
    },
    //
    GREATER_THAN {
        @Override
        TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                             final StringBuilder buffer) {
            return c == '>' ?
                    INSERT_SPACE_BEFORE_TEXT :
                    INSIDE_COMMENT;
        }
    };

    final boolean isText() {
        return INSERT_SPACE_BEFORE_TEXT == this || TEXT == this;
    }

    void addChar(final char c,
                 final StringBuilder builder) {
        throw new UnsupportedOperationException();
    }

    abstract TaglessPlainTextCharSequenceUnaryOperatorMode handle(final char c,
                                                                  final StringBuilder buffer);

    final TaglessPlainTextCharSequenceUnaryOperatorMode handleInstead(final char c,
                                                                      final StringBuilder buffer,
                                                                      final String instead) {
        final TaglessPlainTextCharSequenceUnaryOperatorMode mode;

        if (c == '>') {
            buffer.append(instead);
            mode = TEXT;
        } else {
            if ((c == '/') || Character.isWhitespace(c)) {
                buffer.append(instead);
                mode = INSIDE_TAG;
            } else {
                mode = TAG_NAME;
            }
        }

        return mode;
    }
}
