package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public class StyleContainer extends Style<StyleContainer> {

	protected StyleContainer() {
	}

	public static StyleContainer of() {
		return new StyleContainer();
	}

	@Override
	protected StyleContainer concreteCopy(Immutable<?> source) {
		return new StyleContainer();
	}
}
