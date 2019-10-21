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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.util.FunctionTesting;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ColumnsExtractorCharSequenceFunctionTest implements FunctionTesting<ColumnsExtractorCharSequenceFunction<BigInteger>,
        BigInteger,
        List<CharSequence>>,
        ToStringTesting<ColumnsExtractorCharSequenceFunction<BigInteger>> {

    @Test
    public void testWithNullTransformersFails() {
        assertThrows(NullPointerException.class, () -> ColumnsExtractorCharSequenceFunction.with(null));
    }

    @Test
    public void testWithEmptyTransformersFails() {
        assertThrows(IllegalArgumentException.class, () -> ColumnsExtractorCharSequenceFunction.with(Lists.empty()));
    }

    @Test
    public void testApply() {
        this.applyAndCheck(BigInteger.ONE, Lists.of("plus", "1"));
    }

    @Test
    public void testApply2() {
        this.applyAndCheck(BigInteger.valueOf(-12), Lists.of("minus", "12"));
    }

    @Test
    public void testApply3() {
        this.applyAndCheck(BigInteger.ZERO, Lists.of("zero", "0"));
    }

    @Test
    public void testToString() {
        final List<Function<BigInteger, CharSequence>> transformers = this.transformers();
        this.toStringAndCheck(ColumnsExtractorCharSequenceFunction.with(transformers), transformers.toString());
    }

    // type.............................................................................................................

    @Override
    public ColumnsExtractorCharSequenceFunction<BigInteger> createFunction() {
        return ColumnsExtractorCharSequenceFunction.with(this.transformers());
    }

    private List<Function<BigInteger, CharSequence>> transformers() {
        return Lists.of(this::sign, this::integer);
    }

    private String sign(final BigInteger value) {
        final int signum = value.signum();
        return 0 == signum ?
                "zero" :
                signum > 0 ?
                        "plus" :
                        "minus";
    }

    private String integer(final BigInteger value) {
        return String.valueOf(Math.abs(value.longValue()));
    }

    @Override
    public Class<ColumnsExtractorCharSequenceFunction<BigInteger>> type() {
        return Cast.to(ColumnsExtractorCharSequenceFunction.class);
    }
}
