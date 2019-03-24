package mattjohns.common.immutable.userinterface.textarea;

/**
 * A location within a (wrapped) viewport document.  'line' is
 * the line index and 'symbol' is the location within that line.
 * 
 * The location after the last character on the line is also valid,
 * as long as the line is not wrapped.
 * 
 * Note that viewport locations refer to the wrapped document. They need to be
 * properly transformed to DocumentLocation if you need to access the source
 * document directly.
 */
final class ViewportLocation extends Location<ViewportLocation> {
	public ViewportLocation(int line, int symbol) {
		super(line, symbol);
	}

	@Override
	protected ViewportLocation concreteThis() {
		return this;
	}

	@Override
	protected ViewportLocation copy(int line, int symbol) {
		return ViewportLocation.of(line, symbol);
	}

	public static ViewportLocation of() {
		return of(0, 0);
	}

	public static ViewportLocation of(int line, int symbol) {
		return new ViewportLocation(line, symbol);
	}
}
