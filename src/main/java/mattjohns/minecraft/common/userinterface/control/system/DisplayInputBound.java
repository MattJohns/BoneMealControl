package mattjohns.minecraft.common.userinterface.control.system;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.Result;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.minecraft.common.userinterface.style.StyleInput;

public class DisplayInputBound<T> extends Immutable<T> {
	public final int id;

	// 'Desired' because the actual bound() method might cap the value it
	// returns. For example a Control has a minimum size so bound() will never
	// return a size lower than that, even if the desired bound is tiny.
	public final DisplayBound boundDesire;

	// mouse is held down on control
	public final MouseHold.Concrete mouseHold;

	public final ControlEventList<NewMouseClickEvent> eventListMouseClick;

	protected DisplayInputBound(int id, DisplayBound boundDesire, MouseHold.Concrete mouseHold,
			ControlEventList<NewMouseClickEvent> eventListMouseClick) {
		super();

		this.id = id;
		this.boundDesire = boundDesire;
		this.mouseHold = mouseHold;
		this.eventListMouseClick = eventListMouseClick;
	}

	public static DisplayInputBound<?> of(int id) {
		return new DisplayInputBound<>(id, DisplayBound.Tiny, MouseHold.Concrete.ofMinimal(), ControlEventList.of());
	}

	protected T concreteCopy(DisplayInputBound<?> source) {
		return concreteTo(
				new DisplayInputBound<>(source.id, source.boundDesire, source.mouseHold, source.eventListMouseClick));
	}

	@Override
	protected T concreteCopy(Immutable<?> source) {
		return concreteCopy(new DisplayInputBound<>(id, boundDesire, mouseHold, eventListMouseClick));
	}

	protected T withId(int id) {
		return concreteCopy(new DisplayInputBound<>(id, boundDesire, mouseHold, eventListMouseClick));
	}

	public T withBoundDesire(DisplayBound boundDesire) {
		return concreteCopy(new DisplayInputBound<>(id, boundDesire, mouseHold, eventListMouseClick));
	}

	protected T withMouseHold(MouseHold.Concrete mouseHold) {
		return concreteCopy(new DisplayInputBound<>(id, boundDesire, mouseHold, eventListMouseClick));
	}

	public T withEventListMouseClick(ControlEventList<NewMouseClickEvent> eventListMouseClick) {
		return concreteCopy(new DisplayInputBound<>(id, boundDesire, mouseHold, eventListMouseClick));
	}

	public T withEventClear() {
		return withEventListMouseClick(eventListMouseClick.withClear());
	}

	protected T withDimensionConsumeChange() {
		return concreteThis();
	}

	public T withPosition(DisplayPosition position) {
		return withBoundDesire(bound().withPosition(position));
	}

	public T withSize(DisplaySize size) {
		return withBoundDesire(bound().withSize(size));
	}

	protected T withInputStyleApply(StyleInput style) {
		return withMouseHold(mouseHold

				.withMouseHoldFrequencyInitial(style.mouseHoldFrequencyInitial)
				.withMouseHoldFrequency(style.mouseHoldFrequency));
	}

	// relative position
	public T withInputMouseConsumeClickDown(DisplayPosition mousePosition, int mouseButton) {
		if (bound().contains(mousePosition)) {
			return withInputMouseConsumeClickDownInside(mousePosition, mouseButton);
		}
		else {
			return concreteThis();
		}
	}

	protected T withInputMouseConsumeClickDownInside(DisplayPosition mousePosition, int buttonIndex) {
		NewMouseClickEvent event = NewMouseClickEvent.of(mousePosition, buttonIndex, false);

		return withEventListMouseClick(eventListMouseClick.withJoinItem(event));
	}

	public T withTick() {
		// Manually trigger mouse hold events here rather than in ControlInput.
		// That way subclasses can override mouseHoldOn() rather deal with
		// consumers.
		Result<MouseHold.Concrete, Integer> result = mouseHold.withMouseHoldTick();

		int mouseHoldEventListSize = result.data;
		if (mouseHoldEventListSize >= 1) {
			// mouseHoldOn(mouseHoldEventListSize);
		}

		return withMouseHold(result.self);
	}

	/**
	 * Override to provided a safe value (e.g. within parent control area).
	 */
	public DisplayBound bound() {
		return boundDesire;
	}

	public DisplayPosition position() {
		return bound().topLeft;
	}

	public DisplaySize size() {
		return bound().size();
	}

	protected void inputMouseHoldStart() {
		// if (mouseHoldStart()) {
		// inputMouseConsumeHoldStart();
		// }
	}

	protected void inputMouseHoldStop() {
		// if (mouseHoldStop()) {
		// inputMouseConsumeHoldStop();
		// }
	}

	protected void inputMouseConsumeHoldStart() {
		// send an artificial hold event because the timer doesn't do an
		// initial fire
		mouseHoldOn(1);
	}

	protected void inputMouseConsumeHoldStop() {
	}

	protected void mouseHoldOn(int eventListSize) {
		// MouseHoldEvent event = MouseHoldEvent.of(this, eventListSize);
		// mouseHoldEventConsume(event);
	}


	/**
	 * Position relative to control.
	 */
	public boolean mouseoverCheck(DisplayPosition mousePositionRelative) {
		return size().contain(mousePositionRelative);
	}

	public boolean equalIdIs(DisplayInputBound<?> item) {
		return id == item.id;
	}

	// @Override
	// protected TConcrete concreteThis() {
	// return (TConcrete)this;
	// }

	public static final class NewMouseClickEvent extends ControlEvent<NewMouseClickEvent> {
		public final DisplayPosition positionRelative;
		public final int buttonIndex;
		public final boolean upIs;

		public NewMouseClickEvent(DisplayPosition positionRelative, int buttonIndex, boolean upIs) {
			super();

			this.positionRelative = positionRelative;
			this.buttonIndex = buttonIndex;
			this.upIs = upIs;
		}

		public static NewMouseClickEvent of(DisplayPosition positionRelative, int buttonIndex, boolean upIs) {
			return new NewMouseClickEvent(positionRelative, buttonIndex, upIs);
		}

		@Override
		protected NewMouseClickEvent concreteCopy(Immutable<?> source) {
			return new NewMouseClickEvent(positionRelative, buttonIndex, upIs);
		}
	}
}
