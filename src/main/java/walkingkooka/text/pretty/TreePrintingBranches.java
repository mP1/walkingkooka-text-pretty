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

import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.naming.Name;
import walkingkooka.naming.Path;

import java.util.Set;

/**
 * Controls how the tree prints children sharing a common parent.
 */
public enum TreePrintingBranches {

    /**
     * Child {@link Path} will be printed according to {@link Comparable}.<br>
     * This is useful to print a tree of files where each branch has files & directories sorted lexically.
     */
    SORTED {
        @Override //
        <P extends Path<P, N> & Comparable<P>, N extends Name & Comparable<N>> Set<P> emptyChildrenSet() {
            return SortedSets.tree();
        }
    },

    /**
     * Child {@link Path} will be printed according to their original order.<br>
     * This would be useful to print a tree of classpath entries, where the actual order top to bottom matches the
     * entries original order.
     */
    VISITED {
        @Override <P extends Path<P, N> & Comparable<P>, N extends Name & Comparable<N>> Set<P> emptyChildrenSet() {
            return Sets.ordered();
        }
    };

    /**
     * Factory used to create a {@link Set} which will hold gathered {@link Path} that are actually children of a common parent.
     */
    abstract <P extends Path<P, N> & Comparable<P>, N extends Name & Comparable<N>> Set<P> emptyChildrenSet();
}
