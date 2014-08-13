package uk.co.thinkofdeath.micromc.network.protocol.login;

import uk.co.thinkofdeath.micromc.network.LoginHandler;
import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class EncryptionResponse implements Packet<LoginHandler> {

    private byte[] secretKey;
    private byte[] verifyToken;

    @Override
    public void read(MCByteBuf buf) {
        secretKey = buf.readByteArray(128);
        verifyToken = buf.readByteArray(128);
    }

    @Override
    public void handle(LoginHandler handler) {
        handler.handle(this);
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public byte[] getVerifyToken() {
        return verifyToken;
    }
}
