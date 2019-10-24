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

public abstract class CharSequenceBiFunctionOverflowTestCase<O extends CharSequenceBiFunctionOverflow> extends CharSequenceBiFunctionTestCase<O> {

    CharSequenceBiFunctionOverflowTestCase() {
        super();
    }

    @Test
    public final void testEmpty() {
        this.applyAndCheck("", 10, "");
    }

    @Test
    public final void testNotEmpty() {
        this.applyAndCheck("aa", 10, "aa");
    }

    @Test
    public final void testFull() {
        this.applyAndCheck("abcd", 4, "abcd");
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createBiFunction(), this.overflow());
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return "Overflow" + this.overflow();
    }

    abstract String overflow();
}
