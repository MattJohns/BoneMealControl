package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapMenu extends StyleMap<StyleMenu, StyleMapMenu> {
	public final StyleMapComboBox comboBox;

	protected StyleMapMenu(ListImmutable<EnumStyle> keyList, ListImmutable<StyleMenu> valueList,
			StyleMapComboBox comboBox) {
		super(keyList, valueList);

		this.comboBox = comboBox;
	}

	public static StyleMapMenu of() {
		return new StyleMapMenu(ListImmutable.of(), ListImmutable.of(), StyleMapComboBox.of());
	}

	@Override
	protected StyleMapMenu copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleMenu> valueList) {
		return new StyleMapMenu(keyList, valueList, comboBox);
	}

	@Override
	protected StyleMapMenu concreteThis() {
		return this;
	}

	public StyleMapMenu withComboBox(StyleMapComboBox comboBox) {
		return new StyleMapMenu(keyList, valueList, comboBox);
	}
}
