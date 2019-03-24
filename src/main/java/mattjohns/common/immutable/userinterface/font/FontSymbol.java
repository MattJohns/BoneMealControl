package mattjohns.common.immutable.userinterface.font;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.userinterface.display.DisplaySize;

/**
 * A simple font character that always has the same size regardless of context
 * within a word.
 */
public final class FontSymbol extends Immutable<FontSymbol> {
	public final int id;
	public final char character;
	public final String displayText;
	public final DisplaySize size;

	protected FontSymbol(int id, char character, String displayText, DisplaySize size) {
		this.id = id;
		this.character = character;
		this.displayText = displayText;
		this.size = size;

		assert this.id >= 0;
	}

	public static FontSymbol of(int id, char character, DisplaySize size) {
		String displayText = Character.toString(character);

		return new FontSymbol(id, character, displayText, size);
	}

	@Override
	protected FontSymbol concreteCopy(Immutable<?> source) {
		return new FontSymbol(id, character, displayText, size);
	}

	public FontSymbol withDisplayText(String displayText) {
		return new FontSymbol(id, character, displayText, size);
	}

	@Override
	public String toString() {
		return id + ": " + displayText;
	}
}
