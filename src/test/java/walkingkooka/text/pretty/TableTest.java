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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class TableTest implements ClassTesting2<Table> {

    // copyRowText......................................................................................................

    private final static String NULL = null;
    private final static List<CharSequence> NULL_LIST = null;

    @Test
    public void testCopyRowTextNulls() {
        this.copyRowTextAndCheck(
                list(NULL),
                NULL_LIST
        );
    }

    @Test
    public void testCopyRowTextEmpty() {
        this.copyRowTextAndCheck(
                list(""),
                NULL_LIST
        );
    }

    @Test
    public void testCopyRowTextNulls2() {
        this.copyRowTextAndCheck(
                list(null, null),
                NULL_LIST
        );
    }

    @Test
    public void testCopyRowTextEmpty2() {
        this.copyRowTextAndCheck(
                list("", ""),
                NULL_LIST
        );
    }

    @Test
    public void testCopyRowTextNullAndEmptyString() {
        this.copyRowTextAndCheck(
                list("", null, ""),
                NULL_LIST
        );
    }

    @Test
    public void testCopyRowText() {
        this.copyRowTextAndCheck(
                list("A", "B", "C"),
                list("A", "B", "C")
        );
    }

    @Test
    public void testCopyRowTextIncludesNull() {
        this.copyRowTextAndCheck(
                list(null, "B", "C"),
                list(null, "B", "C")
        );
    }

    @Test
    public void testCopyRowTextIncludesEmpty() {
        this.copyRowTextAndCheck(
                list("", "B", "C"),
                list(null, "B", "C")
        );
    }

    @Test
    public void testCopyRowTextTrimmedNull() {
        this.copyRowTextAndCheck(
                list("A", "B", "C", null),
                list("A", "B", "C")
        );
    }

    @Test
    public void testCopyRowTextTrimmedEmpty() {
        this.copyRowTextAndCheck(
                list("A", "B", "C", ""),
                list("A", "B", "C")
        );
    }

    @Test
    public void testCopyRowTextTrimmedNull2() {
        this.copyRowTextAndCheck(
                list("A", "B", "C", null, null),
                list("A", "B", "C")
        );
    }

    @Test
    public void testCopyRowTextTrimmedEmpty2() {
        this.copyRowTextAndCheck(
                list("A", "B", "C", "", ""),
                list("A", "B", "C")
        );
    }

    private <T> List<T> list(final T ... elements) {
        return Arrays.asList(elements);
    }

    private void copyRowTextAndCheck(final List<CharSequence> rowText,
                                     final List<CharSequence> expected) {
        this.checkEquals(
                expected,
                Table.copyRowText(rowText),
                () -> "rowText " + rowText
        );
    }

    // column...........................................................................................................
    
    @Test
    public void testSetColumn() {
        final Table table = Table.empty();

        // - x -
        // - y -
        // - z -
        final Table table2 = table.setColumn(1, Lists.of("x", "y", "z"));
        this.columnAndCheck(table2, 1, "x", "y", "z");
        this.check(table2);
    }

    @Test
    public void testSetColumn2() {
        final Table table = Table.empty();

        // - x -
        // - y -
        // - z -
        final Table table2 = table.setColumn(1, Lists.of("x", "y", "z"));
        this.check(table2);

        // - x -
        // - - -
        // - - -
        final Table table3 = table2.setColumn(1, Lists.of("x"));
        this.check(table3);
    }

    @Test
    public void testSetColumn3() {
        final Table table = Table.empty();

        // - x -
        // - y -
        // - z -
        final Table table2 = table.setColumn(1, Lists.of("x", "y", "z"))
                .setColumn(0, Lists.of("a"));
        this.columnAndCheck(table2, 1, "x", "y", "z");
        this.check(table2);

        // a x -
        // - - -
        // - - -
        final Table table3 = table2.setColumn(1, Lists.of("x"));
        this.columnAndCheck(table3, 1, "x");
        this.check(table3);
    }

    @Test
    public void testEmptySetColumnEmpty() {
        // - x -
        // - y -
        // - z -
        assertSame(
                Table.empty(),
                Table.empty()
                        .setColumn(1, Lists.empty())
        );
    }

    @Test
    public void testEmptySetColumn() {
        // - x -
        // - y -
        // - z -
        final Table table = Table.empty()
                .setColumn(1, Lists.of("x", "y", "z"));
        this.check(table);
    }

    @Test
    public void testSetColumnsEmptyColumn() {
        // - x -
        // - y -
        // - z -
        final Table table = Table.empty()
                .setColumn(1, Lists.of("x", "y", "z"))
                .setColumn(2, Lists.of("a"));
        this.check(table);
    }

    // row...........................................................................................................

    @Test
    public void testSetRow() {
        final Table table = Table.empty();

        // - - -
        // x y z
        // - - -
        final Table table2 = table.setRow(1, Lists.of("x", "y", "z"));
        this.rowAndCheck(table2, 1, "x", "y", "z");
        this.check(table2);
    }

    @Test
    public void testSetRow2() {
        final Table table = Table.empty();

        // - - -
        // x y z
        // - - -
        final Table table2 = table.setRow(1, Lists.of("x", "y", "z"));
        this.check(table2);

        // - - -
        // x - -
        // - - -
        final Table table3 = table2.setRow(1, Lists.of("x"));
        this.check(table3);
    }

    @Test
    public void testSetRow3() {
        final Table table = Table.empty();

        // - - -
        // x y z
        // - - -
        final Table table2 = table.setRow(1, Lists.of("x", "y", "z"))
                .setRow(0, Lists.of("a"));
        this.rowAndCheck(table2, 1, "x", "y", "z");
        this.check(table2);

        // - - -
        // x - -
        // - - -
        final Table table3 = table2.setRow(1, Lists.of("x"));
        this.rowAndCheck(table3, 1, "x");
        this.check(table3);
    }

    @Test
    public void testSetEmptyRow() {
        // - - -
        // x y z
        // - - -
        final Table table = Table.empty()
                .setRow(1, Lists.of("x", "y", "z"));
        this.check(table);
    }

    @Test
    public void testSetRowsEmptyRow() {
        // - - -
        // a - -
        // - - -
        final Table table = Table.empty()
                .setRow(1, Lists.of("x", "y", "z"))
                .setRow(2, Lists.of("a"));
        this.check(table);
    }

    // Collector........................................................................................................

    @Test
    public void testCollectColumn() {
        final List<CharSequence> column1 = Lists.of("c1a", "c1b", "c1c");
        final List<CharSequence> column2 = Lists.of("c2a", "c2b", "c2z");

        final Table table = Table.empty()
                .setColumn(1, column1)
                .setColumn(2, column2);

        this.checkEquals(table,
                Lists.of(column1, column2)
                        .stream()
                        .collect(Table.empty().collectColumn(1)));
    }

    @Test
    public void testCollectRow() {
        final List<CharSequence> row1 = Lists.of("r1a", "y", "z");
        final List<CharSequence> row2 = Lists.of("q", "r", "s");

        final Table table = Table.empty()
                .setRow(1, row1)
                .setRow(2, row2);

        this.checkEquals(table,
                Lists.of(row1, row2)
                .stream()
                .collect(Table.empty().collectRow(1)));
    }

    // helpers..........................................................................................................

    void check(final Table table) {
        if (table instanceof TableNotEmpty) {
            this.check((TableNotEmpty) table);
        }
    }

    void check(final TableNotEmpty table) {
        final int width = table.rows.stream()
                .filter(Objects::nonNull)
                .mapToInt(List::size)
                .max()
                .orElse(0);

        this.widthAndCheck(
                table,
                width
        );
        this.heightAndCheck(
                table,
                table.rows.size()
        );
    }

    void widthAndCheck(final Table table,
                       final int expected) {
        this.checkEquals(
                expected,
                table.width(),
                () -> "width of " + table
        );
    }

    void heightAndCheck(final Table table,
                        final int expected) {
        this.checkEquals(
                expected,
                table.height(),
                () -> "height of " + table
        );
    }


    private void columnAndCheck(final Table table,
                                final int column,
                                final CharSequence... text) {
        this.checkEquals(
                Lists.of(text),
                table.column(column),
                () -> "column " + column + " from " + table
        );
    }

    private void rowAndCheck(final Table table,
                             final int row,
                             final CharSequence... text) {
        this.checkEquals(Lists.of(text),
                table.row(row),
                () -> "row " + row + " from " + table);
    }

    // ClassTesting2....................................................................................................

    @Override
    public Class<Table> type() {
        return Table.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
