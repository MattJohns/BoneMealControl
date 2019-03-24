package mattjohns.minecraft.common.network.packet;

import java.util.Optional;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Receives packets sent from client to server. Runs server side.
 */
public class PacketHandlerFromClient<TPacket extends PacketToServer> extends PacketHandler<TPacket> {
	protected ReceiveConsumer<TPacket> receiveConsumer;

	public PacketHandlerFromClient(ReceiveConsumer<TPacket> receiveConsumer) {
		super();
		
		this.receiveConsumer = receiveConsumer;
	}

	protected EntityPlayerMP player(MessageContext context) {
		assert context != null;
		assert context.getServerHandler() != null;
		assert context.getServerHandler().player != null;

		return context.getServerHandler().player;
	}

	protected WorldServer world(MessageContext context) {
		assert context != null;
		assert player(context) != null;
		assert player(context).getServerWorld() != null;

		return player(context).getServerWorld();
	}

	@Override
	protected Optional<String> validate(TPacket packet, MessageContext context) {
		if (context.side != Side.SERVER) {
			return Optional.of("Packet received on wrong side:" + context.side);
		}

		if (!packet.isContentValid) {
			return Optional.of("Invalid content.");
		}

		EntityPlayerMP player = player(context);
		if (player == null) {
			return Optional.of("Server packet without player.");
		}

		WorldServer world = world(context);
		if (world == null) {
			return Optional.of("Server packet without world.");
		}

		return Optional.empty();
	}

	@Override
	protected void runInMainThread(TPacket packet, MessageContext context) {
		// consume on main server thread, this is currently a server network
		// thread
		world(context).addScheduledTask(new Runnable() {
			@Override
			public void run() {
				receiveConsumer.consume(packet, context, player(context), world(context));
			}
		});
	}

	/**
	 * All packets sent to server have a player associated who is the source of
	 * the packet.  The world is also extracted from the player object for convenience.
	 */
	public interface ReceiveConsumer<TPacket extends PacketToServer> {
		void consume(TPacket packet, MessageContext context, EntityPlayerMP player, WorldServer world);
	}
}