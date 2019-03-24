package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleLabel extends Style<StyleLabel> {
	public final StyleText text;

	protected StyleLabel(StyleText text) {
		this.text = text;
	}

	public static StyleLabel of() {
		return new StyleLabel(StyleText.of());
	}

	@Override
	protected StyleLabel concreteCopy(Immutable<?> source) {
		return new StyleLabel(text);
	}

	public StyleLabel withText(StyleText text) {
		return new StyleLabel(text);
	}
}
