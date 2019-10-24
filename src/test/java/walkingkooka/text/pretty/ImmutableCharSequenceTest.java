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
import walkingkooka.text.CharSequenceTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ImmutableCharSequenceTest extends TextPrettyTestCase<ImmutableCharSequence>
        implements CharSequenceTesting<ImmutableCharSequence> {

    private final static String TOSTRING = "abc123";

    @Test
    public void testSubSequence() {
        final ImmutableCharSequence chars = this.createCharSequence();
        assertSame(chars, chars.subSequence(0, TOSTRING.length()));
    }

    @Test
    public void testSubSequenceCreated() {
        final ImmutableCharSequence chars = this.createCharSequence();
        final CharSequence chars2 = chars.subSequence(1, 5);
        assertEquals(ImmutableCharSequence.class, chars2.getClass(), chars2.toString());
        this.toStringAndCheck(chars2, "bc12");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharSequence(), TOSTRING);
    }

    @Override
    public ImmutableCharSequence createCharSequence() {
        return ImmutableCharSequence.with(TOSTRING);
    }

    @Override
    public ImmutableCharSequence createObject() {
        return this.createCharSequence();
    }

    @Override
    public Class<ImmutableCharSequence> type() {
        return ImmutableCharSequence.class;
    }
}
