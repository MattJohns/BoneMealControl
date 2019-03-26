package mattjohns.minecraft.bonemealcontrol.server.consolecommand;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

// force configuration for this mod to reload in-game
public class ConsoleCommandConfigurationReload extends CommandBase {
	@Override
	public String getName() {
		return "boneMealControlConfigurationReload";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/boneMealControlConfigurationReload";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) sender;

			MinecraftForge.EVENT_BUS.post(new ConsoleCommandEvent.ConfigurationReload(player));
		}
	}
}
