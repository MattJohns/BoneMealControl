package mattjohns.minecraft.bonemealcontrol.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface ProgramEventReceiver {
	void onProgramInitializeStart(FMLPreInitializationEvent event);

	void onProgramInitializeEnd(FMLInitializationEvent event);

	void onProgramInitializeAllModEnd(FMLPostInitializationEvent event);
}
