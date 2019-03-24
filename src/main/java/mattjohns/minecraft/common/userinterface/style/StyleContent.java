package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.userinterface.display.DisplayPadding;

public final class StyleContent extends Style<StyleContent>{
	public final boolean enableIs;
	public final DisplayPadding padding;
	public final StyleBackground background;
	public final DisplayPadding backgroundPadding;

	protected StyleContent(boolean enableIs, DisplayPadding padding, StyleBackground background,
			DisplayPadding backgroundPadding) {
		this.enableIs = enableIs;
		this.padding = padding;
		this.background = background;
		this.backgroundPadding = backgroundPadding;
	}

	public static StyleContent of() {
		return new StyleContent(false, DisplayPadding.None, StyleBackground.of(), DisplayPadding.None);
	}

	@Override
	protected StyleContent concreteCopy(Immutable<?> source) {
		return new StyleContent(enableIs, padding, background, backgroundPadding);
	}

	public StyleContent withEnableIs(boolean enableIs) {
		return new StyleContent(enableIs, padding, background, backgroundPadding);
	}

	public StyleContent withContentPadding(DisplayPadding padding) {
		return new StyleContent(enableIs, padding, background, backgroundPadding);
	}

	public StyleContent withContentBackground(StyleBackground background) {
		return new StyleContent(enableIs, padding, background, backgroundPadding);
	}

	public StyleContent withContentBackgroundPadding(DisplayPadding backgroundPadding) {
		return new StyleContent(enableIs, padding, background, backgroundPadding);
	}

	public StyleContent withContentBackgroundEnable(boolean backgroundEnableIs) {
		return new StyleContent(enableIs, padding, background.withEnableIs(backgroundEnableIs), backgroundPadding);
	}

	public StyleContent withContentBackgroundColor(DisplayColor backgroundColor) {
		return new StyleContent(enableIs, padding, background.withColor(backgroundColor), backgroundPadding);
	}

	public DisplayPadding paddingEffective() {
		if (enableIs) {
			return padding;
		}
		else {
			return DisplayPadding.None;
		}
	}

	public DisplayPadding backgroundPaddingEffective() {
		if (enableIs && background.enableIs) {
			return backgroundPadding;
		}
		else {
			return DisplayPadding.None;
		}
	}
}
