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
import walkingkooka.text.LineEnding;

import java.util.List;

/**
 * Cuts any overflowed text at the max width boundary, without carrying if words etc are broken.
 */
final class CharSequenceBiFunctionOverflowMaxWidthBreak extends CharSequenceBiFunctionOverflow {

    /**
     * Singleton
     */
    final static CharSequenceBiFunctionOverflowMaxWidthBreak INSTANCE = new CharSequenceBiFunctionOverflowMaxWidthBreak();

    private CharSequenceBiFunctionOverflowMaxWidthBreak() {
        super();
    }

    @Override
    CharSequence overflowed(final CharSequence text,
                            final int width) {
        final int textLength = text.length();
        final List<CharSequence> lines = Lists.array();

        int start = 0;
        do {
            final int end = Math.min(textLength, start + width);
            lines.add(text.subSequence(start, end));

            start = end;
        } while(start < textLength);

        return MultiLineCharSequence.with(lines, LineEnding.NL); // hardcoding NL doesnt matter will be replaced later anyway.
    }

    @Override
    public String toString() {
        return "OverflowMaxWidthBreak";
    }
}
