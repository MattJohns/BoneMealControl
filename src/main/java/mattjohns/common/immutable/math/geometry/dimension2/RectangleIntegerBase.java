package mattjohns.common.immutable.math.geometry.dimension2;

public abstract class RectangleIntegerBase<TPosition extends VectorIntegerBase<TPosition>, TSize extends VectorIntegerBase<TSize>, TConcrete extends RectangleIntegerBase<TPosition, TSize, TConcrete>>
		extends Rectangle<Integer, TPosition, TSize, TConcrete> {

	protected RectangleIntegerBase(TPosition topLeft, TPosition bottomRight) {
		super(topLeft, bottomRight);
	}
}
