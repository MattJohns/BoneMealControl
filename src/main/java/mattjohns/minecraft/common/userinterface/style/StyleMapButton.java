package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapButton extends StyleMap<StyleButton, StyleMapButton> {
	public final StyleMapContainer container;
	public final StyleMapLabel title;
	
	protected StyleMapButton(ListImmutable<EnumStyle> keyList, ListImmutable<StyleButton> valueList, StyleMapContainer container, StyleMapLabel title) {
		super(keyList, valueList);
		
		this.container = container;
		this.title = title;
	}

	public static StyleMapButton of() {
		return new StyleMapButton(ListImmutable.of(), ListImmutable.of(), StyleMapContainer.of(), StyleMapLabel.of());
	}
	
	@Override
	protected StyleMapButton copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleButton> valueList) {
		return new StyleMapButton(keyList, valueList, container, title);
	}

	@Override
	protected StyleMapButton concreteThis() {
		return this;
	}

	public StyleMapButton withContainer(StyleMapContainer container) {
		return new StyleMapButton(keyList, valueList, container, title);
	}

	public StyleMapButton withTitle(StyleMapLabel title) {
		return new StyleMapButton(keyList, valueList, container, title);
	}
}
