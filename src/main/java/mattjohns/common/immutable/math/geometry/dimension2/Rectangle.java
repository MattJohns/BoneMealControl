package mattjohns.common.immutable.math.geometry.dimension2;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.list.ListImmutable;

/**
 * Rectangle that stores top left and bottom right positions, rather than
 * position and size.
 * 
 * Bottom right is exclusive, it just defines the boundary. Top left is
 * inclusive.
 *
 * @param <TNumber>
 *            The type of number used for each dimension.
 * 
 * @param <TPosition>
 *            The type used to store the top left position of the rectangle.
 * 
 * @param <TSize>
 *            The type used to store the size. This may be different than
 *            TPosition in cases where you want the subclass to always have a
 *            positive size but position may be negative.
 * 
 * @param <TConcrete>
 *            The concrete subclass.
 */
public abstract class Rectangle<TNumber extends Comparable<TNumber>, TPosition extends Vector<TNumber, TPosition>, TSize extends Vector<TNumber, TSize>, TConcrete extends Rectangle<TNumber, TPosition, TSize, TConcrete>>
		extends Immutable<TConcrete> implements Comparable<Rectangle<TNumber, TPosition, TSize, TConcrete>> {
	public final TPosition topLeft;

	/**
	 * Note this is exclusive. It is not actually part of the rectangle's area,
	 * it just defines the border.
	 */
	public final TPosition bottomRightExclusive;

	protected Rectangle(TPosition topLeft, TPosition bottomRightExclusive) {
		this.topLeft = topLeft;
		this.bottomRightExclusive = bottomRightExclusive;
	}

	protected abstract TConcrete copy(TPosition topLeft, TPosition bottomRightExclusive);

	public TConcrete withCorner(TPosition topLeft, TPosition bottomRightExclusive) {
		/// use self().with instead
		return copy(topLeft, bottomRightExclusive);
	}

	public TConcrete withTranslateX(TNumber delta) {
		return withCorner(topLeft.withTranslateX(delta), bottomRightExclusive.withTranslateX(delta));
	}

	public TConcrete withTranslateY(TNumber delta) {
		return withCorner(topLeft.withTranslateY(delta), bottomRightExclusive.withTranslateY(delta));
	}

	public TConcrete withTranslate(TNumber xDelta, TNumber yDelta) {
		return withTranslateX(xDelta).withTranslateY(yDelta);
	}

	public TConcrete withTranslate(Vector<TNumber, ?> delta) {
		return withTranslate(delta.x, delta.y);
	}

	public TConcrete withSubtract(Vector<TNumber, ?> delta) {
		return withTranslate(dimensionNegate(delta.x), dimensionNegate(delta.y));
	}

	public TConcrete withPosition(Vector<TNumber, ?> position) {
		return withPositionX(position.x).withPositionY(position.y);
	}

	public TConcrete withPositionX(TNumber positionX) {
		TNumber sizeX = sizeX();

		TPosition newTopLeft = topLeft.withX(positionX);

		TNumber bottomRightX = dimensionTranslate(newTopLeft.x, sizeX);
		TPosition newBottomRight = bottomRightExclusive.withX(bottomRightX);

		return withCorner(newTopLeft, newBottomRight);
	}

	public TConcrete withPositionY(TNumber positionY) {
		TNumber sizeY = sizeY();

		TPosition newTopLeft = topLeft.withY(positionY);

		TNumber bottomRightY = dimensionTranslate(newTopLeft.y, sizeY);
		TPosition newBottomRight = bottomRightExclusive.withY(bottomRightY);

		return withCorner(newTopLeft, newBottomRight);
	}

	public TConcrete withSize(Vector<TNumber, ?> size) {
		return withSizeX(size.x).withSizeY(size.y);
	}

	public TConcrete withSizeX(TNumber sizeX) {
		TNumber bottomRightX = dimensionTranslate(topLeft.x, sizeX);

		return withCorner(topLeft, bottomRightExclusive.withX(bottomRightX));
	}

	public TConcrete withSizeY(TNumber sizeY) {
		TNumber bottomRightY = dimensionTranslate(topLeft.y, sizeY);

		return withCorner(topLeft, bottomRightExclusive.withY(bottomRightY));
	}

	/**
	 * Flips the rectangle so that top left is less than bottom right.
	 */
	public TConcrete withPositive() {
		TNumber newTopLeftX;
		TNumber newBottomRightX;
		if (topLeft.x.compareTo(bottomRightExclusive.x) > 0) {
			newTopLeftX = bottomRightExclusive.x;
			newBottomRightX = topLeft.x;
		} else {
			newTopLeftX = topLeft.x;
			newBottomRightX = bottomRightExclusive.x;
		}

		TNumber newTopLeftY;
		TNumber newBottomRightY;
		if (topLeft.y.compareTo(bottomRightExclusive.y) > 0) {
			newTopLeftY = bottomRightExclusive.y;
			newBottomRightY = topLeft.y;
		} else {
			newTopLeftY = topLeft.y;
			newBottomRightY = bottomRightExclusive.y;
		}

		return withCorner(positionCreate(newTopLeftX, newTopLeftY), positionCreate(newBottomRightX, newBottomRightY));
	}

	protected abstract TPosition positionCreate(TNumber x, TNumber y);

	protected abstract TSize sizeCreate(TNumber x, TNumber y);

	/**
	 * Compares top left first, then bottom right.
	 */
	@Override
	public int compareTo(Rectangle<TNumber, TPosition, TSize, TConcrete> item) {
		int topLeftResult = concreteThis().topLeft.compareTo(item.topLeft);
		if (topLeftResult != 0) {
			return topLeftResult;
		}

		int bottomRightResult = concreteThis().bottomRightExclusive.compareTo(item.bottomRightExclusive);
		if (bottomRightResult != 0) {
			return bottomRightResult;
		}

		return 0;
	}

	public String toString() {
		return "(top left: " + topLeft + ", bottom right: " + bottomRightExclusive + ")";
	}

	/**
	 * Both dimensions are >= 0 .
	 */
	public boolean isSizePositive() {
		return size().isPositive();
	}

	/**
	 * One or both dimensions are 0 size.
	 */
	public boolean isSizeFlat() {
		return size().isFlat();
	}

	/**
	 * Both dimensions > 0 .
	 */
	public boolean isSizePositiveNonFlat() {
		return size().isPositiveNonFlat();
	}

	public TNumber sizeX() {
		return dimensionTranslate(bottomRightExclusive.x, dimensionNegate(topLeft.x));
	}

	public TNumber sizeY() {
		return dimensionTranslate(bottomRightExclusive.y, dimensionNegate(topLeft.y));
	}

	public TSize size() {
		return sizeCreate(sizeX(), sizeY());
	}

	/**
	 * The zero value for this number system.
	 */
	protected TNumber dimensionZero() {
		// can just use one of the vectors instead of making subclass provide it
		return topLeft.dimensionZero();
	}

	/**
	 * Adds delta to the given number.
	 */
	protected TNumber dimensionTranslate(TNumber item, TNumber delta) {
		return topLeft.dimensionTranslate(item, delta);
	}

	/**
	 * Flips the sign for the given number.
	 */
	protected TNumber dimensionNegate(TNumber item) {
		return topLeft.dimensionNegate(item);
	}

	///// change this to convert from int into whatever type TNumber is
	protected TNumber dimensionOne() {
		return topLeft.dimensionOne();
	}

	public TNumber left() {
		return topLeft.x;
	}

	public TNumber top() {
		return topLeft.y;
	}

	public TNumber rightExclusive() {
		return bottomRightExclusive.x;
	}

	public TNumber bottomExclusive() {
		return bottomRightExclusive.y;
	}

	public TNumber rightInclusive() {
		return dimensionTranslate(bottomRightExclusive.x, dimensionNegate(dimensionOne()));
	}

	public TNumber bottomInclusive() {
		return dimensionTranslate(bottomRightExclusive.y, dimensionNegate(dimensionOne()));
	}

	public TPosition bottomRightInclusive() {
		return positionCreate(rightInclusive(), bottomInclusive());
	}

	public TPosition topRightExclusive() {
		return positionCreate(bottomRightExclusive.x, topLeft.y);
	}

	public TPosition bottomLeftExclusive() {
		return positionCreate(topLeft.x, bottomRightExclusive.y);
	}

	public TPosition topRightInclusive() {
		return positionCreate(bottomRightInclusive().x, topLeft.y);
	}

	public TPosition bottomLeftInclusive() {
		return positionCreate(topLeft.x, bottomRightInclusive().y);
	}

	/**
	 * Is inside the given container rectangle, or at least touching the walls.
	 */
	public boolean isInsideOrEqualTo(TConcrete container) {
		return container.containsOrEqualBound(topLeft) && container.containsOrEqualBound(bottomRightExclusive);
	}

	/**
	 * Both rectangles overlap inclusively.
	 */
	public boolean isIntersect(TConcrete peer) {
		// intersect if any corner within the other rectangle
		if (this.contains(peer.topLeft)) {
			return true;
		}

		if (this.contains(peer.bottomLeftInclusive())) {
			return true;
		}

		if (this.contains(peer.topRightInclusive())) {
			return true;
		}

		if (this.contains(peer.bottomRightInclusive())) {
			return true;
		}

		if (peer.contains(this.topLeft)) {
			return true;
		}

		if (peer.contains(this.bottomLeftInclusive())) {
			return true;
		}

		if (peer.contains(this.topRightInclusive())) {
			return true;
		}

		if (peer.contains(this.bottomRightInclusive())) {
			return true;
		}

		return false;
	}

	/**
	 * Inclusive intersection.
	 */
	////// wrong for case where both rectangles intersect like a cross, and no
	////// corners are inside
	public TConcrete withIntersectOld(TConcrete peer) {
		assert isIntersect(peer);

		ListImmutable.Builder<TPosition> builder = ListImmutable.Builder.of();

		if (this.contains(peer.topLeft)) {
			builder.add(peer.topLeft);
		}

		if (this.contains(peer.topRightInclusive())) {
			builder.add(peer.topRightInclusive());
		}

		if (this.contains(peer.bottomLeftInclusive())) {
			builder.add(peer.bottomLeftInclusive());
		}

		if (this.contains(peer.bottomRightInclusive())) {
			builder.add(peer.bottomRightInclusive());
		}

		if (peer.contains(this.topLeft)) {
			builder.add(this.topLeft);
		}

		if (peer.contains(this.topRightInclusive())) {
			builder.add(this.topRightInclusive());
		}

		if (peer.contains(this.bottomLeftInclusive())) {
			builder.add(this.bottomLeftInclusive());
		}

		if (peer.contains(this.bottomRightInclusive())) {
			builder.add(this.bottomRightInclusive());
		}

		ListImmutable<TPosition> intersectList = builder.build();

		assert intersectList.size() >= 1 : "isIntersect() lied or the logic above is wrong.";

		return withCornerList(intersectList);
	}

	protected TNumber dimensionMinimum(TNumber number1, TNumber number2) {
		if (number1.compareTo(number2) <= 0) {
			return number1;
		} else {
			return number2;
		}
	}

	protected TNumber dimensionMaximum(TNumber number1, TNumber number2) {
		if (number1.compareTo(number2) >= 0) {
			return number1;
		} else {
			return number2;
		}
	}

	public TConcrete withIntersect(TConcrete peer) {
		assert isIntersect(peer);

		TNumber left = dimensionMaximum(this.left(), peer.left());
		TNumber top = dimensionMaximum(this.top(), peer.top());
		TNumber right = dimensionMinimum(this.rightInclusive(), peer.rightInclusive());
		TNumber bottom = dimensionMinimum(this.bottomInclusive(), peer.bottomInclusive());

		TPosition topLeft = positionCreate(left, top);

		// make bottom right exclusive
		TNumber rightExclusive = dimensionTranslate(right, dimensionOne());
		TNumber bottomExclusive = dimensionTranslate(bottom, dimensionOne());

		TPosition bottomRightExclusive = positionCreate(rightExclusive, bottomExclusive);

		return withCorner(topLeft, bottomRightExclusive);
	}

	/**
	 * Gets the maximum rectangle that is represented by the points. At least 2
	 * opposite corners are needed otherwise the rectangle will be flat.
	 * 
	 * Always returns a positive rectangle.
	 */
	public TConcrete withCornerList(ListImmutable<TPosition> cornerList) {
		assert cornerList.size() >= 1;

		if (cornerList.size() == 1) {
			return withCorner(cornerList.get(0),
					cornerList.get(0).withTranslateX(dimensionOne()).withTranslateY(dimensionOne()));
		}

		TNumber left = cornerList.get(0).x;
		for (int i = 0; i < cornerList.size(); i++) {
			TNumber cornerLeft = cornerList.get(i).x;
			if (cornerLeft.compareTo(left) < 0) {
				left = cornerLeft;
			}
		}

		TNumber right = cornerList.get(0).x;
		for (int i = 0; i < cornerList.size(); i++) {
			TNumber cornerRight = cornerList.get(i).x;
			if (cornerRight.compareTo(right) > 0) {
				right = cornerRight;
			}
		}

		TNumber top = cornerList.get(0).y;
		for (int i = 0; i < cornerList.size(); i++) {
			TNumber cornerTop = cornerList.get(i).y;
			if (cornerTop.compareTo(top) < 0) {
				top = cornerTop;
			}
		}

		TNumber bottom = cornerList.get(0).y;
		for (int i = 0; i < cornerList.size(); i++) {
			TNumber cornerBottom = cornerList.get(i).y;
			if (cornerBottom.compareTo(bottom) > 0) {
				bottom = cornerBottom;
			}
		}

		// should now be positive
		assert left.compareTo(right) <= 0;
		assert top.compareTo(bottom) <= 0;

		TPosition topLeft = positionCreate(left, top);

		// make bottom right exclusive

		/// Fails when using floating point values, don't want to add 1.0 to it.
		/// This is
		/// all poorly designed.

		TNumber rightExclusive = dimensionTranslate(right, dimensionOne());
		TNumber bottomExclusive = dimensionTranslate(bottom, dimensionOne());

		TPosition bottomRightExclusive = positionCreate(rightExclusive, bottomExclusive);

		return withCorner(topLeft, bottomRightExclusive);
	}

	/**
	 * Position is inside or touching walls.
	 */
	public boolean contains(TPosition position) {
		if (position.x.compareTo(topLeft.x) < 0) {
			return false;
		}

		if (position.y.compareTo(topLeft.y) < 0) {
			return false;
		}

		// exclusive
		if (position.x.compareTo(bottomRightExclusive.x) >= 0) {
			return false;
		}

		// exclusive
		if (position.y.compareTo(bottomRightExclusive.y) >= 0) {
			return false;
		}

		return true;
	}

	/**
	 * Position is inside or on the bounds. Same as contains() method except
	 * position may also be on the exclusive bottom right border.
	 */
	protected boolean containsOrEqualBound(TPosition position) {
		if (position.x.compareTo(topLeft.x) < 0) {
			return false;
		}

		if (position.y.compareTo(topLeft.y) < 0) {
			return false;
		}

		// exclusive
		if (position.x.compareTo(bottomRightExclusive.x) > 0) {
			return false;
		}

		// exclusive
		if (position.y.compareTo(bottomRightExclusive.y) > 0) {
			return false;
		}

		return true;
	}
}