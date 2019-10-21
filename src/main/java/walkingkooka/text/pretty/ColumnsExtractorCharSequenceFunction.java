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

import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link Function} which transforms a value into a row of {@link CharSequence} using the provided individual
 * transformer functions.
 */
final class ColumnsExtractorCharSequenceFunction<T> implements Function<T, List<CharSequence>> {

    /**
     * Creates a new {@link ColumnsExtractorCharSequenceFunction}
     */
    static <T> ColumnsExtractorCharSequenceFunction<T> with(final List<Function<T, CharSequence>> transformers) {
        Objects.requireNonNull(transformers, "transformers");

        final List<Function<T, CharSequence>> copy = Lists.immutable(transformers);
        if (copy.isEmpty()) {
            throw new IllegalArgumentException("Expected at least one transformer, but got none");
        }
        return new ColumnsExtractorCharSequenceFunction<>(copy);
    }

    private ColumnsExtractorCharSequenceFunction(final List<Function<T, CharSequence>> transformers) {
        super();
        this.transformers = transformers;
    }

    @Override
    public List<CharSequence> apply(final T value) {
        return Lists.immutable(this.transformers.stream()
                .map(t -> t.apply(value))
                .collect(Collectors.toList()));
    }

    private final List<Function<T, CharSequence>> transformers;

    @Override
    public String toString() {
        return this.transformers.toString();
    }
}
