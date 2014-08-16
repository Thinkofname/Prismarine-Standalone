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
