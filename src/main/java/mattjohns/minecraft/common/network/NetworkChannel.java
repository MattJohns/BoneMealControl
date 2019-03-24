package mattjohns.minecraft.common.network;

import mattjohns.common.general.IdGenerator;
import mattjohns.minecraft.common.network.packet.PacketToClient;
import mattjohns.minecraft.common.network.packet.PacketHandlerFromServer;
import mattjohns.minecraft.common.network.packet.PacketToServer;
import mattjohns.minecraft.common.network.packet.PacketHandlerFromClient;
import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * A game network channel.
 * 
 * The same instance should be shared for client and server code.
 * 
 * Packets for both server and client need to be registered regardless of
 * whether dedicated or integrated server program.
 * 
 * The 'REPLY' type of IMessageHandler isn't used. Packets are sent and then the
 * other side sends its response as a normal packet, rather than using REPLY.
 * There's no reason except I wasn't sure how it worked exactly or if there were
 * hidden limitations compared to a normal reply packet.
 */
public class NetworkChannel extends SimpleNetworkWrapper {
	/**
	 * Each packet needs to be registered with an id.
	 */
	protected IdGenerator packetIdGenerator = IdGenerator.of();

	/**
	 * Channel name is automatically namespaced for the mod, so any name is
	 * fine.
	 */
	public NetworkChannel(String channelName) {
		super(channelName);
	}

	public <TPacket extends PacketToServer> void registerServer(Class<TPacket> packetClass,
			PacketHandlerFromClient<TPacket> handler) {
		registerMessage(handler, packetClass, packetToServerIdConsume(), Side.SERVER);
	}

	public <TPacket extends PacketToClient> void registerClient(Class<TPacket> packetClass,
			PacketHandlerFromServer<TPacket> handler) {
		registerMessage(handler, packetClass, packetToClientIdConsume(), Side.CLIENT);
	}

	public void sendToClient(PacketToClient packet, EntityPlayerMP player) {
		sendTo(packet, player);
	}

	public void sendToServer(PacketToServer packet) {
		super.sendToServer(packet);
	}

	// use same generator for both client and server but i don't think it
	// matters either way
	public int packetToServerIdConsume() {
		return packetIdGenerator.consume();
	}

	public int packetToClientIdConsume() {
		return packetIdGenerator.consume();
	}
}
