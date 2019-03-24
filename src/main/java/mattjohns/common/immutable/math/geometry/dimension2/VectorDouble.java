package mattjohns.common.immutable.math.geometry.dimension2;

import mattjohns.common.immutable.Immutable;

public final class VectorDouble extends Vector<Double, VectorDouble> {
	public static final VectorDouble Zero = VectorDouble.of(0d, 0d);

	protected VectorDouble(Double x, Double y) {
		super(x, y);
	}

	public static VectorDouble of() {
		return Zero;
	}

	public static VectorDouble of(Double x, Double y) {
		return new VectorDouble(x, y);
	}

	public static VectorDouble of(Vector<Double, ?> source) {
		return new VectorDouble(source.x, source.y);
	}

	@Override
	protected VectorDouble copy(Double x, Double y) {
		return new VectorDouble(x, y);
	}

	@Override
	protected VectorDouble concreteCopy(Immutable<?> source) {
		return copy(x, y);
	}

	@Override
	public double length() {
		return Math.sqrt(lengthSquare());
	}

	@Override
	public Double dimensionZero() {
		return 0d;
	}

	@Override
	public Double dimensionOne() {
		return 1d;
	}

	@Override
	public Double dimensionNegate(Double item) {
		return item * -1d;
	}

	@Override
	public Double dimensionTranslate(Double item, Double delta) {
		return item + delta;
	}

	@Override
	public Double dimensionScale(Double item, Double factor) {
		return item * factor;
	}
}
