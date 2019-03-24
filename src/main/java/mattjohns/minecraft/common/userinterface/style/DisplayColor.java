package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class DisplayColor extends Immutable<DisplayColor> {
	protected static final int ComponentSizeMaximum = 255;

	public final java.awt.Color colorInternal;

	protected DisplayColor(java.awt.Color colorInternal) {
		this.colorInternal = colorInternal;
	}

	public static DisplayColor of() {
		// use a debug color so it stands out if not initialized
		return new DisplayColor(java.awt.Color.MAGENTA);
	}

	public static DisplayColor of(java.awt.Color colorInternal) {
		// use a debug color so it stands out if not initialized
		return new DisplayColor(colorInternal);
	}

	public static DisplayColor of(int integerArgb) {
		// use a debug color so it stands out if not initialized
		return new DisplayColor(new java.awt.Color(integerArgb));
	}

	@Override
	protected DisplayColor concreteCopy(Immutable<?> source) {
		return new DisplayColor(colorInternal);
	}

	protected DisplayColor withColorInternal(java.awt.Color colorInternal) {
		return new DisplayColor(colorInternal);
	}

	public DisplayColor withComponentTranslateNoAlpha(int delta) {
		return withComponentTranslate(delta, delta, delta);
	}

	public DisplayColor withComponentTranslate(int red, int green, int blue) {
		return withComponentTranslate(red, green, blue, 0);
	}

	public DisplayColor withComponentTranslate(int redDelta, int greenDelta, int blueDelta, int alphaDelta) {
		int red = componentTranslate(componentRed(), redDelta);
		int green = componentTranslate(componentGreen(), greenDelta);
		int blue = componentTranslate(componentBlue(), blueDelta);
		int alpha = componentTranslate(componentAlpha(), alphaDelta);

		java.awt.Color newColor = new java.awt.Color(red, green, blue, alpha);
		return withColorInternal(newColor);
	}

	public int componentRed() {
		return colorInternal.getRed();
	}

	public int componentGreen() {
		return colorInternal.getGreen();
	}

	public int componentBlue() {
		return colorInternal.getBlue();
	}

	public int componentAlpha() {
		return colorInternal.getAlpha();
	}

	protected int componentTranslate(int item, int size) {
		return componentCap(item + size);
	}

	protected int componentCap(int item) {
		if (item < 0) {
			return 0;
		}

		if (item > ComponentSizeMaximum) {
			return ComponentSizeMaximum;
		}

		return item;
	}

	public int toInteger() {
		return colorInternal.getRGB();
	}
}
