package mattjohns.minecraft.bonemealcontrol.client.network.dialog;

import mattjohns.minecraft.bonemealcontrol.client.network.ClientNetworkController;
import mattjohns.minecraft.common.network.DialogAbstract;

/**
 * Concrete dialogs need the channel so they can send their inner
 * requests.
 */
public abstract class Dialog extends DialogAbstract {
	protected ClientNetworkController network;

	protected Dialog(int id, ClientNetworkController network) {
		super(id);

		this.network = network;
	}

	@Override
	protected void end() {
		super.end();

		// notify network so dialog can be removed from waiting list
		network.dialogEnd(id);
	}
}