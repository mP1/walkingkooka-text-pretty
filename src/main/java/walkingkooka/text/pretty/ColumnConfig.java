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
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.character.CharPredicate;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * A {@link UnaryOperator} that supports assembling transformation upon a columns of text.
 */
public final class ColumnConfig implements UnaryOperator<List<CharSequence>> {

    /**
     * An empty {@link ColumnConfig}
     */
    static ColumnConfig empty() {
        return new ColumnConfig(Integer.MAX_VALUE, Lists.empty());
    }

    private ColumnConfig(final int maxWidth,
                         final List<BiFunction<CharSequence, Integer, CharSequence>> functions) {
        super();
        this.maxWidth = maxWidth;
        this.functions = functions;
    }

    /**
     * Sets the max width of this column.
     */
    public ColumnConfig maxWidth(final int maxWidth) {
        if (maxWidth <= 0) {
            throw new IllegalArgumentException("Invalid maxWidth " + maxWidth + " < 0");
        }
        return this.maxWidth == maxWidth ?
                this :
                new ColumnConfig(maxWidth, this.functions);
    }

    private void checkMaxWidth() {
        if (Integer.MAX_VALUE == this.maxWidth) {
            throw new IllegalStateException("Max width required");
        }
    }

    final int maxWidth;

    /**
     * Sets the alignment for this {@link ColumnConfig} to CENTER.
     */
    public ColumnConfig centerAlign() {
        this.checkMaxWidth();
        return this.add(TextPretty.centerAlignment());
    }

    /**
     * Aligns a column at the give column using the {@link CharPredicate} to find the character.
     */
    public ColumnConfig characterAlign(final CharPredicate chars,
                                       final int column) {
        this.checkMaxWidth();
        return this.add(TextPretty.character(chars, column));
    }

    /**
     * Sets the alignment for this {@link ColumnConfig} to LEFT.
     */
    public ColumnConfig leftAlign() {
        this.checkMaxWidth();
        return this.add(TextPretty.leftAlignment());
    }

    /**
     * Sets the overflow for this {@link ColumnConfig} to BREAK on the max width boundary.
     */
    public ColumnConfig overflowMaxWidthBreak() {
        this.checkMaxWidth();
        return this.add(TextPretty.overflowMaxWidthBreak());
    }

    /**
     * Sets the overflow for this {@link ColumnConfig} to WORD BREAK.
     */
    public ColumnConfig overflowWordBreak() {
        this.checkMaxWidth();
        return this.add(TextPretty.overflowWordBreak());
    }

    /**
     * Sets the alignment for this {@link ColumnConfig} to RIGHT.
     */
    public ColumnConfig rightAlign() {
        this.checkMaxWidth();
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
        this.checkMaxWidth();
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
        return new ColumnConfig(this.maxWidth, functions);
    }

    // UnaryOperator....................................................................................................

    @Override
    public List<CharSequence> apply(final List<CharSequence> rows) {
        return rows.stream()
                .map(this::applyColumn)
                .collect(Collectors.toList());
    }

    private CharSequence applyColumn(final CharSequence row) {
        CharSequence out = row;

        for (final BiFunction<CharSequence, Integer, CharSequence> function : this.functions) {
            out = function.apply(out, this.maxWidth);
        }

        return out;
    }

    final List<BiFunction<CharSequence, Integer, CharSequence>> functions;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.maxWidth, this.functions);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof ColumnConfig && this.equals0((ColumnConfig) other);
    }

    private boolean equals0(final ColumnConfig other) {
        return this.maxWidth == other.maxWidth && this.functions.equals(other.functions);
    }

    // toString.........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .labelSeparator("=")
                .label("maxWidth").value(this.maxWidth)
                .valueSeparator(" ")
                .value(this.functions)
                .build();
    }
}
