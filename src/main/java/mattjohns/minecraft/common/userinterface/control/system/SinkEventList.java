package mattjohns.minecraft.common.userinterface.control.system;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.list.ListImmutableBase;

/**
 * Immutable list of consumers for an event type.
 * 
 * Can contain events from multiple source types.
 * 
 * @param <TEvent>
 * An event from a control, for example a mouse click.
 */
public class SinkEventList<TEvent extends SinkEvent<?, TEvent>>

		extends ListImmutableBase<Consumer<TEvent>, SinkEventList<TEvent>> {

	protected SinkEventList(ImmutableList<Consumer<TEvent>> internalList) {
		super(internalList);
	}

	public static <TEvent extends SinkEvent<?, TEvent>> SinkEventList<TEvent> of() {
		return new SinkEventList<>(ImmutableList.of());
	}

	@Override
	protected SinkEventList<TEvent> copy(ImmutableList<Consumer<TEvent>> internalList) {
		return new SinkEventList<>(internalList);
	}

	public void consume(TEvent event) {
		this.forEach(x -> x.accept(event));
	}

	public SinkEventList<TEvent> add(Consumer<TEvent> sink) {
		assert !contains(sink) : "Consumer already exists.";

		return withJoinItem(sink);
	}
}
