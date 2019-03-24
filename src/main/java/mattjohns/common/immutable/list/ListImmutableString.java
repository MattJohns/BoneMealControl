package mattjohns.common.immutable.list;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;

public final class ListImmutableString extends ListImmutableBase<String, ListImmutableString> {
	protected ListImmutableString(ImmutableList<String> internalList) {
		super(internalList);
	}

	public static ListImmutableString of() {
		return new ListImmutableString(ImmutableList.of());
	}

	public static ListImmutableString of(String item) {
		return new ListImmutableString(ImmutableList.of(item));
	}

	public static ListImmutableString of(Iterable<String> list) {
		return new ListImmutableString(ImmutableList.copyOf(list));
	}

	@Override
	protected final ListImmutableString copy(ImmutableList<String> internalList) {
		return new ListImmutableString(internalList);
	}

	@Override
	protected ListImmutableString concreteCopy(Immutable<?> source) {
		return copy(internalList);
	}

	public String serialize(String separator) {
		if (isEmpty()) {
			return "";
		}

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < size(); i++) {
			builder.append(get(i));

			if (i != endIndex()) {
				builder.append(separator);
			}
		}

		return builder.toString();
	}

	public static ListImmutableString deserialize(String text, String separator) {
		String[] splitText = text.split(separator);

		Builder builder = Builder.of();

		for (String splitItem : splitText) {
			builder.add(splitItem);
		}

		return builder.build();
	}

	public static final class Builder extends ListImmutableBase.Builder<String, ListImmutableString, Builder> {
		public static Builder of() {
			return new Builder();
		}

		@Override
		protected Builder concreteCopy(Immutable<?> source) {
			return new Builder();
		}

		@Override
		protected ListImmutableString upcastList(ImmutableList<String> baseList) {
			return new ListImmutableString(baseList);
		}
	}
}
