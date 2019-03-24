package mattjohns.minecraft.bonemealcontrol.server;

import mattjohns.minecraft.common.log.Log;
import mattjohns.minecraft.common.network.NetworkChannel;
import mattjohns.minecraft.bonemealcontrol.common.ClientController;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;
import mattjohns.minecraft.bonemealcontrol.common.PacketFromServerReceiver;

/**
 * Dummy controller for dedicated server program.
 */
public class ClientControllerDedicated extends ClientController {
	public ClientControllerDedicated(Log log, CommonConfiguration commonConfiguration, NetworkChannel network) {
		super(log, commonConfiguration, network);
	}
	
	@Override
	protected PacketFromServerReceiver packetReceiverCreate() {
		// no client exists to receive packets from server so use dummy handler
		return new PacketFromServerReceiverVoid();
	}
}
