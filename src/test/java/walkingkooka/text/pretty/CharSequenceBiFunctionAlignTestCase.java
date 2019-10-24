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
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.util.BiFunctionTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class CharSequenceBiFunctionAlignTestCase<A extends CharSequenceBiFunctionAlign> extends CharSequenceBiFunctionTestCase<A> {

    CharSequenceBiFunctionAlignTestCase() {
        super();
    }

    @Test
    public void testEmpty() {
        this.applyAndCheck("", 10, "");
    }

    @Test
    public final void testGreaterThanWidthFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createBiFunction().apply("abc123", 5));
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createBiFunction(), this.align());
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return "Align" + this.align();
    }

    abstract String align();
}