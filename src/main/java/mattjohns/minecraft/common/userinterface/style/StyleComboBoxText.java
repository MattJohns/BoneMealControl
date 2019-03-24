package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleComboBoxText extends Style<StyleComboBoxText> {
	protected StyleComboBoxText() {
	}

	public static StyleComboBoxText of() {
		return new StyleComboBoxText();
	}

	@Override
	protected StyleComboBoxText concreteCopy(Immutable<?> source) {
		return new StyleComboBoxText();
	}
}
