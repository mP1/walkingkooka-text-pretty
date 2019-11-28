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

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * An immutable representation of a table made up of columns. When used a {@link UnaryOperator} these can transform
 * the columns in a {@link Table}.
 */
public abstract class TableConfig implements UnaryOperator<Table> {

    /**
     * An empty {@link TableConfig}
     */
    static TableConfig empty() {
        return TableConfigEmpty.INSTANCE;
    }

    TableConfig() {
        super();
    }

    /**
     * Adds another {@link ColumnConfig} to this table.
     */
    public final TableConfig add(final ColumnConfig column) {
        Objects.requireNonNull(column, "column");

        return this.add0(column);
    }

    abstract TableConfig add0(final ColumnConfig column);

    // UnaryOperator....................................................................................................

    /**
     * Applies the {@link ColumnConfig} to the columns belonging to the given {@link Table} returning the end result.
     * Extra columns within the {@link Table} will not be modified. An empty {@link TableConfig} will thus return the original
     * {@link Table} unmodified.
     */
    @Override
    public final Table apply(final Table table) {
        Objects.requireNonNull(table, "table");
        return this.apply0(table);
    }

    abstract Table apply0(final Table table);

    @Override
    abstract public String toString();
}
