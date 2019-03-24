package mattjohns.common.immutable.userinterface.textarea;

/**
 * Refers to a location within a document.
 * 
 * A document index is different to a document location. An index must refer to
 * a symbol whereas a location may also refer to one past the end of line.
 * 
 * Locations are useful when manipulating symbols (e.g. subset() methods), and in
 * a normal text editor the cursor is allowed to sit one past the end of the
 * line.
 */
final class DocumentLocation extends Location<DocumentLocation> {
	public DocumentLocation(int line, int symbol) {
		super(line, symbol);
	}

	public static DocumentLocation of() {
		return of(0, 0);
	}

	@Override
	protected DocumentLocation copy(int line, int symbol) {
		return DocumentLocation.of(line, symbol);
	}

	public static DocumentLocation of(int line, int symbol) {
		return new DocumentLocation(line, symbol);
	}
}
