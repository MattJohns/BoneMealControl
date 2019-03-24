package mattjohns.common.immutable.userinterface.textarea;

import mattjohns.common.immutable.Immutable;

/**
 * Base class for document locations.
 * 
 * Used by other models when they want to have their own special document
 * location class instead of just using DocumentLocation class. That way you
 * don't mix up different location types (even though the classes have the same
 * structure).
 * 
 * A 'document location' is different than a 'document index'. An index must
 * point to a specific character whereas a location may also point to the index
 * after the last character.
 * 
 * Locations are useful when dealing with say a cursor because you can allow the
 * cursor to sit on the end of the line, rather than forcing it to always be
 * over a symbol. Locations are also useful when dealing with document ranges
 * because they can represent a inclusive start point and an exclusive end point
 * (which is the way sub strings are normally represented).
 * 
 * @param <TConcrete>
 * The concrete type that inherits from this base class.
 */
abstract class Location<TConcrete extends Location<TConcrete>> extends Immutable<TConcrete>
		implements Comparable<TConcrete> {
	/**
	 * Line index within the document.
	 */
	public final int line;

	/**
	 * Symbol location within the line.
	 */
	public final int symbol;

	public Location(int line, int symbol) {
		this.line = line;
		this.symbol = symbol;

		assert line >= 0;
		assert symbol >= 0;
	}

	// protected abstract TConcrete self();

	protected abstract TConcrete copy(int line, int symbol);

	@Override
	protected TConcrete concreteCopy(Immutable<?> source) {
		return copy(line, symbol);
	}

	// Don't want to allow caller to set line separately because then
	// symbol would be incorrect. So force them to be set together.
	public TConcrete withLocation(int line, int symbol) {
		return copy(line, symbol);
	}

	// safe to set symbol by itself because line remains valid regardless
	public TConcrete withSymbol(int symbol) {
		return copy(line, symbol);
	}

	/**
	 * Warning: Ensure symbol is correct when using this method. If you just
	 * change the line then symbol will be incorrect unless you take care of it.
	 */
	public TConcrete withLineAdd(int delta) {
		return copy(line + delta, symbol);
	}

	/**
	 * Safe to freely modify the symbol, it is unable to make the line invalid
	 * (unlike the other way around).
	 */
	public TConcrete withSymbolAdd(int delta) {
		return withSymbol(symbol + delta);
	}

	/**
	 * Top left of document.
	 */
	public boolean isZero() {
		return (line == 0) && (symbol == 0);
	}

	@Override
	public String toString() {
		return "[" + line + ", " + symbol + "]";
	}

	@Override
	public int compareTo(TConcrete item) {
		int lineResult = new Integer(line).compareTo(item.line);
		if (lineResult != 0) {
			return lineResult;
		}

		int symbolResult = new Integer(symbol).compareTo(item.symbol);
		if (symbolResult != 0) {
			return symbolResult;
		}

		return 0;
	}
}
