package mattjohns.minecraft.common.userinterface.control.system;

import mattjohns.common.general.TimerDiscrete;
import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.Result;

public abstract class MouseHold<TConcrete extends MouseHold<TConcrete>> extends Immutable<TConcrete> {
	// mouse is held down on control
	protected final boolean mouseHoldIs;

	// mutable, changes each tick

	// it would mean every parent class would also have to copy each tick, so
	// just let it mutate
	protected final TimerDiscrete mouseHoldTimer;

	// waiting for initial hold repeat
	protected final boolean mouseHoldWaitInitial;

	public final double mouseHoldFrequencyInitial;
	public final double mouseHoldFrequency;

	protected MouseHold(boolean mouseHoldIs, TimerDiscrete mouseHoldTimer, boolean mouseHoldWaitInitial,
			double mouseHoldFrequencyInitial, double mouseHoldFrequency) {
		this.mouseHoldIs = mouseHoldIs;
		this.mouseHoldTimer = mouseHoldTimer;
		this.mouseHoldWaitInitial = mouseHoldWaitInitial;
		this.mouseHoldFrequencyInitial = mouseHoldFrequencyInitial;
		this.mouseHoldFrequency = mouseHoldFrequency;
	}

	protected abstract TConcrete copy(boolean mouseHoldIs, TimerDiscrete mouseHoldTimer, boolean mouseHoldWaitInitial,
			double mouseHoldFrequencyInitial, double mouseHoldFrequency);

	@Override
	protected TConcrete concreteCopy(Immutable<?> source) {
		return copy(mouseHoldIs, mouseHoldTimer, mouseHoldWaitInitial, mouseHoldFrequencyInitial, mouseHoldFrequency);
	}

	public TConcrete withMouseHoldFrequencyInitial(double mouseHoldFrequencyInitial) {
		return copy(mouseHoldIs, mouseHoldTimer, mouseHoldWaitInitial, mouseHoldFrequencyInitial, mouseHoldFrequency);
	}

	public TConcrete withMouseHoldFrequency(double mouseHoldFrequency) {
		return copy(mouseHoldIs, mouseHoldTimer, mouseHoldWaitInitial, mouseHoldFrequencyInitial, mouseHoldFrequency);
	}

	public TConcrete withMouseHoldStart() {
		if (mouseHoldIs) {
			// already held, just ignore and allow this to be called loosely
			return concreteThis();
		}

		boolean mouseHoldIs = true;
		boolean mouseHoldWaitInitial = true;

		// mutable
		mouseHoldTimer.frequencySet(mouseHoldFrequencyInitial);
		mouseHoldTimer.restart();

		return copy(mouseHoldIs, mouseHoldTimer, mouseHoldWaitInitial, mouseHoldFrequencyInitial, mouseHoldFrequency);
	}

	protected TConcrete mouseHoldInitial() {
		if (!mouseHoldIs) {
			// mouse is no longer being held, disregard
			return concreteThis();
		}

		boolean mouseHoldWaitInitial = false;

		mouseHoldTimer.frequencySet(mouseHoldFrequency);
		mouseHoldTimer.restart();

		return copy(mouseHoldIs, mouseHoldTimer, mouseHoldWaitInitial, mouseHoldFrequencyInitial, mouseHoldFrequency);
	}

	protected TConcrete withMouseHoldStop() {
		if (!mouseHoldIs) {
			return concreteThis();
		}

		boolean mouseHoldIs = false;

		return copy(mouseHoldIs, mouseHoldTimer, mouseHoldWaitInitial, mouseHoldFrequencyInitial, mouseHoldFrequency);
	}

	/**
	 * Returns number of events that should be fired since the last tick. Caller
	 * must manually fire the events by calling holdEventConsume().
	 * 
	 * Returns 0 if hold not active or no events in time period.
	 */
	public Result<TConcrete, Integer> withMouseHoldTick() {
		if (mouseHoldIs) {
			long timeCurrent = System.currentTimeMillis();

			int eventListSize = mouseHoldTimer.consumeEventListSize(timeCurrent);

			if (eventListSize >= 1) {
				if (mouseHoldWaitInitial) {
					mouseHoldInitial();
				}
			}

			return Result.ofChange(concreteThis(), eventListSize);
			// return eventListSize;
		}
		else {
			// mouse not being held
			return Result.of(concreteThis(), 0);
			// return 0;
		}
	}

	public static final class Concrete extends MouseHold<Concrete> {
		protected Concrete(boolean mouseHoldIs, TimerDiscrete mouseHoldTimer, boolean mouseHoldWaitInitial,
				double mouseHoldFrequencyInitial, double mouseHoldFrequency) {

			super(mouseHoldIs, mouseHoldTimer, mouseHoldWaitInitial, mouseHoldFrequencyInitial, mouseHoldFrequency);
		}

		public static Concrete of(double mouseHoldFrequencyInitial, double mouseHoldFrequency) {
			boolean mouseHoldIs = false;

			TimerDiscrete mouseHoldTimer = TimerDiscrete.of();

			boolean mouseHoldWaitInitial = true;

			return new Concrete(mouseHoldIs, mouseHoldTimer, mouseHoldWaitInitial, mouseHoldFrequencyInitial,
					mouseHoldFrequency);
		}

		public static Concrete ofMinimal() {
			boolean mouseHoldIs = false;
			TimerDiscrete mouseHoldTimer = TimerDiscrete.of();
			boolean mouseHoldWaitInitial = true;
			double mouseHoldFrequencyInitial = 10;
			double mouseHoldFrequency = 15;

			return new Concrete(mouseHoldIs, mouseHoldTimer, mouseHoldWaitInitial, mouseHoldFrequencyInitial,
					mouseHoldFrequency);
		}

		@Override
		protected Concrete concreteThis() {
			return this;
		}

		@Override
		protected Concrete copy(boolean mouseHoldIs, TimerDiscrete mouseHoldTimer, boolean mouseHoldWaitInitial,
				double mouseHoldFrequencyInitial, double mouseHoldFrequency) {

			return new Concrete(mouseHoldIs, mouseHoldTimer, mouseHoldWaitInitial, mouseHoldFrequencyInitial,
					mouseHoldFrequency);
		}
	}
}
