package mattjohns.minecraft.bonemealcontrol.common;

import mattjohns.common.immutable.Immutable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

/**
 * Main class for mod.
 *
 * The mod allows control bonemeal usage.
 */
@Mod(modid = CommonConfiguration.Internal.ModNameInternal, version = CommonConfiguration.Internal.Version)
public class ModEntryPoint extends Immutable<ModEntryPoint> implements ProgramEventReceiver {
	/**
	 * If program is dedicated server this gets instantiated with the server
	 * proxy only. If integrated server this gets instantiated with client proxy
	 * only.
	 */
	@SidedProxy(clientSide = CommonConfiguration.Internal.ProxyClassIntegrated, serverSide = CommonConfiguration.Internal.ProxyClassDedicated, modId = CommonConfiguration.Internal.ModNameInternal)
	public static ProxyCommon proxy;

	// a static reference to this mod, only used by Forge
	@Instance(CommonConfiguration.Internal.ModNameInternal)
	public static ModEntryPoint instance;

	@Override
	@EventHandler
	public void onProgramInitializeStart(FMLPreInitializationEvent event) {
		proxy.onProgramInitializeStart(event);
	}

	@Override
	@EventHandler
	public void onProgramInitializeEnd(FMLInitializationEvent event) {
		proxy.onProgramInitializeEnd(event);
	}

	@Override
	@EventHandler
	public void onProgramInitializeAllModEnd(FMLPostInitializationEvent event) {
		proxy.onProgramInitializeAllModEnd(event);
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		proxy.onServerStart(event);
	}
}
