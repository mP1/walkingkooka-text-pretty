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

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.text.LineEnding;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

final public class TextPretty implements PublicStaticHelper {

    /**
     * {@see CharSequenceBiFunctionAlignCenter}
     */
    public static BiFunction<CharSequence, Integer, CharSequence> centerAlignment() {
        return CharSequenceBiFunctionAlignCenter.INSTANCE;
    }

    /**
     * {@see CharSequenceBiFunctionAlignCharacter}
     */
    public static BiFunction<CharSequence, Integer, CharSequence> character(final CharPredicate chars,
                                                                            final int column) {
        return CharSequenceBiFunctionAlignCharacter.with(chars, column);
    }

    /**
     * {@see ColumnConfig}
     */
    public static ColumnConfig columnConfig() {
        return ColumnConfig.empty();
    }

    /**
     * {@see ColumnsExtractorCharSequenceFunction}
     */
    public static <T> Function<T, List<CharSequence>> columnsExtractor(final List<Function<T, CharSequence>> transformers) {
         return ColumnsExtractorCharSequenceFunction.with(transformers);
    }

    /**
     * {@see CharSequenceColumnsToLinesFunction}
     */
    public static Function<List<CharSequence>, CharSequence> columnsToLines(final IntUnaryOperator rightPaddings,
                                                                            final LineEnding lineEnding) {
        return CharSequenceColumnsToLinesFunction.with(rightPaddings, lineEnding);
    }

    /**
     * {@see CsvListCharSequenceFunction}
     */
    public static Function<CharSequence, List<CharSequence>> csv(final char delimiter) {
        return CsvListCharSequenceFunction.with(delimiter);
    }

    /**
     * {@see CharSequenceBiFunctionAlignLeft}
     */
    public static BiFunction<CharSequence, Integer, CharSequence> leftAlignment() {
        return CharSequenceBiFunctionAlignLeft.INSTANCE;
    }

    /**
     * {@see CharSequenceBiFunctionOverflowMaxWidthBreak}
     */
    public static BiFunction<CharSequence, Integer, CharSequence> overflowMaxWidthBreak() {
        return CharSequenceBiFunctionOverflowMaxWidthBreak.INSTANCE;
    }

    /**
     * {@see CharSequenceBiFunctionOverflowWordBreak}
     */
    public static BiFunction<CharSequence, Integer, CharSequence> overflowWordBreak() {
        return CharSequenceBiFunctionOverflowWordBreak.INSTANCE;
    }

    /**
     * {@see CharSequenceBiFunctionAlignRight}
     */
    public static BiFunction<CharSequence, Integer, CharSequence> rightAlignment() {
        return CharSequenceBiFunctionAlignRight.INSTANCE;
    }

    /**
     * {@see TabExpandingCharSequenceUnaryOperator}
     */
    public static UnaryOperator<CharSequence> tabExpanding(final IntUnaryOperator tabStops) {
        return TabExpandingCharSequenceUnaryOperator.with(tabStops);
    }

    /**
     * {@see Table}
     */
    public static Table table() {
        return Table.empty();
    }

    /**
     * {@see TableConfig#empty}
     */
    public static TableConfig tableConfig() {
        return TableConfig.empty();
    }

    /**
     * {@see TableTransformerUnaryOperator}
     */
    public static UnaryOperator<Table> tableTransformer(final List<ColumnConfig> columns) {
        return TableTransformerUnaryOperator.with(columns);
    }

    /**
     * {@see TagStrippingCharSequenceUnaryOperator}
     */
    public static UnaryOperator<CharSequence> tagStripping() {
        return TagStrippingCharSequenceUnaryOperator.INSTANCE;
    }

    /**
     * {@see CharSequenceBiFunctionTrimLeft}
     */
    public static BiFunction<CharSequence, Integer, CharSequence> trimLeft() {
        return CharSequenceBiFunctionTrimLeft.INSTANCE;
    }

    /**
     * {@see CharSequenceBiFunctionTrimLeftRight}
     */
    public static BiFunction<CharSequence, Integer, CharSequence> trimLeftRight() {
        return CharSequenceBiFunctionTrimLeftRight.INSTANCE;
    }

    /**
     * {@see CharSequenceBiFunctionTrimRight}
     */
    public static BiFunction<CharSequence, Integer, CharSequence> trimRight() {
        return CharSequenceBiFunctionTrimRight.INSTANCE;
    }

    /**
     * {@see CharSequenceBiFunctionOverflowTruncate}
     */
    public static BiFunction<CharSequence, Integer, CharSequence> truncate() {
        return CharSequenceBiFunctionOverflowTruncate.INSTANCE;
    }

    /**
     * {@see WhitespaceNormalizingCharSequenceUnaryOperator}
     */
    public static UnaryOperator<CharSequence> whitespaceNormalizing() {
        return WhitespaceNormalizingCharSequenceUnaryOperator.INSTANCE;
    }

    /**
     * Stop creation
     */
    private TextPretty() {
        throw new UnsupportedOperationException();
    }
}
