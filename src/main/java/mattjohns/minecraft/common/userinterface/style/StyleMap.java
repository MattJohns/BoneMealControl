package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.list.ListImmutable;
import mattjohns.common.immutable.list.MapImmutable;

public abstract class StyleMap<TStyle extends Style<TStyle>,

TConcrete extends StyleMap<TStyle, TConcrete>>

		extends MapImmutable<EnumStyle, TStyle, TConcrete> {

	protected StyleMap(ListImmutable<EnumStyle> keyList, ListImmutable<TStyle> valueList) {
		super(keyList, valueList);
	}

	public TConcrete withNormal(TStyle item) {
		return withValueSet(EnumStyle.Normal, item);
	}

	public TConcrete withNotEnable(TStyle item) {
		return withValueSet(EnumStyle.NotEnable, item);
	}

	public TConcrete withReadOnly(TStyle item) {
		return withValueSet(EnumStyle.ReadOnly, item);
	}

	public TConcrete withMouseover(TStyle item) {
		return withValueSet(EnumStyle.Mouseover, item);
	}

	public TConcrete withSelect(TStyle item) {
		return withValueSet(EnumStyle.Select, item);
	}

	public TConcrete withAll(TStyle item) {
		return withNormal(item).withNotEnable(item)
				.withReadOnly(item)
				.withMouseover(item)
				.withSelect(item);
	}

	public TStyle normal() {
		return value(EnumStyle.Normal);
	}

	public TStyle notEnable() {
		return value(EnumStyle.NotEnable);
	}

	public TStyle readOnly() {
		return value(EnumStyle.ReadOnly);
	}

	public TStyle mouseover() {
		return value(EnumStyle.Mouseover);
	}

	public TStyle select() {
		return value(EnumStyle.Select);
	}
}
