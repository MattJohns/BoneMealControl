package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapGroupBox extends StyleMap<StyleGroupBox, StyleMapGroupBox> {
	public final StyleMapContainer container;
	public final StyleMapLabel title;

	protected StyleMapGroupBox(ListImmutable<EnumStyle> keyList, ListImmutable<StyleGroupBox> valueList,
			StyleMapContainer container, StyleMapLabel title) {
		super(keyList, valueList);

		this.container = container;
		this.title = title;
	}

	public static StyleMapGroupBox of() {
		return new StyleMapGroupBox(ListImmutable.of(), ListImmutable.of(), StyleMapContainer.of(), StyleMapLabel.of());
	}

	@Override
	protected StyleMapGroupBox copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleGroupBox> valueList) {
		return new StyleMapGroupBox(keyList, valueList, container, title);
	}

	@Override
	protected StyleMapGroupBox concreteThis() {
		return this;
	}

	public StyleMapGroupBox withContainer(StyleMapContainer container) {
		return new StyleMapGroupBox(keyList, valueList, container, title);
	}

	public StyleMapGroupBox withTitle(StyleMapLabel title) {
		return new StyleMapGroupBox(keyList, valueList, container, title);
	}
}
