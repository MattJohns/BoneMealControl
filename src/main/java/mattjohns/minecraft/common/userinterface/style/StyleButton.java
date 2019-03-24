package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.userinterface.display.DisplaySize;

public final class StyleButton extends Style<StyleButton> {
	public final DisplaySize sizeStandard;

	protected StyleButton(DisplaySize sizeStandard) {
		this.sizeStandard = sizeStandard;
	}

	public static StyleButton of() {
		return new StyleButton(DisplaySize.Tiny);
	}

	@Override
	protected StyleButton concreteCopy(Immutable<?> source) {
		return new StyleButton(sizeStandard);
	}

	public StyleButton withSizeStandard(DisplaySize sizeStandard) {
		return new StyleButton(sizeStandard);
	}
}
