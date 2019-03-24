package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapComboBoxText extends StyleMap<StyleComboBoxText, StyleMapComboBoxText> {
	public final StyleMapComboBox comboBox;
	public final StyleMapLabel title;
	public final StyleMapLabel element;

	protected StyleMapComboBoxText(ListImmutable<EnumStyle> keyList, ListImmutable<StyleComboBoxText> valueList,
			StyleMapComboBox comboBox, StyleMapLabel title, StyleMapLabel element) {
		super(keyList, valueList);

		this.comboBox = comboBox;
		this.title = title;
		this.element = element;
	}

	public static StyleMapComboBoxText of() {
		return new StyleMapComboBoxText(ListImmutable.of(), ListImmutable.of(), StyleMapComboBox.of(),
				StyleMapLabel.of(), StyleMapLabel.of());
	}

	@Override
	protected StyleMapComboBoxText copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleComboBoxText> valueList) {
		return new StyleMapComboBoxText(keyList, valueList, comboBox, title, element);
	}

	@Override
	protected StyleMapComboBoxText concreteThis() {
		return this;
	}

	public StyleMapComboBoxText withComboBox(StyleMapComboBox comboBox) {
		return new StyleMapComboBoxText(keyList, valueList, comboBox, title, element);
	}

	public StyleMapComboBoxText withTitle(StyleMapLabel title) {
		return new StyleMapComboBoxText(keyList, valueList, comboBox, title, element);
	}

	public StyleMapComboBoxText withElement(StyleMapLabel element) {
		return new StyleMapComboBoxText(keyList, valueList, comboBox, title, element);
	}
}
