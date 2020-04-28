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
import walkingkooka.test.Fake;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Set;


public class FakeTreePrinting<P extends Path<P, N> & Comparable<P>, N extends Name & Comparable<N>> implements TreePrinting<P, N>, Fake {

    @Override
    public TreePrintingBranches branches(final P parent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void branchBegin(final List<N> names, final IndentingPrinter printer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void branchEnd(final List<N> names, final IndentingPrinter printer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void children(final Set<P> paths, final IndentingPrinter printer) {
        throw new UnsupportedOperationException();
    }
}
