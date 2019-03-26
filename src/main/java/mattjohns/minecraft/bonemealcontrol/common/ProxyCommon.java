package mattjohns.minecraft.bonemealcontrol.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import mattjohns.minecraft.common.log.Log;
import mattjohns.minecraft.common.network.NetworkChannel;
import mattjohns.minecraft.bonemealcontrol.server.ServerController;

/**
 * Common code for dedicated and integrated server programs. Handles loading of
 * client and server controllers.
 * 
 * If the mod is being run within a dedicated server program then a dummy client
 * controller is used otherwise it's a normal client controller.
 */
public abstract class ProxyCommon implements ProgramEventReceiver {
	// Same channel name is needed for both client and server. Each mod has
	// a separate namespace.
	protected static final String NetworkChannelName = "main";

	protected Log log;
	protected CommonConfiguration configuration;

	// shared between client and server
	protected NetworkChannel network;

	// There is always a server, regardless of whether dedicated or
	// integrated program running.
	protected ServerController server;

	// Client only exists if program is integrated server, otherwise
	// a dummy is used.
	protected ClientController client;

	@Override
	public void onProgramInitializeStart(FMLPreInitializationEvent event) {
		// attach custom log to game log
		if (event.getModLog() == null) {
			// should never be missing, use void log anyway
			log = Log.createVoid();
		}
		else {
			log = new Log(event.getModLog());
		}

		///// might need to load json later in case it references blocks from mods that aren't loaded yet,
		/// alternatively just load them lazily
		
		configuration = new CommonConfiguration(log);
		configuration.copyFromStorage();

		network = new NetworkChannel(NetworkChannelName);

		server = new ServerController(log, configuration, network);
		server.onProgramInitializeStart(event);

		client = clientCreate(log, configuration, network);
		client.onProgramInitializeStart(event);
	}

	@Override
	public void onProgramInitializeEnd(FMLInitializationEvent event) {
		server.onProgramInitializeEnd(event);
		client.onProgramInitializeEnd(event);
	}

	@Override
	public void onProgramInitializeAllModEnd(FMLPostInitializationEvent event) {
		server.onProgramInitializeAllModEnd(event);
		client.onProgramInitializeAllModEnd(event);
	}

	/**
	 * Return dummy controller if dedicated server program.
	 */
	protected abstract ClientController clientCreate(Log log, CommonConfiguration commonConfiguration,
			NetworkChannel network);

	public void onServerStart(FMLServerStartingEvent event) {
		server.onGameServerStart(event);
	}
}