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

package uk.co.thinkofdeath.prismarine.network.protocol.login;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.ILoginHandlerServerbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class EncryptionResponse implements Packet<ILoginHandlerServerbound> {

    private byte[] secretKey;
    private byte[] verifyToken;

    public EncryptionResponse() {
    }

    public EncryptionResponse(byte[] secretKey, byte[] verifyToken) {
        this.secretKey = secretKey;
        this.verifyToken = verifyToken;
    }

    @Override
    public void read(MCByteBuf buf) {
        secretKey = buf.readByteArray(128);
        verifyToken = buf.readByteArray(128);
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeByteArray(secretKey);
        buf.writeByteArray(verifyToken);
    }

    @Override
    public void handle(ILoginHandlerServerbound handler) {
        handler.handle(this);
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public byte[] getVerifyToken() {
        return verifyToken;
    }
}
