package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapScrollbar extends StyleMap<StyleScrollbar, StyleMapScrollbar> {
	public final StyleMapContainer container;
	public final StyleMapButton button;

	protected StyleMapScrollbar(ListImmutable<EnumStyle> keyList, ListImmutable<StyleScrollbar> valueList,
			StyleMapContainer container, StyleMapButton button) {
		super(keyList, valueList);

		this.container = container;
		this.button = button;
	}

	public static StyleMapScrollbar of() {
		return new StyleMapScrollbar(ListImmutable.of(), ListImmutable.of(), StyleMapContainer.of(),
				StyleMapButton.of());
	}

	@Override
	protected StyleMapScrollbar copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleScrollbar> valueList) {
		return new StyleMapScrollbar(keyList, valueList, container, button);
	}

	@Override
	protected StyleMapScrollbar concreteThis() {
		return this;
	}

	public StyleMapScrollbar withContainer(StyleMapContainer container) {
		return new StyleMapScrollbar(keyList, valueList, container, button);
	}

	public StyleMapScrollbar withButton(StyleMapButton button) {
		return new StyleMapScrollbar(keyList, valueList, container, button);
	}
}
