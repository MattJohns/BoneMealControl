package mattjohns.common.immutable;

/**
 * @param <TObject>
 * Immutable object that is returning the result. The object may be a new,
 * updated version from the code that produced this result.
 * 
 * @param <TData>
 * Can be null.
 */
public abstract class ResultData<TObject, TData, TConcrete extends ResultData<TObject, TData, TConcrete>>

		extends ResultBase<TObject, TConcrete> {

	public final TData data;

	protected ResultData(TObject self, boolean isChange, TData data) {
		super(self, isChange);

		this.data = data;
	}

	protected abstract TConcrete copy(TObject self, boolean isChange, TData data);

	@Override
	protected TConcrete copy(TObject self, boolean isChange) {
		return copy(self, isChange, data);
	}
	
	public TConcrete withData(TData data) {
		return copy(self, isChange, data);
	}
}
