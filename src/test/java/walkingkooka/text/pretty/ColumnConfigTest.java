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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;
import walkingkooka.util.FunctionTesting;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ColumnConfigTest implements FunctionTesting<ColumnConfig, List<CharSequence>, List<CharSequence>>,
        ClassTesting<ColumnConfig>,
        HashCodeEqualsDefinedTesting2<ColumnConfig>,
        ToStringTesting<ColumnConfig> {

    private final static CharPredicate CHARACTER = CharPredicates.is('.');
    private final static int CHARACTER_COLUMN = 5;

    @Test
    public void testMaxWidthInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> ColumnConfig.empty().maxWidth(-1));
    }

    @Test
    public void testMaxWidthInvalidFails2() {
        assertThrows(IllegalArgumentException.class, () -> ColumnConfig.empty().maxWidth(0));
    }

    @Test
    public void testCenterAlignWithoutMaxWidthFails() {
        assertThrows(IllegalStateException.class, () -> ColumnConfig.empty().centerAlign());
    }

    @Test
    public void testMaxWidth() {
        final int width = 123;

        this.check(ColumnConfig.empty()
                        .maxWidth(width),
                123);
    }

    @Test
    public void testMaxWidthSame() {
        final int width = 123;

        final ColumnConfig column = ColumnConfig.empty()
                .maxWidth(width);
        assertSame(column, column.maxWidth(width));
    }

    @Test
    public void testCharacterAlign() {
        this.check(ColumnConfig.empty()
                        .maxWidth(80)
                        .characterAlign(CHARACTER, CHARACTER_COLUMN),
                80,
                TextPretty.character(CHARACTER, CHARACTER_COLUMN));
    }

    @Test
    public void testCenterAlign() {
        this.check(ColumnConfig.empty()
                        .maxWidth(80)
                        .centerAlign(),
                80,
                TextPretty.centerAlignment());
    }

    @Test
    public void testLeftAlign() {
        this.check(ColumnConfig.empty()
                        .maxWidth(81)
                        .leftAlign(),
                81,
                TextPretty.leftAlignment());
    }

    @Test
    public void testRightAlign() {
        this.check(ColumnConfig.empty()
                        .maxWidth(82)
                        .rightAlign(),
                82,
                TextPretty.rightAlignment());
    }

    @Test
    public void testTrimLeft() {
        this.check(ColumnConfig.empty()
                        .trimLeft(),
                TextPretty.trimLeft());
    }

    @Test
    public void testTrimLeftRight() {
        this.check(ColumnConfig.empty()
                        .trimLeftRight(),
                TextPretty.trimLeftRight());
    }

    @Test
    public void testTrimRight() {
        this.check(ColumnConfig.empty()
                        .trimRight(),
                TextPretty.trimRight());
    }

    @Test
    public void testTruncate() {
        this.check(ColumnConfig.empty()
                        .maxWidth(83)
                        .truncate(),
                83,
                TextPretty.truncate());
    }

    @Test
    public void testLeftAlignLeftAlignReplacedAlign() {
        this.check(ColumnConfig.empty()
                        .maxWidth(84)
                        .leftAlign()
                        .leftAlign(),
                84,
                TextPretty.leftAlignment());
    }

    @Test
    public void testLeftAlignRightAlignReplacedAlign() {
        this.check(ColumnConfig.empty()
                        .maxWidth(85)
                        .leftAlign()
                        .rightAlign(),
                85,
                TextPretty.rightAlignment());
    }

    @Test
    public void testCenterAlignRightAlignReplacedAlign() {
        this.check(ColumnConfig.empty()
                        .maxWidth(90)
                        .centerAlign()
                        .rightAlign(),
                90,
                TextPretty.rightAlignment());
    }

    @Test
    public void testCenterAlignCharacterAlign() {
        this.check(ColumnConfig.empty()
                        .maxWidth(90)
                        .centerAlign()
                        .characterAlign(CHARACTER, CHARACTER_COLUMN),
                90,
                TextPretty.character(CHARACTER, CHARACTER_COLUMN));
    }

    @Test
    public void testCharacterAlignCharacterAlign() {
        this.check(ColumnConfig.empty()
                        .maxWidth(90)
                        .characterAlign(CHARACTER, CHARACTER_COLUMN - 1)
                        .characterAlign(CHARACTER, CHARACTER_COLUMN),
                90,
                TextPretty.character(CHARACTER, CHARACTER_COLUMN));
    }

    @Test
    public void testSetLeftMaxWidth() {
        final int width = 120;

        this.check(ColumnConfig.empty()
                        .maxWidth(80)
                        .leftAlign()
                        .maxWidth(width),
                width,
                TextPretty.leftAlignment());
    }

    @Test
    public void testSetMaxWidthCenterAlign() {
        final int width = 132;

        this.check(ColumnConfig.empty()
                        .maxWidth(80)
                        .rightAlign()
                        .maxWidth(width)
                        .centerAlign(),
                width,
                TextPretty.centerAlignment());
    }

    @Test
    public void testSetMaxWidthTrimLeftTrimRight() {
        final int width = 132;

        this.check(ColumnConfig.empty()
                        .maxWidth(width)
                        .trimLeft()
                        .trimRight()
                        .centerAlign(),
                width,
                TextPretty.trimRight(), TextPretty.centerAlignment());
    }

    @Test
    public void testSetMaxWidthTrimLeftRightTrimLeft() {
        final int width = 132;

        this.check(ColumnConfig.empty()
                        .maxWidth(width)
                        .trimLeftRight()
                        .trimLeft()
                        .centerAlign(),
                width,
                TextPretty.trimLeft(), TextPretty.centerAlignment());
    }

    private void check(final ColumnConfig column,
                       final BiFunction<CharSequence, Integer, CharSequence>... functions) {
        this.check(column, Integer.MAX_VALUE, functions);
    }

    private void check(final ColumnConfig column,
                       final int maxWidth,
                       final BiFunction<CharSequence, Integer, CharSequence>... functions) {
        assertEquals(Lists.of(functions),
                column.functions,
                () -> "functions: " + column);
        assertEquals(maxWidth, column.maxWidth, "maxWidth");
    }

    // apply............................................................................................................

    @Test
    public void testEmptyApply() {
        final List<CharSequence> lines = Lists.of("line1", "line2");

        this.apply2(ColumnConfig.empty(),
                lines,
                lines);
    }

    @Test
    public void testLeftAlignApply() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(80)
                        .leftAlign(),
                Lists.of("line 1", "  line 2"));
    }

    @Test
    public void testCharacterAlignApply() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(80)
                        .characterAlign(CHARACTER, CHARACTER_COLUMN),
                Lists.of("1.23", "12.34", "ignored"),
                Lists.of("    1.23", "   12.34", "ignored"));
    }

    @Test
    public void testRightAlignApply() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(10)
                        .rightAlign(),
                Lists.of("line1", "line 2"),
                Lists.of("     line1", "    line 2"));
    }

    @Test
    public void testTrimLeftRightLeftAlignApply() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(20)
                        .trimLeftRight()
                        .leftAlign(),
                Lists.of("  line1  ", "  line 2  "),
                Lists.of("line1", "line 2"));
    }

    @Test
    public void testTrimLeftApply() {
        this.apply2(ColumnConfig.empty()
                        .trimLeft(),
                Lists.of("  line1  ", "  line 2  "),
                Lists.of("line1  ", "line 2  "));
    }

    @Test
    public void testTrimRightApply() {
        this.apply2(ColumnConfig.empty()
                        .trimRight(),
                Lists.of("  line1  ", "  line 2  "),
                Lists.of("  line1", "  line 2"));
    }

    @Test
    public void testMaxWidthTruncateCenterApply() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(10)
                        .truncate()
                        .centerAlign(),
                Lists.of("line1", "line2=====///"),
                Lists.of("  line1   ", "line2====="));
    }

    @Test
    public void testMaxWidthOverflowMaxWidthBreakLeftAlign() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(10)
                        .overflowMaxWidthBreak()
                        .leftAlign(),
                Lists.of("line1", "line2"));
    }

    @Test
    public void testMaxWidthOverflowMaxWidthBreakRightAlignLinesBroken() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(10)
                        .overflowMaxWidthBreak()
                        .rightAlign(),
                Lists.of("line1=====line2=====line3"),
                Lists.of("line1=====\nline2=====\n     line3\n"));
    }

    @Test
    public void testMaxWidthOverflowMaxWidthBreakRightAlignLinesBroken2() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(8)
                        .overflowMaxWidthBreak()
                        .rightAlign(),
                Lists.of("line1", "line2===line3===line4", "line5"),
                Lists.of("   line1", "line2===\nline3===\n   line4\n", "   line5"));
    }

    @Test
    public void testMaxWidthOverflowMaxWidthBreakRightAlignLinesBroken3() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(8)
                        .overflowMaxWidthBreak()
                        .rightAlign(),
                Lists.of("line1", "line2===line3===line4", "", "line6"),
                Lists.of("   line1", "line2===\nline3===\n   line4\n", "", "   line6"));
    }

    @Test
    public void testMaxWidthOverflowWordBreakRightAlign() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(8)
                        .overflowWordBreak()
                        .rightAlign(),
                Lists.of("line1", "line2", "line3"),
                Lists.of("   line1", "   line2", "   line3"));
    }

    @Test
    public void testMaxWidthOverflowWordBreakRightAlign2() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(8)
                        .overflowWordBreak()
                        .rightAlign(),
                Lists.of("line1", "", "line3"),
                Lists.of("   line1", "", "   line3"));
    }

    @Test
    public void testMaxWidthOverflowWordBreakRightAlign3() {
        this.apply2(ColumnConfig.empty()
                        .maxWidth(8)
                        .overflowWordBreak()
                        .rightAlign(),
                Lists.of("line1", "line2 line3", "line4"),
                Lists.of("   line1", "   line2\n   line3\n", "   line4"));
    }

    private void apply2(final ColumnConfig column,
                        final List<CharSequence> rows) {
        this.apply2(column, rows, rows);
    }

    private void apply2(final ColumnConfig column,
                        final List<CharSequence> before,
                        final List<CharSequence> expected) {
        assertEquals(strings(expected),
                strings(column.apply(before)),
                () -> column + " " + before);
    }

    private List<CharSequence> strings(final List<? super CharSequence> lines) {
        return lines.stream()
                .map(Object::toString)
                .map(CharSequences::quoteAndEscape)
                .collect(Collectors.toList());
    }

    // equals...........................................................................................................

    @Test
    public void testEmptyEquals() {
        this.checkEquals(ColumnConfig.empty(), ColumnConfig.empty());
    }

    @Test
    public void testDifferentMaxWidth() {
        this.checkNotEquals(ColumnConfig.empty().maxWidth(12345));
    }

    @Test
    public void testDifferentFunction() {
        final ColumnConfig column = ColumnConfig.empty().maxWidth(123);
        this.checkNotEquals(column.leftAlign(), column.rightAlign());
    }

    // toString.........................................................................................................

    @Test
    public void testToStringWidthLeft() {
        this.toStringAndCheck(ColumnConfig.empty()
                        .maxWidth(80)
                        .leftAlign(),
                "maxWidth=80 Left");
    }

    @Test
    public void testToStringWidthTruncateRight() {
        this.toStringAndCheck(ColumnConfig.empty()
                        .maxWidth(90)
                        .truncate()
                        .rightAlign(),
                "maxWidth=90 OverflowTruncate Right");
    }

    // disabled.........................................................................................................

    @Override
    public void testTypeNaming() {
    }

    // HashCodeEqualsTesting............................................................................................

    @Override
    public ColumnConfig createObject() {
        return this.createFunction();
    }

    // FunctionTesting..................................................................................................

    @Override
    public ColumnConfig createFunction() {
        return ColumnConfig.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ColumnConfig> type() {
        return ColumnConfig.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
