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

import io.netty.handler.codec.DecoderException;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.ILoginHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionRequest implements Packet<ILoginHandlerClientbound> {

    private String serverID;
    private PublicKey publicKey;
    private byte[] verifyToken;

    public EncryptionRequest() {
    }

    public EncryptionRequest(String serverID, PublicKey publicKey, byte[] verifyToken) {
        this.serverID = serverID;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    @Override
    public void read(MCByteBuf buf) {
        serverID = buf.readString(40);
        try {
            publicKey = KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(buf.readByteArray(Short.MAX_VALUE)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new DecoderException(e);
        }
        verifyToken = buf.readByteArray(Short.MAX_VALUE);
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(serverID);
        buf.writeByteArray(publicKey.getEncoded());
        buf.writeByteArray(verifyToken);
    }

    @Override
    public void handle(ILoginHandlerClientbound handler) {
        handler.handle(this);
    }
}
