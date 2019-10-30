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

import walkingkooka.text.LineEnding;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Base class for all {@link BiFunction} that routes the text after testing the width.
 */
abstract class CharSequenceBiFunction implements BiFunction<CharSequence, Integer, CharSequence> {

    /**
     * Package private ctor to limit sub classing.
     */
    CharSequenceBiFunction() {
        super();
    }

    @Override
    public final CharSequence apply(final CharSequence text,
                                    final Integer width) {
        // an empty text is a special case, to avoid returning an empty line with a line ending.
        return text.length() == 0 ?
                text :
                handleNotEmpty(text, width);
    }

    private CharSequence handleNotEmpty(final CharSequence text,
                                        final int width) {
        final MultiLineCharSequence multi = MultiLineCharSequence.parse(text, LineEnding.NL);
        return multi.lines.size() == 1 ?
                this.handleLine(text, width) :
                this.handleMultiLine(text, multi, width);
    }

    /**
     * Process lines from bottom to top, because some lines might be replaced by multiple lines.
     */
    private CharSequence handleMultiLine(final CharSequence text,
                                         final MultiLineCharSequence multi,
                                         final int width) {
        MultiLineCharSequence result = multi;
        final List<CharSequence> lines = result.lines;
        int lineCount = lines.size();
        for (int i = 0; i < lineCount; i++) {
            final int lineNumber = lineCount - i - 1;
            final CharSequence line = result.line(lineNumber);
            result = result.setText(lineNumber, this.handleLine(line, width));
        }

        // if result is the same object return the original different then it must have changes
        return result == text ?
                text :
                result;
    }

    private CharSequence handleLine(final CharSequence text,
                                    final int width) {
        final int length = text.length();
        return 0 == length ?
                this.empty(width) :
                length <= width ?
                        length == width ?
                                this.full(text, width) :
                                this.notEmpty(text, width) :
                        this.overflowed(text, width);
    }

    /**
     * The individual line is empty.
     */
    abstract CharSequence empty(final int width);

    /**
     * A line is not empty but less than the width
     */
    abstract CharSequence notEmpty(final CharSequence text,
                                   final int width);

    /**
     * A line width matches the desired width exactly
     */
    abstract CharSequence full(final CharSequence text,
                               final int width);

    /**
     * The line length is greater than the width.
     */
    abstract CharSequence overflowed(final CharSequence text,
                                     final int width);

    // Column...........................................................................................................

    /**
     * Helper used by {@link Column#add(BiFunction)} that helps overwrite a function already present in {@link Column#functions}.
     */
    abstract boolean isColumnReplace(final BiFunction<CharSequence, Integer, CharSequence> function);
}
