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

import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

public abstract class TableTestCase2<T> extends TableTestCase<T>
        implements HashCodeEqualsDefinedTesting2<T>,
        ToStringTesting<T> {

    final static CharSequence R0C0 = "r0c0";
    final static CharSequence R0C1 = "r0c1";
    final static CharSequence R0C2 = "r0c2";

    final static CharSequence R1C0 = "r1c0";
    final static CharSequence R1C1 = "r1c1";
    final static CharSequence R1C2 = "r1c2";

    final static CharSequence R2C0 = "r2c0";
    final static CharSequence R2C1 = "r2c1";
    final static CharSequence R2C2 = "r2c2";

    TableTestCase2() {
        super();
    }
}
