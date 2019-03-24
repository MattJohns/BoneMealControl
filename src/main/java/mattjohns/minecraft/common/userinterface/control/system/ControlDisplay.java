package mattjohns.minecraft.common.userinterface.control.system;

import java.util.Optional;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.minecraft.common.userinterface.device.DeviceDisplay;
import mattjohns.minecraft.common.userinterface.style.DisplayColor;
import mattjohns.minecraft.common.userinterface.style.StyleBackground;
import mattjohns.minecraft.common.userinterface.style.StyleBorder;
import mattjohns.minecraft.common.userinterface.style.StyleContent;
import mattjohns.minecraft.common.userinterface.style.StyleControl;
import mattjohns.minecraft.common.userinterface.style.StyleGradient;
import mattjohns.minecraft.common.userinterface.style.StyleShadow;
import mattjohns.minecraft.common.userinterface.style.StyleText;
import mattjohns.minecraft.common.userinterface.style.StyleText.EnumLayoutX;

public class ControlDisplay {
	protected DeviceDisplay display;

	protected ControlDisplay(DeviceDisplay display) {
		this.display = display;

		assert this.display != null;
	}

	public static ControlDisplay of(DeviceDisplay display) {
		return new ControlDisplay(display);
	}

	public DeviceDisplay display() {
		return display;
	}

	public void controlRender(DisplayBound controlBound, StyleControl controlStyle) {
		backgroundRender(controlBound, controlStyle.background);
		shadowRender(controlBound, controlStyle.shadow);
		contentBackgroundRender(controlBound, controlStyle.content);
		borderRender(controlBound, controlStyle.border);
	}

	public void backgroundRender(DisplayBound controlBound, StyleBackground style) {
		controlBackgroundRender(display, controlBound, style);
	}

	public void shadowRender(DisplayBound controlBound, StyleShadow shadowStyle) {
		if (!shadowStyle.enableIs) {
			return;
		}

		display.renderShadow(controlBound, shadowStyle.color, shadowStyle.offset);
	}

	public void contentBackgroundRender(DisplayBound controlBound, StyleContent contentStyle) {
		if (!contentStyle.enableIs) {
			return;
		}

		DisplayBound contentBackgroundBound = controlBound.withPadding(contentStyle.backgroundPadding);

		controlBackgroundRender(display, contentBackgroundBound, contentStyle.background);
	}

	protected void borderRenderCavityNormal(DisplayBound controlBound, StyleBorder borderStyle) {
		DisplayBound borderBound = controlBound.withPadding(borderStyle.borderPadding);

		display.renderBorder(borderBound, borderStyle.borderSizeThick, borderStyle.color);
	}

	protected void borderRenderCavityNormalOpposite(DisplayBound controlBound, StyleBorder borderStyle) {
		DisplayBound outerBound = controlBound.withPadding(borderStyle.borderPadding);

		// push inner frame in by outer frame thickness
		DisplayBound innerBound = outerBound.withPadding(borderStyle.borderSizeThick);

		// actual control border may not be enabled but the rest of the style
		// will be filled in
		StyleBorder outerStyle = borderStyle;

		// outer has opposite light cavity effect
		StyleBorder innerStyle = outerStyle.withColor(outerStyle.color.withCavityNegate());

		display.renderBorder(innerBound, innerStyle.borderSizeThick, innerStyle.color);
		display.renderBorder(outerBound, outerStyle.borderSizeThick, outerStyle.color);
	}

	public void borderRender(DisplayBound controlBound, StyleBorder borderStyle) {
		if (!borderStyle.enableIs) {
			return;
		}

		switch (borderStyle.cavity) {
		case Normal: {
			borderRenderCavityNormal(controlBound, borderStyle);
			break;
		}

		case Opposite: {
			borderRenderCavityNormal(controlBound, borderStyle.withColor(borderStyle.color.withCavityNegate()));
			break;
		}

		case NormalOpposite: {
			borderRenderCavityNormalOpposite(controlBound, borderStyle);
			break;
		}

		case OppositeNormal: {
			borderRenderCavityNormalOpposite(controlBound, borderStyle.withColor(borderStyle.color.withCavityNegate()));
			break;
		}
		default: {
			assert false;
			return;
		}
		}
	}

	public void textRender(String text, ControlFont<?> font, DisplayBound bound, StyleText textStyle) {
		DisplayColor color = textStyle.color;
		boolean wrapIs = (textStyle.layoutX == EnumLayoutX.WRAP);
		boolean shadowIs = !textStyle.isFlat;
		boolean trimIs = true;
		String fullText = textStyle.formatCode + text;

		display.renderText(fullText, font, bound, color, wrapIs, shadowIs, trimIs, Optional.empty());
	}

	public void renderGradient(DisplayBound bound, StyleGradient gradient) {
		if (!gradient.enableIs) {
			return;
		}

		DisplayColor color1 = gradient.color1;
		DisplayColor color2 = gradient.color2;

		display.renderGradient(bound, color1, color2, gradient.type);
	}

	public static void controlBackgroundRender(DeviceDisplay display, DisplayBound bound, StyleBackground background) {
		if (background.enableIs) {
			if (background.gradient.enableIs) {
				// gradient
				DisplayColor color1 = background.gradient.color1;
				DisplayColor color2 = background.gradient.color2;

				display.renderGradient(bound, color1, color2, background.gradient.type);
			}
			else {
				// normal
				DisplayColor color = background.color;

				// color = DisplayColor.of(0xff00bb00);//////

				display.renderRectangleSolid(bound, color);
			}
		}
	}

}
