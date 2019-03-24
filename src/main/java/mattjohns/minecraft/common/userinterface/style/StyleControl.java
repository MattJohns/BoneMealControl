package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.userinterface.display.DisplayPadding;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.minecraft.common.userinterface.style.StyleBorder.BorderColor;

public class StyleControl

		extends Style<StyleControl> {

	public final StyleBackground background;
	public final StyleShadow shadow;
	public final StyleBorder border;
	public final StyleContent content;
	public final StyleInput input;

	protected StyleControl(StyleBackground background, StyleShadow shadow, StyleBorder border, StyleContent content,
			StyleInput input) {
		this.background = background;
		this.shadow = shadow;
		this.border = border;
		this.content = content;
		this.input = input;
	}

	public static StyleControl of() {
		return new StyleControl(StyleBackground.of(), StyleShadow.of(), StyleBorder.of(), StyleContent.of(),
				StyleInput.of());
	}

	@Override
	protected StyleControl concreteCopy(Immutable<?> source) {
		return new StyleControl(background, shadow, border, content, input);
	}

	public StyleControl withBackground(StyleBackground background) {
		return new StyleControl(background, shadow, border, content, input);
	}

	public StyleControl withShadow(StyleShadow shadow) {
		return new StyleControl(background, shadow, border, content, input);
	}

	public StyleControl withBorder(StyleBorder border) {
		return new StyleControl(background, shadow, border, content, input);
	}

	public StyleControl withContent(StyleContent content) {
		return new StyleControl(background, shadow, border, content, input);
	}

	public StyleControl withInput(StyleInput input) {
		return new StyleControl(background, shadow, border, content, input);
	}

	public StyleControl withBackgroundEnable(boolean enableIs) {
		return withBackground(background.withEnableIs(enableIs));
	}

	public StyleControl withBackgroundColor(DisplayColor color) {
		return withBackground(background.withColor(color));
	}

	public StyleControl withBackgroundGradient(StyleGradient gradient) {
		return withBackground(background.withGradient(gradient));
	}

	public StyleControl withShadowEnable(boolean enableIs) {
		return withShadow(shadow.withEnableIs(enableIs));
	}

	public StyleControl withShadowColor(DisplayColor color) {
		return withShadow(shadow.withColor(color));
	}

	public StyleControl withShadowOffset(DisplayPosition offset) {
		return withShadow(shadow.withOffset(offset));
	}

	public StyleControl withBorderEnable(boolean enableIs) {
		return withBorder(border.withEnableIs(enableIs));
	}

	public StyleControl withBorderColor(BorderColor color) {
		return withBorder(border.withColor(color));
	}

	public StyleControl withBorderSizeThick(DisplayPadding sizeThick) {
		return withBorder(border.withBorderSizeThick(sizeThick));
	}

	public StyleControl withBorderPadding(DisplayPadding padding) {
		return withBorder(border.withBorderPadding(padding));
	}

	public StyleControl withContentEnable(boolean enableIs) {
		return withContent(content.withEnableIs(enableIs));
	}

	public StyleControl withContentPadding(DisplayPadding padding) {
		return withContent(content.withContentPadding(padding));
	}

	public StyleControl withContentBackground(StyleBackground background) {
		return withContent(content.withContentBackground(background));
	}

	public StyleControl withContentBackgroundColor(DisplayColor color) {
		return withContent(content.withContentBackgroundColor(color));
	}

	public StyleControl withContentBackgroundPadding(DisplayPadding backgroundPadding) {
		return withContent(content.withContentBackgroundPadding(backgroundPadding));
	}

	public StyleControl withContentBackgroundEnable(boolean enableIs) {
		return withContent(content.withContentBackgroundEnable(enableIs));
	}

	public DisplayPadding contentPaddingEffective() {
		DisplayPadding contentPadding = content.paddingEffective();
		DisplayPadding borderPadding = border.paddingEffective();

		return contentPadding.largest(borderPadding);
	}
}