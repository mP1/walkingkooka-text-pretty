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

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created for each individual request to {@link ColumnConfig#apply(List)} and includes some state such as capturing
 * the min/max width for a all rows in a column.
 */
final class ColumnConfigRequest {

    static ColumnConfigRequest with(final ColumnConfig config) {
        return new ColumnConfigRequest(config);
    }

    private ColumnConfigRequest(final ColumnConfig config) {
        super();
        this.config = config;
    }

    public List<CharSequence> apply(final List<CharSequence> rows) {
        final int minWidth = this.config.minWidth;
        final int maxWidth = this.config.maxWidth;

        this.width = minWidth == maxWidth ?
                minWidth :
                Math.max(minWidth, Math.min(maxWidth, rows.stream().mapToInt(CharSequence::length).max().orElse(0)));

        return rows.stream()
                .map(this::applyColumn)
                .collect(Collectors.toList());
    }

    private int width = 0;

    private CharSequence applyColumn(final CharSequence row) {
        final int width = this.width;

        CharSequence out = row;
        for (final BiFunction<CharSequence, Integer, CharSequence> function : this.config.functions) {
            out = function.apply(out, width);
        }

        return out;
    }

    final ColumnConfig config;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.config.toString();
    }
}
