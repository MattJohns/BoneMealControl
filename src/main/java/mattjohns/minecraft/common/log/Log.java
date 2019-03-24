package mattjohns.minecraft.common.log;

import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

/**
 * Main log for the game.  Can be accessed during game pre-initialization and afterwards.
 */
public class Log {
	private boolean isVoid = true;
	private Logger forgeLogger;

	protected Log() {
		// void log
		this.isVoid = true;
		forgeLogger = null;
	}
	
	public Log(Logger forgeLogger) {
		this.isVoid = false;
		this.forgeLogger = forgeLogger;
	}

	public static Log createVoid() {
		// create an empty log
		return new Log();
	}
	
	public void information(String item) {
		if (isVoid) {
			return;
		}

		forgeLogger.info(item);
	}

	public void warning(String item) {
		if (isVoid) {
			return;
		}

		forgeLogger.warn(item);
	}

	public void error(String item) {
		if (isVoid) {
			return;
		}

		forgeLogger.error(item);
	}

	public void errorFatal(String item) {
		if (isVoid) {
			return;
		}

		forgeLogger.fatal(item);
	}

	/**
	 * Send text to client chat window.
	 * <p>  
	 * Call from server code only.
	 */
	public void informationConsole(EntityPlayer player, String item) {
		TextComponentString component = new TextComponentString(item);

		player.sendMessage(component);
	}
}