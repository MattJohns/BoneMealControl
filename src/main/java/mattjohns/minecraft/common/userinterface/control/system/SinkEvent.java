package mattjohns.minecraft.common.userinterface.control.system;

import mattjohns.common.immutable.Immutable;

/**
 * Immutable event from a control, such as a button click.
 * 
 * @param <TSource>
 * The class that created the event.
 * 
 * @param <TConcrete>
 * The concrete class that derives from this one.
 */
public abstract class SinkEvent<TSource, TConcrete extends SinkEvent<TSource, TConcrete>> extends Immutable<TConcrete> {
	public final TSource source;

	protected SinkEvent(TSource sender) {
		this.source = sender;
	}

	protected abstract TConcrete copy(TSource source);

	protected final TConcrete concreteCopy() {
		return copy(this.source);
	}

	public TConcrete withSource(TSource source) {
		return copy(source);
	}
}
