package mattjohns.minecraft.bonemealcontrol.server.grow;

import java.util.ArrayList;

import mattjohns.minecraft.common.log.Log;

public class GrowCustomFillList extends ArrayList<GrowCustomFillItem> {
	private static final long serialVersionUID = 1L;

	// returns error text, removes unfixable items from the list
	public ArrayList<String> validateAndFix() {
		ArrayList<String> result = new ArrayList<>();

		if (isEmpty()) {
			return result;
		}

		// go backwards in list so bad items can be removed
		for (int i = size() - 1; i >= 0; i--) {
			GrowCustomFillItem item = this.get(i);

			if (!item.validateAndFix(result)) {
				this.remove(i);
			}
		}

		return result;
	}
	
	public void blockStateCacheDerive(Log log) {
		for (GrowCustomFillItem item : this) {
			item.blockStateCacheDerive(log);
		}
	}
}
