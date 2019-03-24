package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapTextArea extends StyleMap<StyleTextArea, StyleMapTextArea> {
	public final StyleMapContainer container;
	public final StyleMapScrollbar scrollbar;

	protected StyleMapTextArea(ListImmutable<EnumStyle> keyList, ListImmutable<StyleTextArea> valueList,
			StyleMapContainer container, StyleMapScrollbar scrollbar) {
		super(keyList, valueList);

		this.container = container;
		this.scrollbar = scrollbar;
	}

	public static StyleMapTextArea of() {
		return new StyleMapTextArea(ListImmutable.of(), ListImmutable.of(), StyleMapContainer.of(),
				StyleMapScrollbar.of());
	}

	@Override
	protected StyleMapTextArea copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleTextArea> valueList) {
		return new StyleMapTextArea(keyList, valueList, container, scrollbar);
	}

	@Override
	protected StyleMapTextArea concreteThis() {
		return this;
	}

	public StyleMapTextArea withContainer(StyleMapContainer container) {
		return new StyleMapTextArea(keyList, valueList, container, scrollbar);
	}

	public StyleMapTextArea withScrollbar(StyleMapScrollbar scrollbar) {
		return new StyleMapTextArea(keyList, valueList, container, scrollbar);
	}
}
