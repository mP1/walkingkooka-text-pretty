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
import walkingkooka.text.LineEnding;

import java.util.List;

/**
 * Breaks at word boundaries any lines that exceed the max width.
 */
final class CharSequenceBiFunctionOverflowWordBreak extends CharSequenceBiFunctionOverflow {

    /**
     * Singleton
     */
    final static CharSequenceBiFunctionOverflowWordBreak INSTANCE = new CharSequenceBiFunctionOverflowWordBreak();

    private CharSequenceBiFunctionOverflowWordBreak() {
        super();
    }

    @Override
    CharSequence overflowed(final CharSequence text,
                            final int width) {
        final int textLength = text.length();
        final List<CharSequence> lines = Lists.array();

        int lineStart = 0;
        boolean skipWhitespace = false;

        while(lineStart < textLength) {
            if(skipWhitespace) {
                skipWhitespace = false;

                while(lineStart <= textLength) {
                    if(false == isBreak(text.charAt(lineStart))) {
                        break;
                    }
                    lineStart++;
                }
                continue;
            }

            // not skipping whitespace
            final int nextLineStart = lineStart + width;
            if(textLength <= nextLineStart) {
                // last line...
                lines.add(CharSequences.trimRight(text.subSequence(lineStart, textLength)));
                break;
            }

            // try find end
            int lineEnd = nextLineStart;
            for(;;) {
                lineEnd--;

                // line has no whitespace must be full of text, insert line break at right edge
                if(lineEnd == lineStart) {
                    lines.add(text.subSequence(lineStart, nextLineStart));
                    lineStart = nextLineStart;
                    break;
                }

                //
                if(isBreak(text.charAt(lineEnd))) {
                    lines.add(CharSequences.trimRight(text.subSequence(lineStart, lineEnd)));
                    lineStart = lineEnd;
                    break;
                }
            }

            skipWhitespace = true;
        }

        return MultiLineCharSequence.with(lines, LineEnding.NL); // hardcoding NL doesnt matter will be replaced later anyway.
    }

    private static boolean isBreak(final char c) {
        return Character.isWhitespace(c);
    }

    @Override
    public String toString() {
        return "OverflowWordBreak";
    }
}
