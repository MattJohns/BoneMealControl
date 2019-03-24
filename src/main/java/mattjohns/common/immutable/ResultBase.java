package mattjohns.common.immutable;

/**
 * Often with immutable models you need to return a value, but you also want to
 * return the modified object itself (as is customary). This base class allows
 * you to return both the object (class T) and an arbitrary result (defined by
 * subclass).
 * 
 * @param <TObject>
 * The type of immutable object being worked on.
 * 
 * @param <TConcrete>
 * The class that inherits from this base class.
 */
public abstract class ResultBase<TObject, TConcrete extends ResultBase<TObject, TConcrete>>
		extends Immutable<TConcrete> {
	/**
	 * The immutable object that was modified.
	 */
	public final TObject self;

	/**
	 * The object was modified.
	 */
	public final boolean isChange;

	protected ResultBase(TObject self, boolean isChange) {
		this.self = self;
		this.isChange = isChange;

		assert this.self != null;
	}

	protected abstract TConcrete copy(TObject self, boolean isChange);

	@Override
	protected TConcrete concreteCopy(Immutable<?> source) {
		return copy(self, isChange);
	}

	public TConcrete withSelf(TObject self) {
		return copy(self, isChange);
	}

	public TConcrete withIsChange(boolean isChange) {
		return copy(self, isChange);
	}
}
