package mattjohns.common.math;

/**
 * Only used for advanced operations otherwise might suffer speed issues with
 * too many try catches.
 */
public class MathException extends Exception {
	private static final long serialVersionUID = 1L;

	public MathException(String message) {
		super(message);
	}
}
