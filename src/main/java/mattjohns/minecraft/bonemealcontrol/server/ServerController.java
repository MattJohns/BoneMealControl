package mattjohns.minecraft.bonemealcontrol.server;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import mattjohns.minecraft.common.log.Log;
import mattjohns.minecraft.common.network.NetworkChannel;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;
import mattjohns.minecraft.bonemealcontrol.common.ProgramEventReceiver;
import mattjohns.minecraft.bonemealcontrol.server.consolecommand.ConsoleCommandConfigurationReload;
import mattjohns.minecraft.bonemealcontrol.server.consolecommand.ConsoleCommandEvent;
import mattjohns.minecraft.bonemealcontrol.server.grow.GrowController;
import mattjohns.minecraft.bonemealcontrol.server.grow.GrowResult;

// central controller for server
public class ServerController implements ProgramEventReceiver {
	protected Log log;
	protected CommonConfiguration configuration;
	protected NetworkChannel network;

	protected GrowController grow;

	public ServerController(Log log, CommonConfiguration configuration, NetworkChannel network) {
		this.log = log;
		this.configuration = configuration;
		this.network = network;

		// all bonemeal code is in here
		this.grow = new GrowController(configuration);
	}

	@Override
	public void onProgramInitializeStart(FMLPreInitializationEvent event) {
		// register to receive forge events
		MinecraftForge.EVENT_BUS.register(this);

		packetRegister();
	}

	public void packetRegister() {
		// no packets for this mod
	}

	@Override
	public void onProgramInitializeEnd(FMLInitializationEvent event) {
		log.information("Server initalized");
	}

	@Override
	public void onProgramInitializeAllModEnd(FMLPostInitializationEvent event) {
	}

	public void onGameServerStart(FMLServerStartingEvent event) {
		consoleCommandRegister(event);
	}

	protected void consoleCommandRegister(FMLServerStartingEvent event) {
		event.registerServerCommand(new ConsoleCommandConfigurationReload());
	}

	/**
	 * Called for both client and server when a dimension is loaded for the
	 * player.
	 */
	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event) {
	}

	/**
	 * Called on server after a player connects.
	 */
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent event) {
	}

	public void configurationReload(EntityPlayerMP player) {
		if (player == null) {
			return;
		}

		configuration.copyFromStorage();

		log.informationConsole(player, CommonConfiguration.Internal.ModNameDisplay + " configuration reloaded.");
	}

	@SubscribeEvent
	public void onConsoleCommandConfigurationReload(ConsoleCommandEvent.ConfigurationReload event) {
		configurationReload(event.player);
	}

	// forge detected bonemeal use, run custom code to handle it
	@SubscribeEvent
	public void onBonemealUse(BonemealEvent event) {
		World world = event.getWorld();

		if (world.isRemote) {
			return;
		}

		BlockPos targetPosition = event.getPos();
		IBlockState targetBlockState = event.getBlock();

		// attempt to grow target
		GrowResult growResult = grow.bonemealUse(world, targetPosition, targetBlockState);
		
		// vanilla and custom bonemeal targets need to be handled in different ways
		if (growResult.isVanilla) {
			// vanilla
			if (!growResult.isGrow) {
				// blocked by configuration so cancel to ensure bonemeal isn't used up
				event.setCanceled(true);
			}
		}
		else {
			// custom  
			if (growResult.isGrow) {
				// allowed by configuration and target was able to grow so use up bonemeal
				// from player hand
				event.setResult(Result.ALLOW);
			}
		}
	}
}
