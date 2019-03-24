package mattjohns.common.immutable;

/**
 * @param <TObject>
 * Immutable object that is returning the result. The object may be a new,
 * updated version from the code that produced this result.
 * 
 * @param <TData>
 * Can be null.
 */
public class Result<TObject, TData> extends ResultBase<TObject, Result<TObject, TData>> {
	public final TData data;

	protected Result(TObject self, boolean isChange, TData data) {
		super(self, isChange);

		this.data = data;
	}

	public static <TObject, TData> Result<TObject, TData> of(TObject object, TData data) {
		return new Result<TObject, TData>(object, false, data);
	}

	public static <TObject, TData> Result<TObject, TData> ofChange(TObject object, TData data) {
		return new Result<TObject, TData>(object, true, data);
	}

	@Override
	protected Result<TObject, TData> concreteCopy(Immutable<?> source) {
		return new Result<>(self, isChange, data);
	}

	@Override
	protected Result<TObject, TData> copy(TObject object, boolean isChange) {
		return new Result<TObject, TData>(object, isChange, data);
	}

	public Result<TObject, TData> withData(TData data) {
		return new Result<TObject, TData>(self, isChange, data);
	}
}
