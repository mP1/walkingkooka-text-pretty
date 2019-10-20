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
enum TaglessCharSequenceUnaryOperatorMode {

    INSERT_SPACE_BEFORE_TEXT {
        @Override
        boolean isText() {
            return true;
        }

        @Override
        void addChar(final char c, final StringBuilder builder) {
            builder.append(' ');
            builder.append(c);
        }

        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return c == '<' ? TAG_NAME_COMMENT_ETC : TEXT;
        }
    },
    //
    TEXT {
        @Override
        boolean isText() {
            return true;
        }

        @Override
        void addChar(final char c, final StringBuilder builder) {
            builder.append(c);
        }

        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return c == '<' ? TAG_NAME_COMMENT_ETC : this;
        }
    },
    //
    TAG_NAME_COMMENT_ETC {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
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
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return this.mightBe(c, buffer, "**");
        }
    },
    //
    ITALICS {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return this.mightBe(c, buffer, "*");
        }
    },
    //
    UNDERLINE {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return this.mightBe(c, buffer, "_");
        }
    },
    //
    TAG_NAME {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return c == '>' ?
                    TEXT :
                    c == '/' ?
                            GREATER_THAN :
                            Character.isWhitespace(c) ? INSIDE_TAG : TAG_NAME;
        }
    },
    //
    INSIDE_TAG {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return c == '>' ?
                    TEXT :
                    c == '\'' ? INSIDE_SINGLE_QUOTES : c == '"' ? DOUBLE_QUOTES : this;
        }
    },
    //
    INSIDE_SINGLE_QUOTES {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return c == '\'' ?
                    INSIDE_TAG :
                    c == '\\' ? ESCAPING_INSIDE_SINGLE_QUOTES : this;
        }
    },
    //
    ESCAPING_INSIDE_SINGLE_QUOTES {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return INSIDE_SINGLE_QUOTES;
        }
    },
    //
    DOUBLE_QUOTES {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return c == '"' ? INSIDE_TAG : c == '\\' ? ESCAPING_INSIDE_DOUBLE_QUOTES : this;
        }
    },
    //
    ESCAPING_INSIDE_DOUBLE_QUOTES {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return DOUBLE_QUOTES;
        }
    },
    //
    COMMENT {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return c == '-' ? COMMENT_DASH : INSIDE_TAG;
        }
    },
    //
    COMMENT_DASH {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return c == '-' ? INSIDE_COMMENT : INSIDE_TAG;
        }
    },
    //
    INSIDE_COMMENT {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return c == '-' ? END_COMMENT_DASH_DASH : this;
        }
    },
    //
    END_COMMENT_DASH_DASH {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return c == '-' ? GREATER_THAN : INSIDE_COMMENT;
        }
    },
    //
    GREATER_THAN {
        @Override
        TaglessCharSequenceUnaryOperatorMode process(final char c, final StringBuilder buffer) {
            return c == '>' ? TEXT : INSIDE_COMMENT;
        }
    };

    boolean isText() {
        return false;
    }

    void addChar(final char c, final StringBuilder builder) {
        throw new UnsupportedOperationException();
    }

    abstract TaglessCharSequenceUnaryOperatorMode process(char c, StringBuilder buffer);

    final TaglessCharSequenceUnaryOperatorMode mightBe(final char c, final StringBuilder buffer,
                                                       final String instead) {
        TaglessCharSequenceUnaryOperatorMode mode = TAG_NAME;

        if (c == '>') {
            buffer.append(instead);
            mode = TEXT;
        } else {
            if ((c == '/') || Character.isWhitespace(c)) {
                buffer.append(instead);
                mode = INSIDE_TAG;
            }
        }

        return mode;
    }
}
