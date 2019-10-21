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
import java.util.function.UnaryOperator;

/**
 * A {@link UnaryOperator} that returns another {@link CharSequence} removing tags and comments. This is equivalent of
 * HTML ELEMENT.plainText
 */
final class TagStrippingCharSequenceUnaryOperator implements UnaryOperator<CharSequence> {
    /**
     * Singleton
     */
    final static TagStrippingCharSequenceUnaryOperator INSTANCE = new TagStrippingCharSequenceUnaryOperator();

    /**
     * Private ctor use singleton
     */
    private TagStrippingCharSequenceUnaryOperator() {
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
        final StringBuilder builder = new StringBuilder();

        TagStrippingCharSequenceUnaryOperatorMode mode = TagStrippingCharSequenceUnaryOperatorMode.TEXT;
        int i = 0;
        char lastTextCharacter = 0;

        Loop:
        //
        do {
            if (mode.isText()) {
                do {
                    final char c = chars.charAt(i);
                    i++;
                    if ('<' == c) {
                        mode = TagStrippingCharSequenceUnaryOperatorMode.TAG_NAME_COMMENT_ETC;
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
                    mode = TagStrippingCharSequenceUnaryOperatorMode.TEXT;
                    if (length == i) {
                        break Loop;
                    }
                } while (true);
            }
            while (i < length && false == mode.isText()) {
                mode = mode.handle(chars.charAt(i), builder);
                i++;
            }
        } while (i < length);

        return ImmutableCharSequence.with(builder);
    }

    @Override
    public String toString() {
        return "TagStripping";
    }
}
