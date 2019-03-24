package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapControl extends StyleMap<StyleControl, StyleMapControl> {
	protected StyleMapControl(ListImmutable<EnumStyle> keyList, ListImmutable<StyleControl> valueList) {
		super(keyList, valueList);
	}

	public static StyleMapControl of() {
		return new StyleMapControl(ListImmutable.of(), ListImmutable.of());
	}
	
	@Override
	protected StyleMapControl copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleControl> valueList) {
		return new StyleMapControl(keyList, valueList);
	}

	@Override
	protected StyleMapControl concreteThis() {
		return this;
	}
}
