package mattjohns.common.immutable.list;

import mattjohns.common.immutable.Immutable;

public abstract class MapImmutable<TKey extends Comparable<TKey>, TValue, TConcrete extends MapImmutable<TKey, TValue, TConcrete>>
		extends Immutable<TConcrete> {

	protected final ListImmutable<TKey> keyList;
	protected final ListImmutable<TValue> valueList;

	protected MapImmutable(ListImmutable<TKey> keyList, ListImmutable<TValue> valueList) {
		this.keyList = keyList;
		this.valueList = valueList;

		assert this.keyList.size() == this.valueList.size();
	}

	protected abstract TConcrete copy(ListImmutable<TKey> keyList, ListImmutable<TValue> valueList);

	@Override
	protected TConcrete concreteCopy(Immutable<?> source) {
		return copy(keyList, valueList);
	}
	
	protected TConcrete withList(ListImmutable<TKey> keyList, ListImmutable<TValue> valueList) {
		return copy(keyList, valueList);
	}

	public boolean keyIs(TKey key) {
		for (TKey thisKey : keyList) {
			if (thisKey.compareTo(key) == 0) {
				return true;
			}
		}

		return false;
	}

	protected int keyIndex(TKey key) {
		assert keyIs(key);

		for (int i = 0; i < keyList.size(); i++) {
			if (keyList.get(i)
					.compareTo(key) == 0) {
				return i;
			}
		}

		assert false;
		return -1;
	}

	public TValue value(TKey key) {
		assert keyIs(key);
		
		return valueList.get(keyIndex(key));
	}

	public TConcrete withValueSet(TKey key, TValue value) {
		if (keyIs(key)) {
			// already exist, update it
			int keyIndex = keyIndex(key);

			return withList(keyList, valueList.withReplaceIndexWithItem(keyIndex, value));
		}
		else {
			// not there, add it
			return withList(keyList.withJoinItem(key), valueList.withJoinItem(value));
		}
	}

	public TConcrete withClear() {
		return withList(ListImmutable.of(), ListImmutable.of());
	}

	public int listSize() {
		return keyList.size();
	}

	public boolean isEmpty() {
		return keyList.isEmpty();
	}
}
