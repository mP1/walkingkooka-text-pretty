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


import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.character.CharPredicate;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * A {@link UnaryOperator} that supports assembling transformation upon a columns of text.
 */
public final class ColumnConfig implements UnaryOperator<List<CharSequence>> {

    /**
     * An empty {@link ColumnConfig}
     */
    static ColumnConfig empty() {
        return new ColumnConfig(0, Integer.MAX_VALUE, Lists.empty());
    }

    private ColumnConfig(final int minWidth,
                         final int maxWidth,
                         final List<BiFunction<CharSequence, Integer, CharSequence>> functions) {
        super();
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.functions = functions;
    }

    /**
     * Sets the min width of this column.
     * When transforming a column, any align such as {@link #leftAlign()}, must also be set for the min width of a column to be honoured.
     */
    public ColumnConfig minWidth(final int minWidth) {
        if (minWidth <= 0) {
            throw new IllegalArgumentException("Invalid minWidth " + minWidth + " < 0");
        }
        final int maxWidth = this.maxWidth;
        if(minWidth > maxWidth) {
            throw new IllegalArgumentException("MinWidth " + minWidth + " > maxWidth " + maxWidth);
        }
        return this.minWidth == minWidth ?
                this :
                new ColumnConfig(minWidth, maxWidth, this.functions);
    }

    final int minWidth;

    /**
     * Sets the max width of this column.
     */
    public ColumnConfig maxWidth(final int maxWidth) {
        if (maxWidth <= 0) {
            throw new IllegalArgumentException("Invalid maxWidth " + maxWidth + " <= 0");
        }

        final int minWidth = this.minWidth;
        if (maxWidth < minWidth) {
            throw new IllegalArgumentException("Invalid maxWidth " + maxWidth + " < " + minWidth);
        }
        return this.maxWidth == maxWidth ?
                this :
                new ColumnConfig(minWidth, maxWidth, this.functions);
    }

    final int maxWidth;

    /**
     * Sets the alignment for this {@link ColumnConfig} to CENTER.
     */
    public ColumnConfig centerAlign() {
        return this.add(TextPretty.centerAlignment());
    }

    /**
     * Aligns a column at the give column using the {@link CharPredicate} to find the character.
     */
    public ColumnConfig characterAlign(final CharPredicate chars,
                                       final int column) {
        return this.add(TextPretty.character(chars, column));
    }

    /**
     * Sets the alignment for this {@link ColumnConfig} to LEFT.
     */
    public ColumnConfig leftAlign() {
        return this.add(TextPretty.leftAlignment());
    }

    /**
     * Sets the overflow for this {@link ColumnConfig} to BREAK on the max width boundary.
     */
    public ColumnConfig overflowMaxWidthBreak() {
        return this.add(TextPretty.overflowMaxWidthBreak());
    }

    /**
     * Sets the overflow for this {@link ColumnConfig} to WORD BREAK.
     */
    public ColumnConfig overflowWordBreak() {
        return this.add(TextPretty.overflowWordBreak());
    }

    /**
     * Sets the alignment for this {@link ColumnConfig} to RIGHT.
     */
    public ColumnConfig rightAlign() {
        return this.add(TextPretty.rightAlignment());
    }

    /**
     * Sets the trim LEFT operation for this column, replacing any previous.
     */
    public ColumnConfig trimLeft() {
        return this.add(TextPretty.trimLeft());
    }

    /**
     * Sets the trim RIGHT operation for this column, replacing any previous.
     */
    public ColumnConfig trimLeftRight() {
        return this.add(TextPretty.trimLeftRight());
    }

    /**
     * Sets the trim RIGHT operation for this column, replacing any previous.
     */
    public ColumnConfig trimRight() {
        return this.add(TextPretty.trimRight());
    }

    /**
     * Sets the overflow for this {@link ColumnConfig} to TRUNCATE.
     */
    public ColumnConfig truncate() {
        return this.add(TextPretty.truncate());
    }

    /**
     * Assembles this {@link ColumnConfig} adding another transformer, replacing previous if possible, eg setting RIGHT replaces
     * LEFT.
     */
    ColumnConfig add(final BiFunction<CharSequence, Integer, CharSequence> function) {
        ColumnConfig result = null;

        for (final BiFunction<CharSequence, Integer, CharSequence> possible : this.functions) {
            if (possible.equals(function)) {
                result = this;
                break;
            }
        }

        if (null == result) {
            if (function instanceof CharSequenceBiFunction) {
                final CharSequenceBiFunction function2 = (CharSequenceBiFunction) function;
                int index = 0;

                for (final BiFunction<CharSequence, Integer, CharSequence> possible : this.functions) {
                    if (possible instanceof CharSequenceBiFunction) {
                        final CharSequenceBiFunction charSequenceBiFunction = (CharSequenceBiFunction) possible;
                        if (charSequenceBiFunction.isColumnReplace(function2)) {
                            final List<BiFunction<CharSequence, Integer, CharSequence>> copy = Lists.linkedList();
                            copy.addAll(this.functions);
                            copy.set(index, function);
                            result = this.replaceFunctions(copy);
                            break;
                        }
                    }

                    index++;
                }
            }

            if (null == result) {
                final List<BiFunction<CharSequence, Integer, CharSequence>> copy = Lists.linkedList();
                copy.addAll(this.functions);
                copy.add(function);
                result = this.replaceFunctions(copy);
            }
        }

        return result;
    }

    private ColumnConfig replaceFunctions(final List<BiFunction<CharSequence, Integer, CharSequence>> functions) {
        return new ColumnConfig(this.minWidth, this.maxWidth, functions);
    }

    // UnaryOperator....................................................................................................

    @Override
    public List<CharSequence> apply(final List<CharSequence> rows) {
        return ColumnConfigRequest.with(this).apply(rows);
    }

    final List<BiFunction<CharSequence, Integer, CharSequence>> functions;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.minWidth, this.maxWidth, this.functions);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof ColumnConfig && this.equals0((ColumnConfig) other);
    }

    private boolean equals0(final ColumnConfig other) {
        return this.minWidth == other.minWidth &&
            this.maxWidth == other.maxWidth &&
            this.functions.equals(other.functions);
    }

    // toString.........................................................................................................

    @Override
    public String toString() {
        final int minWidth = this.minWidth;
        final int maxWidth = this.maxWidth;

        final String width;
        if (0 == minWidth) {
            if (Integer.MAX_VALUE == maxWidth) {
                width = "";
            } else {
                width = "width<=" + maxWidth;
            }
        } else {
            if (Integer.MAX_VALUE == maxWidth) {
                width = minWidth + "<=width";
            } else {
                if (maxWidth == minWidth) {
                    width = "width=" + minWidth;
                } else {
                    width = minWidth + "<=width<=" + maxWidth;
                }
            }
        }

        return ToStringBuilder.empty()
                .disable(ToStringBuilderOption.QUOTE)
                .value(width)
                .valueSeparator(" ")
                .value(this.functions)
                .build();
    }
}
