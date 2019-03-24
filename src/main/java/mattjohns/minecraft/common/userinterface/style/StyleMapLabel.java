package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapLabel extends StyleMap<StyleLabel, StyleMapLabel> {
	public final StyleMapControl control;

	protected StyleMapLabel(ListImmutable<EnumStyle> keyList, ListImmutable<StyleLabel> valueList,
			StyleMapControl control) {
		super(keyList, valueList);

		this.control = control;
	}

	public static StyleMapLabel of() {
		return new StyleMapLabel(ListImmutable.of(), ListImmutable.of(), StyleMapControl.of());
	}

	@Override
	protected StyleMapLabel copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleLabel> valueList) {
		return new StyleMapLabel(keyList, valueList, control);
	}

	@Override
	protected StyleMapLabel concreteThis() {
		return this;
	}

	public StyleMapLabel withControl(StyleMapControl control) {
		return new StyleMapLabel(keyList, valueList, control);
	}
}
