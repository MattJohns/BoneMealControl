package mattjohns.common.immutable.math.geometry.dimension2;

import mattjohns.common.immutable.Immutable;

public final class VectorInteger extends VectorIntegerBase<VectorInteger> {
	public static final VectorInteger Zero = new VectorInteger(0, 0);

	protected VectorInteger(Integer x, Integer y) {
		super(x, y);
	}

	public static VectorInteger of() {
		return Zero;
	}

	public static VectorInteger of(Integer x, Integer y) {
		return new VectorInteger(x, y);
	}

	public static VectorInteger of(VectorIntegerBase<?> source) {
		return new VectorInteger(source.x, source.y);
	}

	@Override
	protected final VectorInteger copy(Integer x, Integer y) {
		return new VectorInteger(x, y);
	}

	@Override
	protected VectorInteger concreteCopy(Immutable<?> source) {
		return copy(x, y);
	}

	@Override
	public Integer dimensionOne() {
		return 1;
	}
}
