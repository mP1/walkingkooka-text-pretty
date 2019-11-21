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
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.StringName;
import walkingkooka.naming.StringPath;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.IndentingPrinters;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TreePrintingBiConsumerRequestTest implements ClassTesting2<TreePrintingBiConsumerRequest<StringPath, StringName>> {

    @Test
    public void testWithNullPathsFails() {
        assertThrows(NullPointerException.class, () -> TreePrintingBiConsumerRequest.handle(null, this.printer(), this.treePrinting()));
    }

    @Test
    public void testWithNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> TreePrintingBiConsumerRequest.handle(this.paths(), null, this.treePrinting()));
    }

    @Test
    public void testWithNullTreePrintingFails() {
        assertThrows(NullPointerException.class, () -> TreePrintingBiConsumerRequest.handle(this.paths(), this.printer(), null));
    }

    private Set<StringPath> paths() {
        return Sets.empty();
    }

    private IndentingPrinter printer() {
        return IndentingPrinters.fake();
    }

    private TreePrinting<StringPath, StringName> treePrinting() {
        return new FakeTreePrinting<>();
    }
    
    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<TreePrintingBiConsumerRequest<StringPath, StringName>> type() {
        return Cast.to(TreePrintingBiConsumerRequest.class);
    }
}
