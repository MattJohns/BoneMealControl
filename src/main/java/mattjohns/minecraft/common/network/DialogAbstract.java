package mattjohns.minecraft.common.network;

/**
 * Performs server requests in a custom sequence. 
 */
public abstract class DialogAbstract {
	protected int id;

	protected boolean isStart = false;
	protected boolean isEnd = false;

	protected boolean isError = false;

	protected DialogAbstract(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}

	public boolean isStart() {
		return isStart;
	}

	public boolean isEnd() {
		if (isEnd) {
			assert isStart : "Never started.";
		}

		return isEnd;
	}

	public void start() {
		assert !isStart;
		assert !isEnd;

		isStart = true;
		isEnd = false;
		isError = false;
	}

	protected void end() {
		assert !isEnd;
		assert isStart;

		isEnd = true;

		notifyEnd();
	}

	/**
	 * Needs to be defined by concrete class because there will be unique
	 * parameters sent to the listener.
	 */
	protected abstract void notifyEnd();

	public boolean isError() {
		return isError;
	}

	public void errorSet() {
		isError = true;
	}
}