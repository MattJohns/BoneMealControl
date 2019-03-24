package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public class StyleBackground extends Style<StyleBackground> {
	public final boolean enableIs;
	public final DisplayColor color;
	public final StyleGradient gradient;

	protected StyleBackground(boolean enableIs, DisplayColor color, StyleGradient gradient) {
		this.enableIs = enableIs;
		this.color = color;
		this.gradient = gradient;
	}

	public static StyleBackground of() {
		return new StyleBackground(false, DisplayColor.of(), StyleGradient.of());
	}

	@Override
	protected StyleBackground concreteCopy(Immutable<?> source) {
		return new StyleBackground(enableIs, color, gradient);
	}

	public StyleBackground withEnableIs(boolean enableIs) {
		return new StyleBackground(enableIs, color, gradient);
	}

	public StyleBackground withColor(DisplayColor color) {
		return new StyleBackground(enableIs, color, gradient);
	}

	public StyleBackground withGradient(StyleGradient gradient) {
		return new StyleBackground(enableIs, color, gradient);
	}
}
