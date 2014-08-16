package uk.co.thinkofdeath.prismarine.network.protocol;

import uk.co.thinkofdeath.prismarine.network.protocol.login.EncryptionRequest;
import uk.co.thinkofdeath.prismarine.network.protocol.login.LoginDisconnect;
import uk.co.thinkofdeath.prismarine.network.protocol.login.LoginSuccess;
import uk.co.thinkofdeath.prismarine.network.protocol.login.SetInitialCompression;

public interface ILoginHandlerClientbound extends PacketHandler {
    void handle(EncryptionRequest encryptionRequest);

    void handle(LoginDisconnect loginDisconnect);

    void handle(LoginSuccess loginSuccess);

    void handle(SetInitialCompression setInitialCompression);
}
