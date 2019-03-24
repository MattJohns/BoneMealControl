package mattjohns.minecraft.bonemealcontrol.common;

import mattjohns.minecraft.bonemealcontrol.client.ClientControllerIntegrated;
import mattjohns.minecraft.common.log.Log;
import mattjohns.minecraft.common.network.NetworkChannel;

/**
 * Program is integrated server so this has a real client controller.
 */
public class ProxyIntegrated extends ProxyCommon {
	@Override
	protected ClientController clientCreate(Log log, CommonConfiguration commonConfiguration,
			NetworkChannel network) {
		return new ClientControllerIntegrated(this.log, this.configuration, this.network);
	}
}