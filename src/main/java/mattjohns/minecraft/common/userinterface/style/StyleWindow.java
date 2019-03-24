package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleWindow extends Style<StyleWindow> {
	protected StyleWindow() {
	}

	public static StyleWindow of() {
		return new StyleWindow();
	}

	@Override
	protected StyleWindow concreteCopy(Immutable<?> source) {
		return new StyleWindow();
	}
}
