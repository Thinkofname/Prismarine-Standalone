package uk.co.thinkofdeath.prismarine.network.protocol;

import uk.co.thinkofdeath.prismarine.network.protocol.login.EncryptionResponse;
import uk.co.thinkofdeath.prismarine.network.protocol.login.LoginStart;

public interface ILoginHandlerServerbound extends PacketHandler {
    void handle(EncryptionResponse encryptionResponse);

    void handle(LoginStart loginStart);
}
