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
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.list.Lists;

import java.util.List;

public final class TableNotEmptyListRowTest extends TableNotEmptyListTestCase<TableNotEmptyListRow, CharSequence> implements
        IteratorTesting {

    @Test
    public void testGet() {
        final TableNotEmptyListRow list = TableNotEmptyListRow.empty();

        list.setWidth(1);

        this.getAndCheck(
                list,
                0,
                TableNotEmptyListRow.MISSING
        );
    }

    @Test
    public void testSetAndGet() {
        final TableNotEmptyListRow list = TableNotEmptyListRow.empty();

        final CharSequence a = "A";
        list.setAuto(0, a);

        list.setWidth(1);

        this.getAndCheck(
                list,
                0,
                a
        );
    }

    @Test
    public void testSetAndGet2() {
        final TableNotEmptyListRow list = TableNotEmptyListRow.empty();

        final CharSequence a = "A";
        list.setAuto(0, a);

        final CharSequence b = "B";
        list.setAuto(1, b);

        list.setWidth(2);

        this.getAndCheck(
                list,
                0,
                a
        );

        this.getAndCheck(
                list,
                1,
                b
        );
    }

    @Test
    public void testSetAndGetMany() {
        final TableNotEmptyListRow list = TableNotEmptyListRow.empty();

        for (int i = 0; i < 10; i++) {
            list.setAuto(i, "column-" + i);
        }
        list.setWidth(10);

        for (int i = 0; i < 10; i++) {
            this.getAndCheck(
                    list,
                    i,
                    "column-" + i
            );
        }

        this.sizeAndCheck(
                list,
                10
        );
    }

    @Test
    public void testSetNullAndGet() {
        final TableNotEmptyListRow list = TableNotEmptyListRow.empty();

        list.setAuto(0, null);

        list.setWidth(1);

        this.getAndCheck(
                list,
                0,
                TableNotEmptyListRow.MISSING
        );
    }

    @Test
    public void testSetNullAndGetIncludesExpand() {
        final TableNotEmptyListRow list = TableNotEmptyListRow.empty();

        for (int i = 0; i < 10; i++) {
            list.setAuto(i, null);
        }

        list.setWidth(11);

        for (int i = 0; i < 10; i++) {
            this.getAndCheck(
                    list,
                    i,
                    TableNotEmptyListRow.MISSING
            );
        }
    }

    @Test
    public void testSetEmptyAndGetIncludesExpand() {
        final TableNotEmptyListRow list = TableNotEmptyListRow.empty();

        for (int i = 0; i < 10; i++) {
            list.setAuto(i, "");
        }

        list.setWidth(11);

        for (int i = 0; i < 10; i++) {
            this.getAndCheck(
                    list,
                    i,
                    TableNotEmptyListRow.MISSING
            );
        }
    }

    @Test
    public void testIterator() {
        final List<CharSequence> list = Lists.array();
        final TableNotEmptyListRow row = TableNotEmptyListRow.empty();

        for (int i = 0; i < 10; i++) {
            final CharSequence text = "column-" + i;
            list.add(text);
            row.setAuto(i, text);
        }

        row.setWidth(10);

        this.iterateAndCheck(
                row.iterator(),
                list.toArray(new CharSequence[0])
        );
    }

    @Override
    public TableNotEmptyListRow createList() {
        final TableNotEmptyListRow list = TableNotEmptyListRow.empty();
        list.setWidth(0);
        return list;
    }

    @Override
    CharSequence element(final int index) {
        return "column-text-" + index;
    }

    @Override
    public Class<TableNotEmptyListRow> type() {
        return TableNotEmptyListRow.class;
    }
}
