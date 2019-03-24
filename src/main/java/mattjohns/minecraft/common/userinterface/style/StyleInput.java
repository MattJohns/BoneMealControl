package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleInput extends Style<StyleInput> {
	public final double mouseHoldFrequencyInitial;
	public final double mouseHoldFrequency;

	protected StyleInput(double mouseHoldFrequencyInitial, double mouseHoldFrequency) {
		this.mouseHoldFrequencyInitial = mouseHoldFrequencyInitial;
		this.mouseHoldFrequency = mouseHoldFrequency;
	}

	public static StyleInput of() {
		return new StyleInput(0, 0);
	}

	@Override
	protected StyleInput concreteCopy(Immutable<?> source) {
		return new StyleInput(mouseHoldFrequencyInitial, mouseHoldFrequency);
	}

	public StyleInput withMouseHoldFrequencyInitial(double mouseHoldFrequencyInitial) {
		return new StyleInput(mouseHoldFrequencyInitial, mouseHoldFrequency);
	}

	public StyleInput withMouseHoldFrequency(double mouseHoldFrequency) {
		return new StyleInput(mouseHoldFrequencyInitial, mouseHoldFrequency);
	}
}
