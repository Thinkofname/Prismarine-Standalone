package uk.co.thinkofdeath.prismarine.network.protocol.login;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.NullHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

import java.security.PublicKey;

public class EncryptionRequest implements Packet<NullHandler> {

    private String serverID;
    private PublicKey publicKey;
    private byte[] verifyToken;

    public EncryptionRequest(String serverID, PublicKey publicKey, byte[] verifyToken) {
        this.serverID = serverID;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(serverID);
        buf.writeByteArray(publicKey.getEncoded());
        buf.writeByteArray(verifyToken);
    }
}
