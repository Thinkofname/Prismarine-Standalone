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
