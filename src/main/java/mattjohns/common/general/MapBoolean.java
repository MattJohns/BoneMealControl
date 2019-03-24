package mattjohns.common.general;

import java.util.HashMap;

public abstract class MapBoolean<TKey> {
	protected HashMap<TKey, Boolean> hashMap;

	protected MapBoolean() {
		this.hashMap = new HashMap<>();
	}

	public void set(TKey name, boolean value) {
		hashMap.put(name, value);
	}

	public boolean get(TKey name) {
		return hashMap.get(name);
	}
}
