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

public class CharSequenceBiFunctionAlignCenterTest extends CharSequenceBiFunctionAlignTestCase<CharSequenceBiFunctionAlignCenter> {

    @Test
    public void testApplyNotEmptyOnlyRightPadding() {
        this.applyAndCheck2("abc123", 7, "abc123 ");
    }

    @Test
    public void testApplyNotEmptyDifferentLeftAndRightPadding() {
        this.applyAndCheck2("abc123", 9, " abc123  ");
    }

    @Test
    public void testApplyNotEmptyEqualLeftAndRightPadding() {
        this.applyAndCheck2("abc123", 10, "  abc123  ");
    }

    @Test
    public void testApplyFullWidth() {
        this.applyAndCheck2("abc123", 6, "abc123");
    }

    @Override
    public CharSequenceBiFunctionAlignCenter createBiFunction() {
        return CharSequenceBiFunctionAlignCenter.INSTANCE;
    }

    @Override
    public Class<CharSequenceBiFunctionAlignCenter> type() {
        return CharSequenceBiFunctionAlignCenter.class;
    }

    @Override
    String align() {
        return "Center";
    }
}
