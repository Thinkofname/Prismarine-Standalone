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

package uk.co.thinkofdeath.prismarine.registry;

import uk.co.thinkofdeath.prismarine.util.IntMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Registry<T> {

    protected IntMap<T> idMap = new IntMap<>(256);
    protected Map<T, Integer> reverseIdMap = new HashMap<>();
    private int nextId = 0;

    protected Registry() {

    }

    public T get(int id) {
        return idMap.get(id);
    }

    public int getId(T value) {
        return reverseIdMap.containsKey(value) ? reverseIdMap.get(value) : -1;
    }

    public void put(int id, T value) {
        idMap.put(id, value);
        reverseIdMap.put(value, id);
    }

    public void add(T value) {
        put(nextId++, value);
    }

    public void setNextId(int id) {
        nextId = id;
    }
}
