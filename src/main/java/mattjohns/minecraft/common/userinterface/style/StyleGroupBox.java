package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleGroupBox extends Style<StyleGroupBox> {
	public final StyleBorder frameBorder;

	protected StyleGroupBox(StyleBorder frameBorder) {
		this.frameBorder = frameBorder;
	}

	public static StyleGroupBox of() {
		return new StyleGroupBox(StyleBorder.of());
	}

	@Override
	protected StyleGroupBox concreteCopy(Immutable<?> source) {
		return new StyleGroupBox(frameBorder);
	}

	public StyleGroupBox withFrameBorder(StyleBorder frameBorder) {
		return new StyleGroupBox(frameBorder);
	}
}
