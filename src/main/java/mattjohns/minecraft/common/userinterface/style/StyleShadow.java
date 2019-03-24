package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;

public final class StyleShadow extends Style<StyleShadow>{
	public final boolean enableIs;
	public final DisplayColor color;
	public final DisplayPosition offset;

	protected StyleShadow(boolean enableIs, DisplayColor color, DisplayPosition offset) {
		this.enableIs = enableIs;
		this.color = color;
		this.offset = offset;
	}

	public static StyleShadow of() {
		return new StyleShadow(false, DisplayColor.of(), DisplayPosition.of());
	}
	
	@Override
	protected StyleShadow concreteCopy(Immutable<?> source) {
		return new StyleShadow(enableIs, color, offset);
	}

	public StyleShadow withEnableIs(boolean enableIs) {
		return new StyleShadow(enableIs, color, offset);
	}

	public StyleShadow withColor(DisplayColor color) {
		return new StyleShadow(enableIs, color, offset);
	}

	public StyleShadow withOffset(DisplayPosition offset) {
		return new StyleShadow(enableIs, color, offset);
	}
}
