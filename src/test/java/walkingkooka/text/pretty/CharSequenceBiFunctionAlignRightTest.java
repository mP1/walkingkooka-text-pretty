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

public class CharSequenceBiFunctionAlignRightTest extends CharSequenceBiFunctionAlignTestCase<CharSequenceBiFunctionAlignRight> {

    @Test
    public void testApplyNotEmpty() {
        this.applyAndCheck2("abc123", 10, "    abc123");
    }

    @Test
    public void testApplyFullWidth() {
        this.applyAndCheck2("abc123", 6, "abc123");
    }

    @Override
    public CharSequenceBiFunctionAlignRight createBiFunction() {
        return CharSequenceBiFunctionAlignRight.INSTANCE;
    }

    @Override
    public Class<CharSequenceBiFunctionAlignRight> type() {
        return CharSequenceBiFunctionAlignRight.class;
    }

    @Override
    String align() {
        return "Right";
    }
}
