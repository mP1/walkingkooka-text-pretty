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
import walkingkooka.naming.PathSeparator;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Handles printing a tree of {@link Path paths} with support for grouping children for each branch or path.
 */
public interface TreePrinting<P extends Path<P, N> & Comparable<P>, N extends Name & Comparable<N>> {

    /**
     * {@see TreePrintingBranches}
     */
    TreePrintingBranches branches(final P parent);

    /**
     * Called for each parent before handling children, performing an action like printing the names and then {@link IndentingPrinter#indent()}
     */
    void branchBegin(final List<N> names,
                     final IndentingPrinter printer);

    /**
     * Called for each parent after handling children, performing an action like {@link IndentingPrinter#outdent()}
     */
    void branchEnd(final List<N> names,
                   final IndentingPrinter printer);

    /**
     * A {@link Set} holding the children for the just called parent. The children could be printed one per line or several across.
     */
    void children(final Set<P> paths,
                  final IndentingPrinter printer);

    /**
     * {@see TreePrintingBiConsumer}
     */
    default BiConsumer<Set<P>, IndentingPrinter> biConsumer() {
        return TreePrintingBiConsumer.with(this);
    }

    /**
     * Builds a path from the given {@link Name}.
     */
    default String toPath(final List<? extends Name> names,
                          final PathSeparator separator) {
        return names.stream()
                .filter(n -> false == n.value().isEmpty())
                .map(Name::toString)
                .collect(Collectors.joining(separator.string()));
    }
}
