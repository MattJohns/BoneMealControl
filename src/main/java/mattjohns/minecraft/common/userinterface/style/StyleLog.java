package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleLog extends Style<StyleLog> {
	protected StyleLog() {
	}

	public static StyleLog of() {
		return new StyleLog();
	}

	@Override
	protected StyleLog concreteCopy(Immutable<?> source) {
		return new StyleLog();
	}
}
