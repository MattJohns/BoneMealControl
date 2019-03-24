package mattjohns.minecraft.common.network.packet;

import java.util.Optional;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * The network system requires each packet type to be registered to a handler.
 * The handler receives the packet (on client or server) and processes it.
 * 
 * This base handler will validate the packet and ignore it on failure.
 * Otherwise it will send it to receiveSchedule() where it should be scheduled
 * into the main thread.
 */
public abstract class PacketHandler<TPacket extends Packet> implements IMessageHandler<TPacket, IMessage> {
	protected PacketHandler() {
	}

	@Override
	public IMessage onMessage(TPacket message, MessageContext context) {
		if (validate(message, context).isPresent()) {
			/// log it
			return null;
		}

		runInMainThread(message, context);

		return null;
	}

	/**
	 * Returns text upon error.
	 */
	protected abstract Optional<String> validate(TPacket message, MessageContext context);

	/**
	 * Schedule the packet to be received in the main client or server thread.
	 */
	protected abstract void runInMainThread(TPacket message, MessageContext context);
}
