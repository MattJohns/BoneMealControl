package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleListBox extends Style<StyleListBox> {
	public final boolean elementSelectIs;
	public final StyleBackground elementSelectBackground;

	protected StyleListBox(boolean elementSelectIs, StyleBackground elementSelectBackground) {
		this.elementSelectIs = elementSelectIs;
		this.elementSelectBackground = elementSelectBackground;
	}

	public static StyleListBox of() {
		return new StyleListBox(false, StyleBackground.of());
	}
	
	@Override
	protected StyleListBox concreteCopy(Immutable<?> source) {
		return new StyleListBox(elementSelectIs, elementSelectBackground);
	}

	public StyleListBox withElementSelectIs(boolean elementSelectIs) {
		return new StyleListBox(elementSelectIs, elementSelectBackground);
	}

	public StyleListBox withElementSelectBackground(StyleBackground elementSelectBackground) {
		return new StyleListBox(elementSelectIs, elementSelectBackground);
	}
}
