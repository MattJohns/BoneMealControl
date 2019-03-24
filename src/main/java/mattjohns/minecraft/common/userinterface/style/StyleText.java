package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class StyleText extends Style<StyleText> {
	public final boolean enableIs;
	public final boolean isFlat;
	public final String formatCode;
	public final DisplayColor color;
	public final DisplayColor cursorColor;
	public final EnumLayoutX layoutX;
	public final EnumLayoutY layoutY;

	protected StyleText(boolean enableIs, DisplayColor color, boolean isFlat, String formatCode,
			DisplayColor cursorColor, EnumLayoutX layoutX, EnumLayoutY layoutY) {
		this.enableIs = enableIs;
		this.color = color;
		this.isFlat = isFlat;
		this.formatCode = formatCode;
		this.cursorColor = cursorColor;
		this.layoutX = layoutX;
		this.layoutY = layoutY;
	}

	public static StyleText of() {
		return new StyleText(false, DisplayColor.of(), false, TextFormatCode.None, DisplayColor.of(),
				EnumLayoutX.CENTER, EnumLayoutY.CENTER);
	}

	@Override
	protected StyleText concreteCopy(Immutable<?> source) {
		return new StyleText(enableIs, color, isFlat, formatCode, cursorColor, layoutX, layoutY);
	}

	public StyleText withEnableIs(boolean enableIs) {
		return new StyleText(enableIs, color, isFlat, formatCode, cursorColor, layoutX, layoutY);
	}

	public StyleText withColor(DisplayColor color) {
		return new StyleText(enableIs, color, isFlat, formatCode, cursorColor, layoutX, layoutY);
	}

	public StyleText withFlat(boolean isFlat) {
		return new StyleText(enableIs, color, isFlat, formatCode, cursorColor, layoutX, layoutY);
	}

	public StyleText withFormatCode(String formatCode) {
		return new StyleText(enableIs, color, isFlat, formatCode, cursorColor, layoutX, layoutY);
	}

	public StyleText withCursorColor(DisplayColor cursorColor) {
		return new StyleText(enableIs, color, isFlat, formatCode, cursorColor, layoutX, layoutY);
	}

	public StyleText withLayoutX(EnumLayoutX layoutX) {
		return new StyleText(enableIs, color, isFlat, formatCode, cursorColor, layoutX, layoutY);
	}

	public StyleText withLayoutY(EnumLayoutY layoutY) {
		return new StyleText(enableIs, color, isFlat, formatCode, cursorColor, layoutX, layoutY);
	}

	public enum EnumLayoutX {
		LEFT,
		CENTER,
		RIGHT,

		/// doesn't belong here, can have left or right with wrapping also
		WRAP,
	}

	public enum EnumLayoutY {
		TOP,
		CENTER,
		BOTTOM,
	}
}
