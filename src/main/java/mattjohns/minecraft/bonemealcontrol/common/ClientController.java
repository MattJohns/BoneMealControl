package mattjohns.minecraft.bonemealcontrol.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import mattjohns.minecraft.common.log.Log;
import mattjohns.minecraft.common.network.NetworkChannel;

/**
 * Need this base class for client controller so dedicated server can use a
 * dummy.
 */
public abstract class ClientController implements ProgramEventReceiver {
	protected Log log;
	protected CommonConfiguration commonConfiguration;
	protected NetworkChannel network;

	protected PacketFromServerReceiver packetReceiver;
	
	public ClientController(Log log, CommonConfiguration commonConfiguration, NetworkChannel network) {
		this.log = log;
		this.commonConfiguration = commonConfiguration;
		this.network = network;
	}

	@Override
	public void onProgramInitializeStart(FMLPreInitializationEvent event) {
		packetReceiver = packetReceiverCreate();

		packetRegister();
	}

	@Override
	public void onProgramInitializeEnd(FMLInitializationEvent event) {
	}

	@Override
	public void onProgramInitializeAllModEnd(FMLPostInitializationEvent event) {
	}

	/**
	 * Return a dummy / void receiver for dedicated.
	 */
	protected abstract PacketFromServerReceiver packetReceiverCreate();
	
	public void packetRegister() {
	}
}
