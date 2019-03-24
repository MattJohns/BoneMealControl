package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public class StyleMetric extends Style<StyleMetric> {
	public final boolean sizeIsAutomaticX;
	public final boolean sizeIsAutomaticY;

	protected StyleMetric(boolean sizeIsAutomaticX, boolean sizeIsAutomaticY) {
		this.sizeIsAutomaticX = sizeIsAutomaticX;
		this.sizeIsAutomaticY = sizeIsAutomaticY;
	}

	public static StyleMetric of() {
		return new StyleMetric(false, false);
	}

	@Override
	protected StyleMetric concreteCopy(Immutable<?> source) {
		return new StyleMetric(sizeIsAutomaticX, sizeIsAutomaticY);
	}

	public StyleMetric withSizeIsAutomaticX(boolean sizeIsAutomaticX) {
		return new StyleMetric(sizeIsAutomaticX, sizeIsAutomaticY);
	}

	public StyleMetric withSizeIsAutomaticY(boolean sizeIsAutomaticY) {
		return new StyleMetric(sizeIsAutomaticX, sizeIsAutomaticY);
	}
}
