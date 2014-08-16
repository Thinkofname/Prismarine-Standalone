package uk.co.thinkofdeath.prismarine.network.protocol.play;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.NullHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class PlayerTeleport implements Packet<NullHandler> {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private int flags;

    public PlayerTeleport(double x, double y, double z, float yaw, float pitch, int flags) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.flags = flags;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(yaw);
        buf.writeFloat(pitch);
        buf.writeByte(flags);
    }
}
