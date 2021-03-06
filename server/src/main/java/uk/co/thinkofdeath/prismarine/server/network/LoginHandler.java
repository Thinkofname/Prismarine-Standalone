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

package uk.co.thinkofdeath.prismarine.server.network;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import uk.co.thinkofdeath.prismarine.chat.TextComponent;
import uk.co.thinkofdeath.prismarine.log.LogUtil;
import uk.co.thinkofdeath.prismarine.network.NetworkHandler;
import uk.co.thinkofdeath.prismarine.network.PacketCodec;
import uk.co.thinkofdeath.prismarine.network.login.LoginResponse;
import uk.co.thinkofdeath.prismarine.network.login.Property;
import uk.co.thinkofdeath.prismarine.network.protocol.ILoginHandlerServerbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Protocol;
import uk.co.thinkofdeath.prismarine.network.protocol.login.EncryptionRequest;
import uk.co.thinkofdeath.prismarine.network.protocol.login.EncryptionResponse;
import uk.co.thinkofdeath.prismarine.network.protocol.login.LoginStart;
import uk.co.thinkofdeath.prismarine.network.protocol.login.LoginSuccess;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

public class LoginHandler implements ILoginHandlerServerbound {

    private static final Gson gson = new Gson();
    private static final Logger logger = LogUtil.get(LoginHandler.class);
    private NetworkHandler handler;
    private String username;
    private State currentState = State.START;
    private byte[] verifyToken;
    private String serverId = "";
    private UUID uuid;
    private Property[] properties;

    @Override
    public void setNetworkHandler(NetworkHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(LoginStart loginStart) {
        require(State.START);
        username = loginStart.getUsername();
        if (handler.getManager().isOnlineMode()) {
            currentState = State.WAITING_RESPONSE;
            verifyToken = new byte[16];
            try {
                SecureRandom.getInstanceStrong().nextBytes(verifyToken);
            } catch (NoSuchAlgorithmException e) {
                handler.disconnect(new TextComponent(e.getMessage()));
            }
            handler.sendPacket(new EncryptionRequest(
                    serverId,
                    handler.getManager().getNetworkKeyPair().getPublic(),
                    verifyToken
            ));
        } else {
            finishLogin();
            currentState = State.COMPLETE;
        }
    }

    @Override
    public void handle(EncryptionResponse encryptionResponse) {
        require(State.WAITING_RESPONSE);

        LoginResponse response;
        try {
            response = tryAuth(encryptionResponse);
            if (response == null) {
                handler.disconnect(new TextComponent("Failed to verify username against the session servers"));
                return;
            }
        } catch (IOException e) {
            handler.disconnect(new TextComponent(e.getMessage()));
            return;
        }

        String ustr = response.getId();
        uuid = UUID.fromString(ustr.substring(0, 8) + "-" + ustr.substring(8, 12) + "-" + ustr.substring(12, 16) + "-" + ustr.substring(16, 20) + "-" + ustr.substring(20, 32));
        username = response.getName();

        finishLogin();
        currentState = State.COMPLETE;
    }

    private LoginResponse tryAuth(EncryptionResponse encryptionResponse) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("sha1");

            byte[] testKey = decrypt(encryptionResponse.getVerifyToken());
            byte[] secretKeyBytes = decrypt(encryptionResponse.getSecretKey());

            if (!Arrays.equals(testKey, verifyToken)) {
                handler.disconnect(new TextComponent("Verify token incorrect"));
            }

            SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");

            digest.update(serverId.getBytes(StandardCharsets.UTF_8));
            digest.update(secretKeyBytes);
            digest.update(handler.getManager().getNetworkKeyPair().getPublic().getEncoded());

            handler.enableEncryption(secretKey);

            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/hasJoined?" +
                    "username=" + username
                    + "&serverId=" + new BigInteger(digest.digest()).toString(16));
            return gson.fromJson(
                    Resources.asCharSource(url, StandardCharsets.UTF_8).openBufferedStream(),
                    LoginResponse.class);
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | IllegalBlockSizeException
                | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] decrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        PrivateKey privateKey = handler.getManager().getNetworkKeyPair().getPrivate();
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    private void finishLogin() {
        if (uuid == null) {
            uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
        }
        if (properties == null) {
            properties = new Property[0];
        }
        handler.setCompression(256);

        handler.sendPacket(new LoginSuccess(
                uuid.toString(),
                username
        ));
        logger.info("User " + username + " as logged in with uuid " + uuid);

        handler.getChannel().pipeline().get(PacketCodec.class)
                .setProtocol(Protocol.PLAY);
        PlayHandler play = new PlayHandler(username, uuid, properties);
        handler.setHandler(play);
        play.join();
    }

    private void require(State state) {
        if (state != currentState) {
            throw new RuntimeException("Incorrect state");
        }
    }

    private static enum State {
        START,
        WAITING_RESPONSE,
        COMPLETE
    }
}
