package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapPanel extends StyleMap<StylePanel, StyleMapPanel> {
	public final StyleMapContainer container;
	public final StyleMapContainer contentContainer;
	public final StyleMapScrollbar scrollbar;

	protected StyleMapPanel(ListImmutable<EnumStyle> keyList, ListImmutable<StylePanel> valueList,
			StyleMapContainer container, StyleMapContainer contentContainer, StyleMapScrollbar scrollbar) {
		super(keyList, valueList);

		this.container = container;
		this.contentContainer = contentContainer;
		this.scrollbar = scrollbar;
	}

	public static StyleMapPanel of() {
		return new StyleMapPanel(ListImmutable.of(), ListImmutable.of(), StyleMapContainer.of(), StyleMapContainer.of(),
				StyleMapScrollbar.of());
	}

	@Override
	protected StyleMapPanel copy(ListImmutable<EnumStyle> keyList, ListImmutable<StylePanel> valueList) {
		return new StyleMapPanel(keyList, valueList, container, contentContainer, scrollbar);
	}

	@Override
	protected StyleMapPanel concreteThis() {
		return this;
	}

	public StyleMapPanel withContainer(StyleMapContainer container) {
		return new StyleMapPanel(keyList, valueList, container, contentContainer, scrollbar);
	}

	public StyleMapPanel withContentContainer(StyleMapContainer contentContainer) {
		return new StyleMapPanel(keyList, valueList, container, contentContainer, scrollbar);
	}
	
	public StyleMapPanel withScrollbar(StyleMapScrollbar scrollbar) {
		return new StyleMapPanel(keyList, valueList, container, contentContainer, scrollbar);
	}
}
