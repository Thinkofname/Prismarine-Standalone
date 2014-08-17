/*
 * Copyright 2014 Matthew Collins
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
 */

package uk.co.thinkofdeath.prismarine.util;

import java.util.function.ObjIntConsumer;

public class IntMap<T> {

    private static final Object NULL = new Object();
    private Object[] values;

    public IntMap() {
        this(16);
    }

    public IntMap(int initialSize) {
        if (initialSize < 16) {
            initialSize = 16;
        }
        values = new Object[initialSize];
    }

    public void put(int key, T value) {
        if (key >= values.length) {
            Object[] nv = new Object[values.length << 1];
            System.arraycopy(values, 0, nv, 0, values.length);
            values = nv;
        }
        if (value == null) {
            values[key] = NULL;
        } else {
            values[key] = value;
        }
    }

    @SuppressWarnings("unchecked")
    public T get(int key) {
        if (key >= values.length) {
            return null;
        }
        Object val = values[key];
        if (val == NULL || val == null) {
            return null;
        }
        return (T) val;
    }

    public boolean contains(int key) {
        return key < values.length && values[key] != null;
    }

    @SuppressWarnings("unchecked")
    public T remove(int key) {
        if (key >= values.length) {
            return null;
        }
        Object val = values[key];
        values[key] = null;
        if (val == NULL || val == null) {
            return null;
        }
        return (T) val;
    }

    public void forEach(ObjIntConsumer<T> func) {
        for (int i = 0; i < values.length; i++) {
            if (contains(i)) {
                func.accept(get(i), i);
            }
        }
    }
}
