package mattjohns.common.immutable.userinterface.display;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.math.geometry.dimension2.RectangleIntegerBase;
import mattjohns.common.immutable.math.geometry.dimension2.VectorInteger;

/**
 * Position is positive and area is valid.
 */
public class DisplayBound extends RectangleIntegerBase<DisplayPosition, DisplaySize, DisplayBound> {
	public static final DisplayBound Tiny = DisplayBound.of(DisplayPosition.Zero, DisplaySize.Tiny);

	protected DisplayBound(DisplayPosition topLeft, DisplayPosition bottomRight) {
		super(topLeft, bottomRight);

		assert this.size()
				.isPositiveNonFlat();
	}

	public static DisplayBound of() {
		return of(DisplayPosition.of(), DisplaySize.of());
	}

	public static DisplayBound ofSize(DisplaySize size) {
		return of(DisplayPosition.Zero, size);
	}

	public static DisplayBound of(int top, int left, int bottom, int right) {
		return new DisplayBound(DisplayPosition.of(left, top), DisplayPosition.of(right, bottom));
	}

	public static DisplayBound of(DisplayPosition position, DisplaySize size) {
		return new DisplayBound(position, position.withTranslate(size));
	}

	public static DisplayBound of(DisplayPosition topLeft, DisplayPosition bottomRight) {
		return new DisplayBound(topLeft, bottomRight);
	}

	@Override
	protected DisplayBound copy(DisplayPosition topLeft, DisplayPosition bottomRight) {
		return new DisplayBound(topLeft, bottomRight);
	}

	@Override
	protected DisplayBound concreteCopy(Immutable<?> source) {
		return copy(topLeft, bottomRightExclusive);
	}

	@Override
	protected DisplayPosition positionCreate(Integer x, Integer y) {
		return DisplayPosition.of(x, y);
	}

	@Override
	protected DisplaySize sizeCreate(Integer x, Integer y) {
		return DisplaySize.of(x, y);
	}

	public boolean containsUnsafe(VectorInteger position) {
		return super.contains(DisplayPosition.ofUnsafe(position));
	}

	public DisplayPosition center() {
		return DisplayPosition.of(centerX(), centerY());
	}

	public int centerX() {
		return left() + (size().x / 2);
	}

	public int centerY() {
		return top() + (size().y / 2);
	}

	/**
	 * Caps the size if it's too small.
	 */
	public DisplayBound withPadding(DisplayPadding padding) {
		int top = top() + padding.top;
		int left = left() + padding.left;

		// exclusive
		int bottom = bottomExclusive() - padding.bottom;
		int right = rightExclusive() - padding.right;

		int sizeX = right - left;
		if (sizeX < 1) {
			sizeX = 1;
		}

		int sizeY = bottom - top;
		if (sizeY < 1) {
			sizeY = 1;
		}

		return DisplayBound.of(DisplayPosition.of(left, top), DisplaySize.of(sizeX, sizeY));
	}

	@Override
	public DisplayBound withIntersect(DisplayBound peer) {
		return super.withIntersect(peer);
	}
}