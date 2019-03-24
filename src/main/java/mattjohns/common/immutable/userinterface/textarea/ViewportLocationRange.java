package mattjohns.common.immutable.userinterface.textarea;

final class ViewportLocationRange extends LocationRange<ViewportLocationRange, ViewportLocation> {
	public ViewportLocationRange(ViewportLocation start, ViewportLocation end) {
		super(start, end);
	}

	@Override
	protected ViewportLocationRange concreteThis() {
		return this;
	}

	@Override
	protected ViewportLocationRange copy(ViewportLocation start, ViewportLocation end) {
		return new ViewportLocationRange(start, end);
	}

	public static ViewportLocationRange of() {
		return of(ViewportLocation.of(), ViewportLocation.of());
	}

	public static ViewportLocationRange of(ViewportLocation start, ViewportLocation end) {
		return new ViewportLocationRange(start, end);
	}
}
