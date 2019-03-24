package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapListBox extends StyleMap<StyleListBox, StyleMapListBox> {
	public final StyleMapContainer container;
	public final StyleMapScrollbar scrollbar;

	protected StyleMapListBox(ListImmutable<EnumStyle> keyList, ListImmutable<StyleListBox> valueList,
			StyleMapContainer container, StyleMapScrollbar scrollbar) {
		super(keyList, valueList);

		this.container = container;
		this.scrollbar = scrollbar;
	}

	public static StyleMapListBox of() {
		return new StyleMapListBox(ListImmutable.of(), ListImmutable.of(), StyleMapContainer.of(),
				StyleMapScrollbar.of());
	}

	@Override
	protected StyleMapListBox copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleListBox> valueList) {
		return new StyleMapListBox(keyList, valueList, container, scrollbar);
	}

	@Override
	protected StyleMapListBox concreteThis() {
		return this;
	}

	public StyleMapListBox withContainer(StyleMapContainer container) {
		return new StyleMapListBox(keyList, valueList, container, scrollbar);
	}

	public StyleMapListBox withScrollbar(StyleMapScrollbar scrollbar) {
		return new StyleMapListBox(keyList, valueList, container, scrollbar);
	}
}
