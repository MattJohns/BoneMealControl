package mattjohns.common.immutable.userinterface.textarea;

final class DocumentLocationRange extends LocationRange<DocumentLocationRange, DocumentLocation> {
	public DocumentLocationRange(DocumentLocation start, DocumentLocation end) {
		super(start, end);
	}

	@Override
	protected DocumentLocationRange concreteThis() {
		return this;
	}

	@Override
	protected DocumentLocationRange copy(DocumentLocation start, DocumentLocation end) {
		return new DocumentLocationRange(start, end);
	}

	public static DocumentLocationRange of() {
		return of(DocumentLocation.of(), DocumentLocation.of());
	}

	public static DocumentLocationRange of(DocumentLocation start, DocumentLocation end) {
		return new DocumentLocationRange(start, end);
	}
}