package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StylePanel extends Style<StylePanel> {
	protected StylePanel() {
	}

	public static StylePanel of() {
		return new StylePanel();
	}

	@Override
	protected StylePanel concreteCopy(Immutable<?> source) {
		return new StylePanel();
	}
}
