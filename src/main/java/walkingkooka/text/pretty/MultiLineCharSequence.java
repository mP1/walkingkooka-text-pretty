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
import java.util.Objects;

/**
 * A {@link CharSequence} holding individual lines each represented as a {@link CharSequence} without any line endings.
 */
final class MultiLineCharSequence implements CharSequence {

    static MultiLineCharSequence parse(final CharSequence text,
                                       final LineEnding lineEnding) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(lineEnding, "lineEnding");

        return text instanceof MultiLineCharSequence ?
                ((MultiLineCharSequence) text).setLineEnding(lineEnding) :
                parseCharSequence(text, lineEnding);
    }

    private static MultiLineCharSequence parseCharSequence(final CharSequence text,
                                                           final LineEnding lineEnding) {
        final List<CharSequence> lines = Lists.array();
        final int length = text.length();
        char previous = 0;
        int start = 0;
        int i = 0;

        for (; ; ) {
            if (length == i) {
                if (length != start) {
                    lines.add(text.subSequence(start, length));
                }
                break;
            }
            final char c = text.charAt(i);
            switch (c) {
                case '\n': // NL
                    if ('\r' != previous) {
                        lines.add(text.subSequence(start, i)); // without the NL
                    } // else was CRNL

                    i++;
                    start = i; // after the NL

                    previous = c;
                    break;
                case '\r': // CR
                    lines.add(text.subSequence(start, i)); // without the CR
                    i++;
                    start = i; // after the CR

                    previous = '\r';
                    break;
                default:
                    previous = c;
                    i++;
                    break;
            }
        }

        return new MultiLineCharSequence(lines, lineEnding);
    }

    static MultiLineCharSequence with(final List<CharSequence> lines,
                                      final LineEnding lineEnding) {
        Objects.requireNonNull(lines, "lines");
        Objects.requireNonNull(lineEnding, "lineEnding");

        for (final CharSequence line : lines) {
            checkLine(line);
        }

        return new MultiLineCharSequence(Lists.immutable(lines),
                lineEnding);
    }

    private MultiLineCharSequence(final List<CharSequence> lines,
                                  final LineEnding lineEnding) {
        super();
        this.lines = lines;
        this.lineEnding = lineEnding;
    }

    private MultiLineCharSequence setLineEnding(final LineEnding lineEnding) {
        return this.lineEnding.equals(lineEnding) ?
                this :
                new MultiLineCharSequence(this.lines, lineEnding);
    }

    private final LineEnding lineEnding;

    /**
     * Returns the widest column width in characters without the line ending.
     */
    int maxWidth() {
        if (-1 == this.maxWidth) {
            this.maxWidth = this.lines.stream()
                    .mapToInt(CharSequence::length)
                    .max()
                    .orElse(0);
        }
        return this.maxWidth;
    }

    private int maxWidth = -1;

    int lineCount() {
        return this.lines.size();
    }

    /**
     * Getter that retrieves the line at the requested line number.
     */
    CharSequence line(final int lineNumber) {
        final int lineCount = this.lines.size();
        if (lineNumber < 0 || lineNumber >= lineCount) {
            throw new IllegalArgumentException("Line number " + lineNumber + " not between 0 and " + lineCount);
        }
        return this.lines.get(lineNumber);
    }

    /**
     * Would be setter that returns a {@link MultiLineCharSequence} with the line, creating a new instance if necessary.
     */
    MultiLineCharSequence setLine(final int lineNumber,
                                  final CharSequence line) {
        return this.line(lineNumber).equals(line) ?
                this :
                this.replace(lineNumber, line);
    }

    private MultiLineCharSequence replace(final int lineNumber,
                                          final CharSequence line) {
        checkLine(line);

        final List<CharSequence> replaced = Lists.array();
        replaced.addAll(this.lines);
        replaced.set(lineNumber, line);

        return new MultiLineCharSequence(replaced, this.lineEnding);
    }

    private static void checkLine(final CharSequence line) {
        if (CharSequences.indexOf(line, "\n") != -1) {
            throw new IllegalArgumentException("Line " + CharSequences.quoteAndEscape(line) + " contains '\\n'");
        }
        if (CharSequences.indexOf(line, "\r") != -1) {
            throw new IllegalArgumentException("Line " + CharSequences.quoteAndEscape(line) + " contains '\\r'");
        }
    }

    /**
     * The lines, each {@link CharSequence} must not have any line ending.
     */
    final List<CharSequence> lines;

    // CharSequence.....................................................................................................

    @Override
    public int length() {
        if (-1 == this.length) {
            final List<CharSequence> lines = this.lines;

            this.length = lines.size() * this.lineEnding.length() +
                    lines.stream()
                            .mapToInt(CharSequence::length)
                            .sum();
        }
        return this.length;
    }

    private int length = -1;

    @Override
    public char charAt(final int index) {
        final int length = this.length();

        if (index < 0 || index >= length) {
            throw new StringIndexOutOfBoundsException("Index " + index + " < 0 || >= " + length);
        }

        char c = 0;

        int at = index;
        for (final CharSequence line : this.lines) {
            final int lineLength = line.length();
            if (at < lineLength) {
                c = line.charAt(at);
                break;
            }
            at = at - lineLength;

            final LineEnding lineEnding = this.lineEnding;
            final int lineEndingLength = lineEnding.length();
            if (at < lineEndingLength) {
                c = lineEnding.charAt(at);
                break;
            }

            at = at - lineEndingLength;
        }

        return c;
    }

    @Override
    public CharSequence subSequence(final int start,
                                    final int end) {
        if (start < 0) {
            throw new StringIndexOutOfBoundsException("Start " + start + " < 0");
        }
        if (start > end) {
            throw new StringIndexOutOfBoundsException("Start " + start + " > end " + end);
        }

        final int length = this.length();
        if (end > length) {
            throw new StringIndexOutOfBoundsException("End " + end + "> " + length);
        }

        return start == end ?
                "" :
                0 == start && length == end ?
                        this :
                        subSequence0(start, end);
    }

    private CharSequence subSequence0(final int start,
                                      final int end) {
        CharSequence sub = null;

        int at = 0;

        for (final CharSequence line : this.lines) {
            final int lineLength = line.length();
            if (start >= at && end <= at + lineLength) {
                sub = line.subSequence(start - at, end - at);
                break;
            }
            at += lineLength;

            final int eolLength = this.lineEnding.length();
            if (start >= at && end <= at + eolLength) {
                sub = this.lineEnding.subSequence(start - at, end - at);
                break;
            }

            at += eolLength;
        }

        return null == sub ?
                this.toString().substring(start, end) :
                sub;
    }

    // Object ..........................................................................................................

    @Override
    public int hashCode() {
        if (0 == this.hashCode) {
            this.hashCode = CharSequences.hash(this);
        }
        return this.hashCode;
    }

    private int hashCode = 0;

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof MultiLineCharSequence && this.equals0((MultiLineCharSequence) other);
    }

    private boolean equals0(final MultiLineCharSequence other) {
        return this.lineEnding.equals(other.lineEnding) && this.lines.equals(other.lines);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        if (null == this.toString) {
            this.toString = String.join(this.lineEnding, this.lines)
                    .concat(this.lineEnding.toString());
        }
        return this.toString;
    }

    private String toString;
}
