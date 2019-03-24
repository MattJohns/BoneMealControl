package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleTextArea extends Style<StyleTextArea> {
	public final StyleText text;

	protected StyleTextArea(StyleText text) {
		this.text = text;
	}

	public static StyleTextArea of() {
		return new StyleTextArea(StyleText.of());
	}

	@Override
	protected StyleTextArea concreteCopy(Immutable<?> source) {
		return new StyleTextArea(text);
	}

	public StyleTextArea withText(StyleText text) {
		return new StyleTextArea(text);
	}
}
