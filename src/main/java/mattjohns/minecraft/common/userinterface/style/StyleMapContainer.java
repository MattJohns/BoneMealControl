package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public class StyleMapContainer extends StyleMap<StyleContainer, StyleMapContainer> {
	public final StyleMapControl control;
	
	protected StyleMapContainer(ListImmutable<EnumStyle> keyList, ListImmutable<StyleContainer> valueList, StyleMapControl control) {
		super(keyList, valueList);
		
		this.control = control;
	}

	public static StyleMapContainer of() {
		return new StyleMapContainer(ListImmutable.of(), ListImmutable.of(), StyleMapControl.of());
	}	
	
	@Override
	protected StyleMapContainer copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleContainer> valueList) {
		return new StyleMapContainer(keyList, valueList, control);
	}

	@Override
	protected StyleMapContainer concreteThis() {
		return this;
	}

	public StyleMapContainer withControl(StyleMapControl control) {
		return new StyleMapContainer(keyList, valueList, control);
	}
}
