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

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.util.FunctionTesting;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TableTransformerUnaryOperatorTest extends TextPrettyTestCase<TableTransformerUnaryOperator>
        implements FunctionTesting<TableTransformerUnaryOperator, Table, Table>,
        ToStringTesting<TableTransformerUnaryOperator> {

    @Test
    public void testWithNullColumnsFails() {
        assertThrows(NullPointerException.class, () -> TableTransformerUnaryOperator.with(null));
    }

    @Test
    public void testWithIncludesColumnsFails() {
        assertThrows(NullPointerException.class, () -> TableTransformerUnaryOperator.with(Lists.of(TextPretty.column().maxWidth(8).rightAlign(), null)));
    }

    @Test
    public void testOneRowAllColumnsPresent() {
        this.applyAndCheck2(Table.empty()
                        .setRow(0, Lists.of("A", "B", "1")),
                Table.empty()
                        .setRow(0, Lists.of("A", "       B", "1")));
    }

    @Test
    public void testOneRowMissingColumn() {
        this.applyAndCheck2(Table.empty()
                        .setRow(0, Lists.of("A", "B")),
                Table.empty()
                        .setRow(0, Lists.of("A", "       B")));
    }

    @Test
    public void testOneRowExtraColumnIgnored() {
        final String extra = "*Extra*";

        this.applyAndCheck2(Table.empty()
                        .setRow(0, Lists.of("A", "B", "1", extra)),
                Table.empty()
                        .setRow(0, Lists.of("A", "       B", "1", extra)));
    }

    @Test
    public void testManyRows() {
        this.applyAndCheck2(Table.empty()
                        .setRow(0, Lists.of("A", "B", "1"))
                        .setRow(1, Lists.of("C", "D", "2.0")),
                Table.empty()
                        .setRow(0, Lists.of("A", "       B", "1"))
                        .setRow(1, Lists.of("C", "       D", "    2.0")));
    }

    @Test
    public void testManyRows2() {
        this.applyAndCheck2(Table.empty()
                        .setRow(0, Lists.of("A", "B", "1"))
                        .setRow(1, Lists.of("C", "D", "2.0"))
                        .setRow(2, Lists.of("E", "FF", "3.50")),
                Table.empty()
                        .setRow(0, Lists.of("A", "       B", "1"))
                        .setRow(1, Lists.of("C", "       D", "    2.0"))
                        .setRow(2, Lists.of("E", "      FF", "    3.50")));
    }

    private void applyAndCheck2(final Table input,
                                final Table result) {
        this.applyAndCheck2(this.createFunction(), input, result);
    }

    private void applyAndCheck2(final TableTransformerUnaryOperator transformer,
                                final Table input,
                                final Table result) {
        assertEquals(result.toString(),
                transformer.apply(input).toString(),
                () -> "Wrong result for " + transformer + " for params: " + input);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createFunction(), "[maxWidth=6 Left, maxWidth=8 Right, maxWidth=10 '.' at 5]");
    }

    @Override
    public TableTransformerUnaryOperator createFunction() {
        return this.createFunction0(TextPretty.column().maxWidth(6).leftAlign(),
                TextPretty.column().maxWidth(8).rightAlign(),
                TextPretty.column().maxWidth(10).characterAlign(CharPredicates.is('.'), 5));
    }

    private TableTransformerUnaryOperator createFunction0(final ColumnConfig... columns) {
        return TableTransformerUnaryOperator.with(Lists.of(columns));
    }

    @Override
    public Class<TableTransformerUnaryOperator> type() {
        return TableTransformerUnaryOperator.class;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNameSuffix() {
        return UnaryOperator.class.getSimpleName();
    }
}
