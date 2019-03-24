package mattjohns.common.general;

import mattjohns.common.math.General;

/**
 * A timer that outputs a discrete number of events that have passed when
 * updated.
 * 
 * Partial time is accumulated so overall accuracy is high.
 * 
 * No initial event is fired, zero time is not treated as an event. Frequency
 * must be positive and non zero.
 */
public class TimerDiscrete {
	protected double frequency;

	protected long timeStartMillisecond;
	protected long timePreviousMillisecond;
	protected double accumulate;

	protected TimerDiscrete(double frequency) {
		this.frequency = frequency;

		assert !General.isDoubleLessOrEqual(this.frequency, 0) : "Frequency must be positive.";

		restart();
	}

	public static TimerDiscrete of() {
		return new TimerDiscrete(1d);
	}

	public static TimerDiscrete of(double frequencyHertz) {
		return new TimerDiscrete(frequencyHertz);
	}

	public int consumeEventListSize(long timeCurrent) {
		long timeDeltaMillisecond = timeCurrent - timePreviousMillisecond;
		if (timeDeltaMillisecond < 0) {
			timeDeltaMillisecond = 0;
		}

		if (timeDeltaMillisecond == 0) {
			// no time has passed
			return 0;
		}

		double timeDelta = ((double)timeDeltaMillisecond) / 1000;
		timeDelta += accumulate;

		int eventCount = eventUsefulListSize(timeDelta);

		accumulate = accumulateDerive(timeDelta, eventCount);

		timePreviousMillisecond = timeCurrent;

		return eventCount;
	}

	/**
	 * Gets the number of events that fit within the delta
	 */
	protected int eventUsefulListSize(double delta) {
		if (delta < 0) {
			// should never happen
			return 0;
		}

		double eventListSize = delta / period();

		long eventListSizeRoundDown = (long)eventListSize;

		// prevent insane event frequency
		if (eventListSizeRoundDown > 1000000) {
			eventListSizeRoundDown = 1000000;
		}

		return (int)eventListSizeRoundDown;
	}

	/**
	 * Removes the event time from the delta and returns the leftover seconds.
	 */
	protected double accumulateDerive(double timeDelta, int eventCount) {
		double timeEvent = eventCount * period();

		double result = timeDelta - timeEvent;
		if (!General.isDoubleGreaterOrEqual(result, 0d)) {
			// events take more time than delta, should never happen
			result = 0d;
		}

		return result;
	}

	protected double period() {
		return 1d / frequency;
	}

	public void frequencySet(double frequency) {
		this.frequency = frequency;
	}

	public void restart() {
		timeStartMillisecond = System.currentTimeMillis();
		timePreviousMillisecond = timeStartMillisecond;
		accumulate = 0d;
	}
}
