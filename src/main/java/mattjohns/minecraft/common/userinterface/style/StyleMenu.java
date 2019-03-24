package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleMenu extends Style<StyleMenu> {
	protected StyleMenu() {
	}

	public static StyleMenu of() {
		return new StyleMenu();
	}

	@Override
	protected StyleMenu concreteCopy(Immutable<?> source) {
		return new StyleMenu();
	}
}
