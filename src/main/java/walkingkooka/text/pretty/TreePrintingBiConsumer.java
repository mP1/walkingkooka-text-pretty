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

import walkingkooka.naming.Name;
import walkingkooka.naming.Path;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * A {@link BiConsumer} that accepts paths and prints a tree with indentation and relative paths.
 */
final class TreePrintingBiConsumer<P extends Path<P, N> & Comparable<P>,
        N extends Name & Comparable<N>>
        implements BiConsumer<Set<P>, IndentingPrinter> {

    /**
     * Creates a new {@link TreePrintingBiConsumer}
     */
    static <P extends Path<P, N> & Comparable<P>,
            N extends Name & Comparable<N>>
    TreePrintingBiConsumer<P, N> with(final TreePrinting<P, N> grouping) {
        Objects.requireNonNull(grouping, "grouping");

        return new TreePrintingBiConsumer(grouping);
    }

    private TreePrintingBiConsumer(final TreePrinting<P, N> grouping) {
        super();
        this.grouping = grouping;
    }

    @Override
    public void accept(final Set<P> paths,
                       final IndentingPrinter printer) {
        TreePrintingBiConsumerRequest.handle(paths, printer, this.grouping);
    }

    private TreePrinting<P, N> grouping;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.grouping.toString();
    }
}
