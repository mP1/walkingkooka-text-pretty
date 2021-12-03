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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CharSequenceBiFunctionAlignCharacterTest extends CharSequenceBiFunctionAlignTestCase<CharSequenceBiFunctionAlignCharacter>
        implements HashCodeEqualsDefinedTesting2<CharSequenceBiFunctionAlignCharacter> {

    private final static int COLUMN = 5;

    // with.............................................................................................................

    @Test
    public void testWithNullCharPredicateFails() {
        assertThrows(NullPointerException.class, () -> CharSequenceBiFunctionAlignCharacter.with(null, COLUMN));
    }

    @Test
    public void testWithInvalidColumnFails() {
        assertThrows(IllegalArgumentException.class, () -> CharSequenceBiFunctionAlignCharacter.with(this.character(), -1));
    }

    // apply............................................................................................................

    @Test
    public void testInvalidColumnFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createBiFunction().apply("1", COLUMN));
    }

    @Test
    public void testInvalidColumnFails2() {
        assertThrows(IllegalArgumentException.class, () -> this.createBiFunction().apply("1", COLUMN - 1));
    }

    @Test
    public void testCharacterNotPresent() {
        this.applyAndCheck2("abc123", 7);
    }

    @Test
    public void testLeftPaddingNotRequired() {
        this.applyAndCheck2("abcde.123", 30);
    }

    @Test
    public void testLeftPaddingNotRequired2() {
        this.applyAndCheck2("abcdef.123", 30);
    }

    @Test
    public void testLeftPaddingNotRequired3() {
        this.applyAndCheck2("abc.123", 7);
    }

    @Test
    public void testLeftPadded() {
        this.applyAndCheck2("abc.12345", 20, "  abc.12345");
    }

    @Test
    public void testLeftPadded2() {
        this.applyAndCheck2("abc.12345", 30, "  abc.12345");
    }

    @Test
    public void testLeftPadded3() {
        this.applyAndCheck2("abcd.123", 30, " abcd.123");
    }

    @Test
    public void testLeftPadded4() {
        this.applyAndCheck2("abcd.1", 30, " abcd.1");
    }

    @Test
    public void testLeftPadded5() {
        this.applyAndCheck2("abcd.", 30, " abcd.");
    }

    @Test
    public void testLeftPaddingPartial() {
        this.applyAndCheck2("abc.1234", 9, " abc.1234");
    }

    @Test
    public void testLeftPaddingPartial2() {
        this.applyAndCheck2("ab.12345", 9, " ab.12345");
    }

    // equals.........................................................................................................

    @Test
    public void testDifferentCharPredicate() {
        this.checkNotEquals(CharSequenceBiFunctionAlignCharacter.with(CharPredicates.fake(), COLUMN));
    }

    @Test
    public void testDifferentColumn() {
        this.checkEquals(this.character(), this.character(), "CharPredicate doesnt implement equals");

        this.checkNotEquals(CharSequenceBiFunctionAlignCharacter.with(this.character(), COLUMN + 1));
    }

    // toString.........................................................................................................

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "'.' at " + COLUMN);
    }

    @Override
    public CharSequenceBiFunctionAlignCharacter createBiFunction() {
        return CharSequenceBiFunctionAlignCharacter.with(this.character(), COLUMN);
    }

    @Override
    public CharSequenceBiFunctionAlignCharacter createObject() {
        return this.createBiFunction();
    }

    private CharPredicate character() {
        return CharPredicates.is('.');
    }

    @Override
    public Class<CharSequenceBiFunctionAlignCharacter> type() {
        return CharSequenceBiFunctionAlignCharacter.class;
    }

    @Override
    String align() {
        return Character.class.getSimpleName();
    }
}
