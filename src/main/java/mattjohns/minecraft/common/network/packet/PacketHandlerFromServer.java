package mattjohns.minecraft.common.network.packet;

import java.util.Optional;

import net.minecraft.client.Minecraft;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Receives packets sent from server to client. Runs client side.
 */
public class PacketHandlerFromServer<TPacket extends PacketToClient> extends PacketHandler<TPacket> {
	protected Consumer<TPacket> receive;

	public PacketHandlerFromServer(Consumer<TPacket> receive) {
		super();

		this.receive = receive;
	}

	@Override
	protected Optional<String> validate(TPacket packet, MessageContext context) {
		if (context.side != Side.CLIENT) {
			Optional.of("Packet received on wrong side:" + context.side);
		}

		if (!packet.isContentValid) {
			Optional.of("Packet invalid" + toString());
		}

		return Optional.empty();
	}

	@Override
	protected void runInMainThread(TPacket packet, MessageContext context) {
		// consume on main client thread, this is currently a client network
		// thread
		Minecraft mc = Minecraft.getMinecraft();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				receive.accept(packet, context);
			}
		};

		mc.addScheduledTask(runnable);
	}

	public interface Consumer<TPacket extends PacketToClient> {
		void accept(TPacket packet, MessageContext context);
	}
}