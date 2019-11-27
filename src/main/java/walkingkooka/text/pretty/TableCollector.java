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

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A {@link Collector} that adds columns or rows and returns the final {@link Table}
 */
abstract class TableCollector implements Collector<List<CharSequence>, Table, Table> {

    TableCollector(final Table table,
                   final int next) {
        super();
        this.table = table;
        this.next = next;
    }

    @Override
    public final Supplier<Table> supplier() {
        return () -> this.table;
    }

    @Override
    public final BiConsumer<Table, List<CharSequence>> accumulator() {
        return this::accumulator;
    }

    /**
     * Sub classes will either set the column or row and advance the next counter.
     */
    abstract void accumulator(final Table table,
                              final List<CharSequence> data);

    @Override
    public final BinaryOperator<Table> combiner() {
        return this::combiner;
    }

    /**
     * Parallelism not supported, so should never be called.
     */
    private Table combiner(final Table left, final Table right) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " does not support parallel streams");
    }

    /**
     * Tables are immutable just return the last {@link Table}.
     */
    @Override
    public Function<Table, Table> finisher() {
        return this::finisher;
    }

    private final Table finisher(final Table table) {
        return this.table;
    }

    Table table;
    int next;

    @Override
    public final Set<Characteristics> characteristics() {
        return CHARACTERISTICS;
    }

    // not concurrent, but ordered.
    private final Set<Characteristics> CHARACTERISTICS = EnumSet.noneOf(Characteristics.class);

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.table + " " + this.columnOrRow() + " " + Collector.class.getSimpleName();
    }

    abstract String columnOrRow();
}
