package mattjohns.common.immutable.userinterface.display;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.math.geometry.dimension2.VectorIntegerBase;

/**
 * Position may be negative.
 */
public final class DisplayPosition extends VectorIntegerBase<DisplayPosition> {
	public static final DisplayPosition Zero = new DisplayPosition(0, 0);

	protected DisplayPosition(Integer x, Integer y) {
		super(x, y);
	}

	public static DisplayPosition of() {
		return Zero;
	}

	public static DisplayPosition of(Integer x, Integer y) {
		return new DisplayPosition(x, y);
	}

	public static DisplayPosition ofUnsafe(VectorIntegerBase<?> source) {
		return new DisplayPosition(source.x, source.y);
	}

	public static DisplayPosition of(DisplayPosition source) {
		return new DisplayPosition(source.x, source.y);
	}

	public static DisplayPosition ofSize(DisplaySize source) {
		return new DisplayPosition(source.x, source.y);
	}

	@Override
	protected final DisplayPosition copy(Integer x, Integer y) {
		return new DisplayPosition(x, y);
	}

	@Override
	protected DisplayPosition concreteCopy(Immutable<?> source) {
		return copy(x, y);
	}

	@Override
	public Integer dimensionOne() {
		return 1;
	}
}