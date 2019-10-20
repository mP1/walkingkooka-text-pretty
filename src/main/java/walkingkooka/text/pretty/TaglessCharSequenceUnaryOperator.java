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

import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * A {@link UnaryOperator} that returns another {@link CharSequence} removing tags and comments.
 */
final class TaglessCharSequenceUnaryOperator implements UnaryOperator<CharSequence> {
    /**
     * Singleton
     */
    final static TaglessCharSequenceUnaryOperator INSTANCE = new TaglessCharSequenceUnaryOperator();

    /**
     * Private ctor use singleton
     */
    private TaglessCharSequenceUnaryOperator() {
        super();
    }

    @Override
    public CharSequence apply(final CharSequence chars) {
        Objects.requireNonNull(chars, "chars");

        return chars.length() == 0 ?
                chars :
                nonEmpty(chars);
    }

    private CharSequence nonEmpty(final CharSequence chars) {
        final int length = chars.length();

        TaglessCharSequenceUnaryOperatorMode mode = TaglessCharSequenceUnaryOperatorMode.TEXT;
        int i = 0;
        final StringBuilder builder = new StringBuilder();
        char lastTextCharacter = 0;

        Loop:
        //
        do {
            if (mode.isText()) {
                do {
                    final char c = chars.charAt(i);
                    i++;
                    if ('<' == c) {
                        mode = TaglessCharSequenceUnaryOperatorMode.TAG_NAME_COMMENT_ETC;
                        break;
                    }
                    if (Character.isWhitespace(c)) {
                        if (false == Character.isWhitespace(lastTextCharacter)) {
                            builder.append(c);
                        }
                        lastTextCharacter = c;
                        if (length == i) {
                            break Loop;
                        }
                        continue;
                    }
                    lastTextCharacter = c;
                    mode.addChar(c, builder);
                    mode = TaglessCharSequenceUnaryOperatorMode.TEXT;
                    if (length == i) {
                        break Loop;
                    }
                } while (true);
            }
            while ((i < length) && (false == mode.isText())) {
                mode = mode.process(chars.charAt(i), builder);
                i++;
            }
            if (mode.isText() && (false == Character.isWhitespace(lastTextCharacter))) {
                mode = TaglessCharSequenceUnaryOperatorMode.INSERT_SPACE_BEFORE_TEXT;
            }
        } while (i < length);

        return ImmutableCharSequence.with(builder);
    }

    @Override
    public String toString() {
        return "Tagless";
    }
}
