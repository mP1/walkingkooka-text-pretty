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
import walkingkooka.collect.list.ListTesting2;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.List;

public final class TreePrintingBiConsumerRequestListTest implements ClassTesting2<TreePrintingBiConsumerRequestList<StringName>>,
        ListTesting2<TreePrintingBiConsumerRequestList<StringName>, StringName> {

    @Test
    public void testAddFails() {
        this.addFails(this.createList(), Names.string("abc"));
    }

    @Test
    public void testAppend() {
        final TreePrintingBiConsumerRequestList<StringName> list = this.createList();

        final StringName name1 = Names.string("a1");
        list.append(name1);
        this.getAndCheck(list, 0, name1);
    }

    @Test
    public void testAppendThenRemoveFails() {
        final TreePrintingBiConsumerRequestList<StringName> list = this.createList();

        final StringName name1 = Names.string("a1");
        list.append(name1);
        this.removeFails(list, name1);
    }

    @Test
    public void testAppendAppend() {
        final TreePrintingBiConsumerRequestList<StringName> list = this.createList();

        final StringName name1 = Names.string("a1");
        list.append(name1);
        this.getAndCheck(list, 0, name1);

        final StringName name2 = Names.string("b2");
        list.append(name2);
        this.getAndCheck(list, 1, name2);
    }

    @Test
    public void testAppendAutoExpand() {
        final TreePrintingBiConsumerRequestList<StringName> list = this.createList();

        final List<StringName> arrayList = Lists.array();

        for (int i = 0; i < 10; i++) {
            final StringName name = Names.string("" + i);
            list.append(name);
            arrayList.add(name);

            this.getAndCheck(list, i, name);
        }

        this.checkEquals(arrayList, list);
    }

    @Test
    public void testAppendThenPop() {
        final TreePrintingBiConsumerRequestList<StringName> list = this.createList();

        final StringName name1 = Names.string("a1");
        list.append(name1);
        this.getAndCheck(list, 0, name1);

        list.pop();
        this.getFails(list, 0);
    }

    @Override
    public TreePrintingBiConsumerRequestList<StringName> createList() {
        return TreePrintingBiConsumerRequestList.empty();
    }

    @Override
    public Class<TreePrintingBiConsumerRequestList<StringName>> type() {
        return Cast.to(TreePrintingBiConsumerRequestList.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
