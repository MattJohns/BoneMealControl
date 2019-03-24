package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapComboBox extends StyleMap<StyleComboBox, StyleMapComboBox> {
	public final StyleMapContainer container;
	public final StyleMapButton button;
	public final StyleMapListBox listBox;

	protected StyleMapComboBox(ListImmutable<EnumStyle> keyList, ListImmutable<StyleComboBox> valueList,
			StyleMapContainer container, StyleMapButton button, StyleMapListBox listBox) {
		super(keyList, valueList);

		this.container = container;
		this.button = button;
		this.listBox = listBox;
	}

	public static StyleMapComboBox of() {
		return new StyleMapComboBox(ListImmutable.of(), ListImmutable.of(), StyleMapContainer.of(), StyleMapButton.of(),
				StyleMapListBox.of());
	}

	@Override
	protected StyleMapComboBox copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleComboBox> valueList) {
		return new StyleMapComboBox(keyList, valueList, container, button, listBox);
	}

	@Override
	protected StyleMapComboBox concreteThis() {
		return this;
	}

	public StyleMapComboBox withContainer(StyleMapContainer container) {
		return new StyleMapComboBox(keyList, valueList, container, button, listBox);
	}

	public StyleMapComboBox withButton(StyleMapButton button) {
		return new StyleMapComboBox(keyList, valueList, container, button, listBox);
	}

	public StyleMapComboBox withListBox(StyleMapListBox listBox) {
		return new StyleMapComboBox(keyList, valueList, container, button, listBox);
	}
}
