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

package uk.co.thinkofdeath.prismarine;

import uk.co.thinkofdeath.prismarine.block.BlockRegistry;
import uk.co.thinkofdeath.prismarine.item.ItemRegistry;

public abstract class Prismarine {

    private static Prismarine instance;

    private final BlockRegistry blockRegistry = new BlockRegistry();
    private final ItemRegistry itemRegistry = new ItemRegistry();

    protected Prismarine() {
        if (instance != null) {
            throw new RuntimeException("Only a single Prismarine instance can exist");
        }
        instance = this;
    }

    protected void init() {

    }

    public BlockRegistry getBlockRegistry() {
        return blockRegistry;
    }

    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    public static Prismarine getInstance() {
        return instance;
    }
}
