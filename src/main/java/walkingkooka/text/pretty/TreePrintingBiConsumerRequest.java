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

import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Name;
import walkingkooka.naming.Path;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An individual request to print a {@link Set} of {@link Path paths}.
 */
final class TreePrintingBiConsumerRequest<P extends Path<P, N> & Comparable<P>,
        N extends Name & Comparable<N>> {

    static <P extends Path<P, N> & Comparable<P>,
            N extends Name & Comparable<N>> void handle(final Set<P> paths,
                                                        final IndentingPrinter printer,
                                                        final TreePrinting<P, N> printing) {
        Objects.requireNonNull(paths, "paths");
        Objects.requireNonNull(printer, "printer");
        Objects.requireNonNull(printing, "printing");

        final TreePrintingBiConsumerRequest<P, N> request = new TreePrintingBiConsumerRequest<>(printer, printing);
        request.buildParentToChildren(paths);
        request.printRoot();
    }

    private TreePrintingBiConsumerRequest(final IndentingPrinter printer,
                                          final TreePrinting<P, N> printing) {
        super();
        this.printer = printer;
        this.printing = printing;
    }

    private void buildParentToChildren(final Set<P> paths) {
        final Map<P, Set<P>> parentToChildren = this.parentToChildren;

        for (final P path : paths) {
            if(path.isRoot()) {
                this.root = path;
                continue;
            }

            P parent = path.parent().get();
            {
                Set<P> children = parentToChildren.get(parent);
                if (null == children) {
                    children = this.emptySet(parent);
                    parentToChildren.put(parent, children);
                }
                children.add(path);
            }

            for(;;) {
                if(parent.isRoot()) {
                    this.root = parent;
                    break;
                }
                final P parentOfParent = parent.parent().get();
                Set<P> children = parentToChildren.get(parentOfParent);
                if (null == children) {
                    children = this.emptySet(parentOfParent);
                    parentToChildren.put(parentOfParent, children);
                }
                children.add(parent);
                parent = parentOfParent;
            }
        }
    }

    /**
     * Begins the printing process starting with the root.
     */
    private void printRoot() {
        if(false == this.parentToChildren.isEmpty()) {
            this.printBranch(this.root, this.namesList());
        }
    }

    /**
     * Handles printing a branch printing branches first then children.
     */
    private void printBranch(final P parent,
                             final TreePrintingBiConsumerRequestList<N> names) {
        final Set<P> children = this.parentToChildren.get(parent);
        names.append(parent.name());

        final Set<P> leaves = this.emptySet(parent);
        for(final P child : children) {
            if(false == this.isBranch(child)) {
                leaves.add(child);
            }
        }

        if(leaves.isEmpty() && children.size() == 1) {
            for(final P child : children) {
                this.printBranch(child, names);
            }
        } else {
            final TreePrinting<P, N> printing = this.printing;
            final IndentingPrinter printer = this.printer;

            printing.branchBegin(names, printer);

            {
                final TreePrintingBiConsumerRequestList<N> names2 = this.namesList();
                for (final P child : children) {
                    if (false == leaves.contains(child)) {
                        this.printBranch(child, names2);
                    }
                }
            }

            printing.children(Sets.readOnly(leaves), printer);
            printing.branchEnd(names, printer);
        }

        names.pop();
    }

    /**
     * Tests if the given {@link Path} is a branch, contains at least 1 child.
     */
    private boolean isBranch(final P path) {
        return this.parentToChildren.containsKey(path);
    }

    /**
     * The root {@link Path}
     */
    private P root;

    /**
     * A {@link Map} of parents to their children and not descendants.
     */
    private final Map<P, Set<P>> parentToChildren = Maps.sorted();

    private final IndentingPrinter printer;
    private final TreePrinting<P, N> printing;

    /**
     * Creates an empty {@link Set}.
     */
    private Set<P> emptySet(final P parent) {
        return this.printing.branches(parent).emptyChildrenSet();
    }

    private TreePrintingBiConsumerRequestList<N> namesList() {
        return TreePrintingBiConsumerRequestList.empty();
    }
    

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.parentToChildren.toString();
    }
}
