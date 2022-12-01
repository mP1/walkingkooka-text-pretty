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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TableCollectorTestCase2<C extends TableCollector> extends TableCollectorTestCase<C> implements ToStringTesting<C> {

    TableCollectorTestCase2() {
        super();
    }

    @Test
    public final void testInvalidStartFails() {
        assertThrows(IllegalArgumentException.class,
                () -> this.createCollector(Table.empty(), -1));
    }

    // Collector.........................................................................................................

    @Test
    public final void testParallelStreamFails() {
        assertThrows(UnsupportedOperationException.class,
                () -> {
                    Arrays.<List<CharSequence>>asList(Lists.of("a"), Lists.of("b"))
                            .stream()
                            .parallel()
                            .collect(this.createCollector(Table.empty(), 0));
                });
    }

    @Test
    public final void testNothingEmptyTable() {
        this.collectNothingAndCheck(Table.empty(), 0);
    }

    @Test
    public final void testNothingEmptyTable2() {
        this.collectNothingAndCheck(Table.empty(), 1);
    }

    @Test
    public final void testNothingNonEmptyTable() {
        this.collectNothingAndCheck(Table.empty().setCell(1, 2, "cell-a1"), 9);
    }

    private void collectNothingAndCheck(final Table table,
                                        final int start) {
        assertSame(table, Lists.<List<CharSequence>>empty()
                .stream()
                .collect(this.createCollector(table, start)));
    }

    // toString.........................................................................................................

    @Test
    public final void testToString() {
        final Table table = Table.empty();
        this.toStringAndCheck(this.createCollector(table, 2), table + " " + this.columnOrRow() + " Collector");
    }

    abstract String columnOrRow();

    // helper...........................................................................................................

    abstract C createCollector(final Table table, final int next);

    final void collectAndCheck(final Table table,
                               final int start,
                               final List<CharSequence>... data) {
        this.checkEquals(this.add(table, start, data),
                Lists.of(data)
                        .stream()
                        .collect(this.createCollector(table, start)));
    }

    abstract Table add(final Table table,
                       final int start,
                       final List<CharSequence>... data);
}
