package mattjohns.minecraft.common.userinterface.control.system;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.common.immutable.userinterface.font.Font;
import mattjohns.minecraft.common.userinterface.style.DisplayColor;
import mattjohns.minecraft.common.userinterface.style.StyleText;
import mattjohns.minecraft.common.userinterface.style.StyleText.EnumLayoutX;

public abstract class ControlFont<TConcrete extends ControlFont<TConcrete>> extends Immutable<TConcrete> {
	public ControlFont() {
	}

	public abstract int sizeY();

	public abstract int symbolSizeX(char text);

	public abstract int symbolSizeY(char text);

	public DisplaySize symbolSize(char text) {
		return DisplaySize.of(symbolSizeX(text), symbolSizeY(text));
	}

	public abstract int textSizeX(String text);

	public abstract int textSizeY(String text);

	public DisplaySize textSize(String text) {
		assert text.length() > 0;

		return DisplaySize.of(textSizeX(text), textSizeY(text));
	}

	public DisplaySize textSize(String text, StyleText textStyle, int wrapSizeX) {
		int x;
		int y;

		assert text.length() > 0;

		if (textStyle.layoutX == EnumLayoutX.WRAP) {
			x = Math.max(wrapSizeX, wrapMinimumSizeX());

			y = wrapSizeY(text, x);

			return DisplaySize.of(x, y);
		}
		else {
			return textSize(text);
		}
	}

	public abstract int wrapMinimumSizeX();

	public abstract int wrapSizeY(String text, int wrapSizeX);

	/**
	 * Relative to top left of content.
	 */
	public DisplayPosition textPositionDerive(DisplaySize textSize, StyleText textStyle, DisplaySize contentSize) {
		int textPositionX;
		switch (textStyle.layoutX) {
		default:
		case LEFT: {
			textPositionX = 0;
			break;
		}
		case CENTER: {
			int contentSizeX = contentSize.x;
			if (contentSizeX < 1) {
				contentSizeX = 1;
			}

			int textContentPositionX = (contentSizeX / 2) - (textSize.x / 2);
			if (textContentPositionX < 0) {
				// label is too small for text, just allow it to render outside
				// label bounds
			}

			textPositionX = textContentPositionX;
			break;
		}
		case RIGHT: {
			textPositionX = (contentSize.x - textSize.x);
			break;
		}
		case WRAP: {
			textPositionX = 0;
			break;
		}
		}

		int textPositionY;
		switch (textStyle.layoutY) {
		default:
		case TOP: {
			textPositionY = 0;
			break;
		}
		case CENTER: {
			int contentSizeY = contentSize.y;
			if (contentSizeY < 1) {
				contentSizeY = 1;
			}

			int textContentPositionY = (contentSizeY / 2) - (textSize.y / 2);

			textPositionY = textContentPositionY;
			break;
		}
		case BOTTOM: {
			textPositionY = (contentSize.y - textSize.y);
			break;
		}
		}

		return DisplayPosition.of(textPositionX, textPositionY);
	}

	public DisplayBound textBoundDerive(String text, StyleText textStyle, DisplaySize insideSize) {
		int wrapSizeX = insideSize.x;
		if (wrapSizeX < 1) {
			/// not sure if needed
			wrapSizeX = 1;
		}

		DisplaySize textSize = textSize(text, textStyle, wrapSizeX);

		DisplayPosition textPositionInside = textPositionDerive(textSize, textStyle, insideSize);

		return DisplayBound.of(textPositionInside, textSize);
	}

	public abstract String textTruncate(String text, int sizeX);

	public abstract void textRenderWrap(String text, DisplayBound bound, DisplayColor color);

	public abstract void textRender(String text, DisplayBound bound, DisplayColor color, boolean shadowIs);

	public abstract Font renderModelCreateFont();
}
