package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleComboBox extends Style<StyleComboBox> {
	protected StyleComboBox() {
	}

	public static StyleComboBox of() {
		return new StyleComboBox();
	}

	@Override
	protected StyleComboBox concreteCopy(Immutable<?> source) {
		return new StyleComboBox();
	}
}
