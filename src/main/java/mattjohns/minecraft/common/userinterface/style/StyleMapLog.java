package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;

public final class StyleMapLog extends StyleMap<StyleLog, StyleMapLog> {
	public final StyleMapTextArea textNormal;
	public final StyleMapTextArea textCompact;

	protected StyleMapLog(ListImmutable<EnumStyle> keyList, ListImmutable<StyleLog> valueList,
			StyleMapTextArea textNormal, StyleMapTextArea textCompact) {
		super(keyList, valueList);

		this.textNormal = textNormal;
		this.textCompact = textCompact;
	}

	public static StyleMapLog of() {
		return new StyleMapLog(ListImmutable.of(), ListImmutable.of(), StyleMapTextArea.of(), StyleMapTextArea.of());
	}

	@Override
	protected StyleMapLog copy(ListImmutable<EnumStyle> keyList, ListImmutable<StyleLog> valueList) {
		return new StyleMapLog(keyList, valueList, textNormal, textCompact);
	}

	@Override
	protected StyleMapLog concreteThis() {
		return this;
	}

	public StyleMapLog withTextNormal(StyleMapTextArea textNormal) {
		return new StyleMapLog(keyList, valueList, textNormal, textCompact);
	}

	public StyleMapLog withTextCompact(StyleMapTextArea textCompact) {
		return new StyleMapLog(keyList, valueList, textNormal, textCompact);
	}
}
