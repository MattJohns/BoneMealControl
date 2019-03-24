package mattjohns.minecraft.bonemealcontrol.server.consolecommand;

import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ConsoleCommandEvent extends Event {
	// most console commands need the player object so they can write the result to console
	public EntityPlayerMP player;

	public ConsoleCommandEvent(EntityPlayerMP player) {
		this.player = player;
	}

	public static class ConfigurationReload extends ConsoleCommandEvent {
		public ConfigurationReload(EntityPlayerMP player) {
			super(player);
		}
	}
}