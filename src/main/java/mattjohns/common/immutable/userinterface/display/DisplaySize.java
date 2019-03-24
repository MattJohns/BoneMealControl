package mattjohns.common.immutable.userinterface.display;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.math.geometry.dimension2.VectorIntegerBase;

/**
 * Always has a position that is positive and non zero.
 * 
 * Bottom and right sides are exclusive, meaning they aren't part of the area
 * represented by this size. So a 2 x 2 pixel area should have top left at 0, 0
 * and bottom right at 2, 2 as values in this class . You can see the pixel at
 * say 2, 1 is not part of the 2 x 2 area.
 * 
 * In other words when dealing with sizes and positions you should take care to
 * add or subtract 1 during conversion. A 1 dimensional line that is 5 pixels
 * long only takes up positions 0, 1, 2, 3, 4 . Position 5 is empty. Yet size is
 * equal to 5.
 * 
 * If you try to do it so top left and bottom right are both inclusive you run
 * into problems because you can't tell the difference between something with
 * zero size and a 1 x 1 area. You also see this reflected in library functions
 * like String.subset() which takes an inclusive start and exclusive end.
 */
public final class DisplaySize extends VectorIntegerBase<DisplaySize> {
	public static final DisplaySize Tiny = new DisplaySize(1, 1);

	protected DisplaySize(Integer x, Integer y) {
		super(x, y);

		assert this.isPositiveNonFlat();
	}

	public static DisplaySize of() {
		return Tiny;
	}

	public static DisplaySize of(Integer x, Integer y) {
		return new DisplaySize(x, y);
	}

	public static DisplaySize ofUnsafe(VectorIntegerBase<?> source) {
		return new DisplaySize(source.x, source.y);
	}

	public static DisplaySize of(DisplaySize source) {
		return new DisplaySize(source.x, source.y);
	}

	@Override
	protected final DisplaySize copy(Integer x, Integer y) {
		return new DisplaySize(x, y);
	}

	@Override
	protected DisplaySize concreteCopy(Immutable<?> source) {
		return copy(x, y);
	}

	public DisplayPosition center() {
		return DisplayPosition.of(x / 2, y / 2);
	}

	@Override
	public Integer dimensionOne() {
		return 1;
	}

	public boolean contain(DisplayPosition position) {
		if (position.x < 0) {
			return false;
		}

		if (position.x >= x) {
			return false;
		}

		if (position.y < 0) {
			return false;
		}

		if (position.y >= y) {
			return false;
		}

		return true;
	}
}
