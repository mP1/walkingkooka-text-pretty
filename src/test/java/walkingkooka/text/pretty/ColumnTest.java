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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.util.FunctionTesting;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ColumnTest implements FunctionTesting<Column, List<CharSequence>, List<CharSequence>>,
        ClassTesting<Column>,
        ToStringTesting<Column> {

    private final static CharPredicate CHARACTER = CharPredicates.is('.');
    private final static int CHARACTER_COLUMN = 5;

    @Test
    public void testMaxWidthInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> Column.empty().maxWidth(-1));
    }

    @Test
    public void testMaxWidthInvalidFails2() {
        assertThrows(IllegalArgumentException.class, () -> Column.empty().maxWidth(0));
    }

    @Test
    public void testCenterWithoutMaxWidthFails() {
        assertThrows(IllegalArgumentException.class, () -> Column.empty().maxWidth(0));
    }


    @Test
    public void testMaxWidth() {
        final int width = 123;

        this.check(Column.empty()
                        .maxWidth(width),
                123);
    }

    @Test
    public void testCharacter() {
        this.check(Column.empty()
                        .maxWidth(80)
                        .character(CHARACTER, CHARACTER_COLUMN),
                80,
                TextPretty.character(CHARACTER, CHARACTER_COLUMN));
    }

    @Test
    public void testCenter() {
        this.check(Column.empty()
                        .maxWidth(80)
                        .center(),
                80,
                TextPretty.centerAlignment());
    }

    @Test
    public void testLeft() {
        this.check(Column.empty()
                        .maxWidth(81)
                        .left(),
                81,
                TextPretty.leftAlignment());
    }

    @Test
    public void testRight() {
        this.check(Column.empty()
                        .maxWidth(82)
                        .right(),
                82,
                TextPretty.rightAlignment());
    }

    @Test
    public void testTruncate() {
        this.check(Column.empty()
                        .maxWidth(83)
                        .truncate(),
                83,
                TextPretty.truncate());
    }

    @Test
    public void testLeftLeftReplacedAlign() {
        this.check(Column.empty()
                        .maxWidth(84)
                        .left()
                        .left(),
                84,
                TextPretty.leftAlignment());
    }

    @Test
    public void testLeftRightReplacedAlign() {
        this.check(Column.empty()
                        .maxWidth(85)
                        .left()
                        .right(),
                85,
                TextPretty.rightAlignment());
    }

    @Test
    public void testCenterRightReplacedAlign() {
        this.check(Column.empty()
                        .maxWidth(90)
                        .center()
                        .right(),
                90,
                TextPretty.rightAlignment());
    }

    @Test
    public void testCenterCharacter() {
        this.check(Column.empty()
                        .maxWidth(90)
                        .center()
                        .character(CHARACTER, CHARACTER_COLUMN),
                90,
                TextPretty.character(CHARACTER, CHARACTER_COLUMN));
    }

    @Test
    public void testCharacterCharacter() {
        this.check(Column.empty()
                        .maxWidth(90)
                        .character(CHARACTER, CHARACTER_COLUMN -1)
                        .character(CHARACTER, CHARACTER_COLUMN),
                90,
                TextPretty.character(CHARACTER, CHARACTER_COLUMN));
    }

    @Test
    public void testSetLeftMaxWidth() {
        final int width = 120;

        this.check(Column.empty()
                        .maxWidth(80)
                        .left()
                        .maxWidth(width),
                width,
                TextPretty.leftAlignment());
    }

    @Test
    public void testSetRightMaxWidthCenter() {
        final int width = 132;

        this.check(Column.empty()
                        .maxWidth(80)
                        .right()
                        .maxWidth(width)
                        .center(),
                width,
                TextPretty.centerAlignment());
    }

    private void check(final Column column,
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

        this.apply2(Column.empty(),
                lines,
                lines);
    }

    @Test
    public void testLeftApply() {
        this.apply2(Column.empty()
                        .maxWidth(80)
                        .left(),
                Lists.of("line 1", "  line 2"));
    }

    @Test
    public void testCharacterApply() {
        this.apply2(Column.empty()
                        .maxWidth(80)
                        .character(CHARACTER, CHARACTER_COLUMN),
                Lists.of("1.23", "12.34", "ignored"),
                Lists.of("    1.23", "   12.34", "ignored"));
    }

    @Test
    public void testRightApply() {
        this.apply2(Column.empty()
                        .maxWidth(10)
                        .right(),
                Lists.of("line1", "line 2"),
                Lists.of("     line1", "    line 2"));
    }

    @Test
    public void testTruncateCenterApply() {
        this.apply2(Column.empty()
                        .maxWidth(10)
                        .truncate()
                        .center(),
                Lists.of("line1", "line2=====///"),
                Lists.of("  line1   ", "line2====="));
    }

    private void apply2(final Column column,
                        final List<CharSequence> rows) {
        this.apply2(column, rows, rows);
    }

    private void apply2(final Column column,
                        final List<CharSequence> rows,
                        final List<CharSequence> expected) {
        assertEquals(strings(expected),
                strings(column.apply(rows)),
                () -> column + " " + rows);
    }

    private List<CharSequence> strings(final List<? super CharSequence> lines) {
        return lines.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    // toString.........................................................................................................

    @Test
    public void testToStringWidthLeft() {
        this.toStringAndCheck(Column.empty()
                        .maxWidth(80)
                        .left(),
                "maxWidth=80 Left");
    }

    @Test
    public void testToStringWidthTruncateRight() {
        this.toStringAndCheck(Column.empty()
                        .maxWidth(90)
                        .truncate()
                        .right(),
                "maxWidth=90 Truncate Right");
    }

    // disabled.........................................................................................................

    @Override
    public void testTypeNaming() {
    }

    // FunctionTesting..................................................................................................

    @Override
    public Column createFunction() {
        return Column.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<Column> type() {
        return Column.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
