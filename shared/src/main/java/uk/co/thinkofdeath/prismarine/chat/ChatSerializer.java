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

package uk.co.thinkofdeath.prismarine.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChatSerializer {

    private static final Gson gson = attach(new GsonBuilder())
            .create();

    public static String toString(Component component) {
        return gson.toJson(component, Component.class);
    }

    public static Component fromString(String str) {
        return gson.fromJson(str, Component.class);
    }

    public static GsonBuilder attach(GsonBuilder builder) {
        builder.registerTypeAdapter(Component.class, new ComponentSerializer());
        return builder;
    }
}
