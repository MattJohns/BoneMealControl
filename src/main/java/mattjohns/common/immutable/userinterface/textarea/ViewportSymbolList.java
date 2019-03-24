package mattjohns.common.immutable.userinterface.textarea;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.list.ListImmutableBase;

final class ViewportSymbolList extends ListImmutableBase<ViewportSymbol, ViewportSymbolList> {
	protected ViewportSymbolList(ImmutableList<ViewportSymbol> internalList) {
		super(internalList);
	}

	public static ViewportSymbolList of() {
		return new ViewportSymbolList(ImmutableList.of());
	}

	@Override
	protected final ViewportSymbolList copy(ImmutableList<ViewportSymbol> internalList) {
		return new ViewportSymbolList(internalList);
	}

	public int displaySizeX() {
		return sum(ViewportSymbol::sizeX);
	}

	public String textStyle() {
		StringBuilder builder = new StringBuilder();

		stream().forEach(viewportSymbol -> builder.append(viewportSymbol.textStyle()));

		return builder.toString();
	}

	public String textNoStyle() {
		StringBuilder builder = new StringBuilder();

		stream().forEach(viewportSymbol -> builder.append(viewportSymbol.textNoStyle()));

		return builder.toString();
	}

	@Override
	public String toString() {
		return textStyle();
	}

	public static final class Builder extends ListImmutableBase.Builder<ViewportSymbol, ViewportSymbolList, Builder> {
		public static Builder of() {
			return new Builder();
		}

		@Override
		protected Builder concreteCopy(Immutable<?> source) {
			return new Builder();
		}

		@Override
		protected ViewportSymbolList upcastList(ImmutableList<ViewportSymbol> baseList) {
			return new ViewportSymbolList(baseList);
		}
	}
}
