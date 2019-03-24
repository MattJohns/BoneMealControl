package mattjohns.common.immutable.list;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;

public final class ListImmutableInteger extends ListImmutableBase<Integer, ListImmutableInteger> {
	protected ListImmutableInteger(ImmutableList<Integer> internalList) {
		super(internalList);
	}

	public static ListImmutableInteger of() {
		return new ListImmutableInteger(ImmutableList.of());
	}

	public static ListImmutableInteger of(int item) {
		return new ListImmutableInteger(ImmutableList.of(item));
	}

	public static ListImmutableInteger of(Iterable<Integer> list) {
		return new ListImmutableInteger(ImmutableList.copyOf(list));
	}

	@Override
	protected final ListImmutableInteger copy(ImmutableList<Integer> internalList) {
		return new ListImmutableInteger(internalList);
	}

	@Override
	protected ListImmutableInteger concreteCopy(Immutable<?> source) {
		return copy(internalList);
	}

	public String serialize() {
		if (isEmpty()) {
			return "";
		}

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < size(); i++) {
			builder.append(Integer.toString(get(i)));

			if (i != endIndex()) {
				builder.append(",");
			}
		}

		return builder.toString();
	}

	public static ListImmutableInteger deserialize(String text) throws NumberFormatException {
		String[] splitText = text.split(",");

		Builder builder = Builder.of();

		for (String numberText : splitText) {
			int number = Integer.parseInt(numberText);

			builder.add(number);
		}

		return builder.build();
	}

	public static final class Builder extends ListImmutableBase.Builder<Integer, ListImmutableInteger, Builder> {
		public static Builder of() {
			return new Builder();
		}
		
		@Override
		protected Builder concreteCopy(Immutable<?> source) {
			return new Builder();
		}

		@Override
		protected ListImmutableInteger upcastList(ImmutableList<Integer> baseList) {
			return new ListImmutableInteger(baseList);
		}
	}
}
