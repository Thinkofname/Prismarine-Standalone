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

package uk.co.thinkofdeath.prismarine.network.protocol.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.co.thinkofdeath.prismarine.chat.ChatSerializer;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.ping.Ping;
import uk.co.thinkofdeath.prismarine.network.protocol.IStatusHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class StatusReponse implements Packet<IStatusHandlerClientbound> {

    private static final Gson gson = ChatSerializer.attach(new GsonBuilder())
            .create();
    private Ping response;

    public StatusReponse() {
    }

    public StatusReponse(Ping response) {
        this.response = response;
    }

    @Override
    public void read(MCByteBuf buf) {
        response = gson.fromJson(buf.readString(Short.MAX_VALUE), Ping.class);
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(gson.toJson(response));
    }

    @Override
    public void handle(IStatusHandlerClientbound handler) {
        handler.handle(this);
    }
}
