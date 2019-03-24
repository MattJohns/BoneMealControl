package mattjohns.minecraft.bonemealcontrol.common;

/**
 * Handle all packet types received from server.
 * 
 * This needs to be wrapped into a single interface so the dedicated server
 * program can use empty receive handlers, as it has no client to send packets to.
 * The integrated server program implements this interface and does proper
 * handling because the client is always present there.
 */
public interface PacketFromServerReceiver {
}
