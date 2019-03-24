package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.userinterface.display.DisplayPadding;

public final class StyleBorder extends Style<StyleBorder> {
	public final boolean enableIs;
	public final BorderColor color;
	public final DisplayPadding borderSizeThick;
	public final DisplayPadding borderPadding;
	public final EnumCavity cavity;

	protected StyleBorder(boolean enableIs, BorderColor color, DisplayPadding borderSizeThick,
			DisplayPadding borderPadding, EnumCavity cavity) {
		this.enableIs = enableIs;
		this.color = color;
		this.borderSizeThick = borderSizeThick;
		this.borderPadding = borderPadding;
		this.cavity = cavity;
	}

	public static StyleBorder of() {
		return new StyleBorder(false, BorderColor.of(), DisplayPadding.None, DisplayPadding.None, EnumCavity.Normal);
	}

	@Override
	protected StyleBorder concreteCopy(Immutable<?> source) {
		return new StyleBorder(enableIs, color, borderSizeThick, borderPadding, cavity);
	}

	public StyleBorder withEnableIs(boolean enableIs) {
		return new StyleBorder(enableIs, color, borderSizeThick, borderPadding, cavity);
	}

	public StyleBorder withColor(BorderColor color) {
		return new StyleBorder(enableIs, color, borderSizeThick, borderPadding, cavity);
	}

	public StyleBorder withBorderSizeThick(DisplayPadding borderSizeThick) {
		return new StyleBorder(enableIs, color, borderSizeThick, borderPadding, cavity);
	}

	public StyleBorder withBorderPadding(DisplayPadding borderPadding) {
		return new StyleBorder(enableIs, color, borderSizeThick, borderPadding, cavity);
	}

	public StyleBorder withCavity(EnumCavity cavity) {
		return new StyleBorder(enableIs, color, borderSizeThick, borderPadding, cavity);
	}

	public DisplayPadding paddingEffective() {
		if (enableIs) {
			return borderPadding.add(borderSizeThick);
		}
		else {
			return DisplayPadding.None;
		}
	}

	public enum EnumCavity {
		Normal,
		Opposite,
		NormalOpposite,
		OppositeNormal,
	}

	public static final class BorderColor {
		public final DisplayColor edgeTopLeft;
		public final DisplayColor edgeBottomRight;
		public final DisplayColor cornerTopLeft;
		public final DisplayColor cornerBottomRight;
		public final DisplayColor cornerOther;

		protected BorderColor(DisplayColor edgeTopLeft, DisplayColor edgeBottomRight, DisplayColor cornerTopLeft,
				DisplayColor cornerBottomRight, DisplayColor cornerOther) {
			this.edgeTopLeft = edgeTopLeft;
			this.edgeBottomRight = edgeBottomRight;
			this.cornerTopLeft = cornerTopLeft;
			this.cornerBottomRight = cornerBottomRight;
			this.cornerOther = cornerOther;
		}

		public static BorderColor of() {
			return new BorderColor(DisplayColor.of(), DisplayColor.of(), DisplayColor.of(), DisplayColor.of(),
					DisplayColor.of());
		}

		public BorderColor withEdgeTopLeft(DisplayColor edgeTopLeft) {
			return new BorderColor(edgeTopLeft, edgeBottomRight, cornerTopLeft, cornerBottomRight, cornerOther);
		}

		public BorderColor withEdgeBottomRight(DisplayColor edgeBottomRight) {
			return new BorderColor(edgeTopLeft, edgeBottomRight, cornerTopLeft, cornerBottomRight, cornerOther);
		}

		public BorderColor withCornerTopLeft(DisplayColor cornerTopLeft) {
			return new BorderColor(edgeTopLeft, edgeBottomRight, cornerTopLeft, cornerBottomRight, cornerOther);
		}

		public BorderColor withCornerBottomRight(DisplayColor cornerBottomRight) {
			return new BorderColor(edgeTopLeft, edgeBottomRight, cornerTopLeft, cornerBottomRight, cornerOther);
		}

		public BorderColor withCornerOther(DisplayColor cornerOther) {
			return new BorderColor(edgeTopLeft, edgeBottomRight, cornerTopLeft, cornerBottomRight, cornerOther);
		}

		public BorderColor withCavityNegate() {
			return withEdgeTopLeft(edgeBottomRight).withEdgeBottomRight(edgeTopLeft)
					.withCornerTopLeft(cornerBottomRight)
					.withCornerBottomRight(cornerTopLeft);
		}

		public BorderColor with1Color(DisplayColor color) {
			return withEdgeTopLeft(color).withEdgeBottomRight(color)
					.withCornerTopLeft(color)
					.withCornerBottomRight(color)
					.withCornerOther(color);
		}

		public BorderColor with2Color(DisplayColor colorTopLeft, DisplayColor colorBottomRight) {
			return withEdgeTopLeft(colorTopLeft).withEdgeBottomRight(colorBottomRight)
					.withCornerTopLeft(colorTopLeft)
					.withCornerBottomRight(colorBottomRight)
					.withCornerOther(colorBottomRight);
		}
	}
}
