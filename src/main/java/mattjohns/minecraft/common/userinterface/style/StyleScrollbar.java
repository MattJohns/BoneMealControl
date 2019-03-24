package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleScrollbar extends Style<StyleScrollbar> {
	protected StyleScrollbar() {
	}

	public static StyleScrollbar of() {
		return new StyleScrollbar();
	}

	@Override
	protected StyleScrollbar concreteCopy(Immutable<?> source) {
		return new StyleScrollbar();
	}
}
