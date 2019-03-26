package mattjohns.minecraft.bonemealcontrol.server;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import mattjohns.minecraft.common.log.Log;
import mattjohns.minecraft.common.network.NetworkChannel;
import mattjohns.minecraft.common.system.SystemUtility;
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

		// all bone meal code is in here
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
		// only load the custom fill after all mods are loaded, otherwise you
		// might
		// not have access to all blocks from mods
		configuration.customFillCopyFromStorage();
	}

	public void onGameServerStart(FMLServerStartingEvent event) {
		consoleCommandRegister(event);
	}

	protected void consoleCommandRegister(FMLServerStartingEvent event) {
		event.registerServerCommand(new ConsoleCommandConfigurationReload());
	}

	public void configurationReload(EntityPlayerMP player) {
		if (player == null) {
			return;
		}

		configuration.copyFromStorage();

		configuration.customFillCopyFromStorage();

		log.informationConsole(player, CommonConfiguration.Internal.ModNameDisplay + " configuration reloaded.");
	}

	@SubscribeEvent
	public void onConsoleCommandConfigurationReload(ConsoleCommandEvent.ConfigurationReload event) {
		configurationReload(event.player);
	}

	// handle fluids separately (called before normal bone meal use event)
	@SubscribeEvent
	public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		World world = event.getWorld();
		if (world.isRemote) {
			return;
		}

		EntityPlayer player = event.getEntityPlayer();
		EnumHand hand = event.getHand();
		ItemStack itemStack = event.getItemStack();

		if (!grow.isBonemeal(itemStack)) {
			return;
		}

		// need to trace for fluid block
		RayTraceResult trace = SystemUtility.rayTrace(world, player, true);

		if (trace.typeOfHit != RayTraceResult.Type.BLOCK) {
			// not in reach of a block
			event.setCancellationResult(EnumActionResult.FAIL);
			return;
		}

		BlockPos targetPosition = trace.getBlockPos();
		EnumFacing targetFace = trace.sideHit;

		if (!player.canPlayerEdit(targetPosition.offset(targetFace), targetFace, itemStack)) {
			// block is read-only
			event.setCancellationResult(EnumActionResult.FAIL);
			return;
		}

		IBlockState targetBlockState = world.getBlockState(targetPosition);

		// ensure it's liquid
		if (!targetBlockState.getMaterial().isLiquid()) {
			event.setCancellationResult(EnumActionResult.FAIL);
			return;
		}

		if (ItemDye.applyBonemeal(itemStack, world, targetPosition, player, hand)) {
			event.setCancellationResult(EnumActionResult.SUCCESS);
		} else {
			event.setCancellationResult(EnumActionResult.PASS);
		}
	}

	// forge detected bone meal use, run custom code to handle it
	@SubscribeEvent
	public void onBonemealUse(BonemealEvent event) {
		World world = event.getWorld();

		if (world.isRemote) {
			return;
		}

		BlockPos targetPosition = event.getPos();
		IBlockState targetBlockState = event.getBlock();

		// attempt to grow target
		GrowResult growResult = grow.boneMealUse(world, targetPosition, targetBlockState);

		// vanilla and custom bone meal targets need to be handled in different
		// ways
		if (growResult.isVanilla) {
			// vanilla
			if (!growResult.isGrow) {
				// blocked by configuration so cancel to ensure bone meal isn't
				// used up
				event.setCanceled(true);

				// notify user
				if (configuration.elementGet(CommonConfiguration.ElementShowDisableMessageVanilla)) {
					EntityPlayer player = event.getEntityPlayer();
					log.informationConsole(player, "Bonemeal is disabled for this block.");
				}
			}
		} else {
			// custom
			if (growResult.isGrow) {
				// allowed by configuration and target was able to grow so use
				// up bone meal
				// from player hand
				event.setResult(Result.ALLOW);
			}
		}
	}

	// handle animals and other entities as targets
	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		World world = event.getWorld();
		if (world.isRemote) {
			return;
		}

		// ensure holding bone meal
		ItemStack itemStack = event.getItemStack();
		if (!grow.isBonemeal(itemStack)) {
			return;
		}

		// grow entity
		Entity entity = event.getTarget();
		if (!grow.boneMealUseEntity(world, entity)) {
			return;
		}

		// use up bone meal
		EntityPlayer player = event.getEntityPlayer();
		if (!player.capabilities.isCreativeMode) {
			itemStack.shrink(1);
		}

		grow.bonemealEffect(world, entity);

		event.setCancellationResult(EnumActionResult.SUCCESS);
	}
}
