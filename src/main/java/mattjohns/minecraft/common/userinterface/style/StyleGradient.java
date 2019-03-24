package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleGradient extends Style<StyleGradient> {
	public final boolean enableIs;
	public final EnumType type;
	public final DisplayColor color1;
	public final DisplayColor color2;

	protected StyleGradient(boolean enableIs, EnumType type, DisplayColor color1, DisplayColor color2) {
		this.enableIs = enableIs;
		this.type = type;
		this.color1 = color1;
		this.color2 = color2;
		
		assert this.color1 != null;
		assert this.color2 != null;
	}

	public static StyleGradient of() {
		return new StyleGradient(false, EnumType.TopToBottom, DisplayColor.of(), DisplayColor.of());
	}
	
	@Override
	protected StyleGradient concreteCopy(Immutable<?> source) {
		return new StyleGradient(enableIs, type, color1, color2);
	}

	public StyleGradient withEnableIs(boolean enableIs) {
		return new StyleGradient(enableIs, type, color1, color2);
	}

	public StyleGradient withType(EnumType type) {
		return new StyleGradient(enableIs, type, color1, color2);
	}

	public StyleGradient withColor1(DisplayColor color1) {
		return new StyleGradient(enableIs, type, color1, color2);
	}
	
	public StyleGradient withColor2(DisplayColor color2) {
		return new StyleGradient(enableIs, type, color1, color2);
	}

	public enum EnumType {
		TopToBottom,
	}
}
