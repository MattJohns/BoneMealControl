package mattjohns.common.immutable.userinterface.font;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.list.ListImmutableBase;

public final class FontSymbolList extends ListImmutableBase<FontSymbol, FontSymbolList> {
	protected FontSymbolList(ImmutableList<FontSymbol> internalList) {
		super(internalList);
	}

	public static FontSymbolList of() {
		return new FontSymbolList(ImmutableList.of());
	}

	@Override
	protected final FontSymbolList copy(ImmutableList<FontSymbol> internalList) {
		return new FontSymbolList(internalList);
	}

	public static final class Builder extends ListImmutableBase.Builder<FontSymbol, FontSymbolList, Builder> {
		@Override
		protected Builder concreteCopy(Immutable<?> source) {
			return new Builder();
		}

		@Override
		protected FontSymbolList upcastList(ImmutableList<FontSymbol> baseList) {
			return new FontSymbolList(baseList);
		}
	}
}
