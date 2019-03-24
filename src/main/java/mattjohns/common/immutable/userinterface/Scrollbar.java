package mattjohns.common.immutable.userinterface;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.math.geometry.dimension1.RangeDouble;
import mattjohns.common.immutable.userinterface.display.DisplaySize;

/**
 * Just handles trough and thumb, not buttons. So size refers to trough size and
 * not the whole scrollbar control size.
 */
public final class Scrollbar extends Immutable<Scrollbar> {
	protected final int documentSizeY;
	protected final int viewportSizeY;
	protected final int elementSizeY;
	protected final int troughSizeY;
	protected final int thumbSizeYMinimum;

	public final int scrollOffset;

	protected Scrollbar(int documentSizeY, int viewportSizeY, int elementSizeY, int troughSizeY, int thumbSizeYMinimum,
			int scrollOffset) {
		this.documentSizeY = documentSizeY;
		this.viewportSizeY = viewportSizeY;
		this.elementSizeY = elementSizeY;
		this.troughSizeY = troughSizeY;
		this.thumbSizeYMinimum = thumbSizeYMinimum;
		this.scrollOffset = scrollOffset;

		assert this.documentSizeY >= 0;
		assert this.viewportSizeY > 0;
		assert this.elementSizeY > 0;
		assert this.troughSizeY > 0;
		assert this.thumbSizeYMinimum > 0;

		assert offsetCheck(this.scrollOffset);
	}

	public static Scrollbar of(int scrollbarElementSizeY, int thumbSizeYMinimum) {
		int documentSizeY = 0;
		int contentSizeY = 1;
		int troughSizeY = 1;
		int offset = 0;

		return new Scrollbar(documentSizeY, contentSizeY, scrollbarElementSizeY, troughSizeY, thumbSizeYMinimum,
				offset);
	}

	public static Scrollbar of(int documentSizeY, int viewportSizeY, int elementSizeY, int troughSizeY,
			int thumbSizeYMinimum) {
		int scrollOffset = 0;

		return new Scrollbar(documentSizeY, viewportSizeY, elementSizeY, troughSizeY, thumbSizeYMinimum, scrollOffset);
	}

	@Override
	protected Scrollbar concreteCopy(Immutable<?> source) {
		return new Scrollbar(documentSizeY, viewportSizeY, elementSizeY, troughSizeY, thumbSizeYMinimum, scrollOffset);
	}

	public Scrollbar withOffsetTranslate(int delta) {
		return withOffsetCap(scrollOffset + delta);
	}

	public Scrollbar withOffsetCap(int offset) {
		return withOffsetCap(documentSizeY, viewportSizeY, offset);
	}

	public Scrollbar withOffsetCap(int documentSizeY, int contentSizeY, int offset) {
		int offsetListSize = offsetListSize(documentSizeY, contentSizeY, elementSizeY);

		int offsetCap = offsetCap(offset, offsetListSize);

		return new Scrollbar(documentSizeY, contentSizeY, elementSizeY, troughSizeY, thumbSizeYMinimum, offsetCap);
	}

	public Scrollbar withTroughSizeY(int troughSizeY) {
		return new Scrollbar(documentSizeY, viewportSizeY, elementSizeY, troughSizeY, thumbSizeYMinimum, scrollOffset);
	}

	public Scrollbar withEmpty() {
		int documentSizeY = 0;
		int offset = 0;

		return new Scrollbar(documentSizeY, viewportSizeY, elementSizeY, troughSizeY, thumbSizeYMinimum, offset);
	}

	public boolean offsetCheck(int offset) {
		if (offsetListSize() <= 1) {
			// offset must always be zero if nowhere to scroll
			return offset == 0;
		}

		return (offset >= 0) && (offset < offsetListSize());
	}

	public int offsetCap(int source) {
		return offsetCap(source, offsetListSize());
	}

	public static int offsetCap(int source, int offsetListSize) {
		if (offsetListSize <= 0) {
			return 0;
		}

		if (source < 0) {
			return 0;
		}

		if (source > offsetListSize - 1) {
			return offsetListSize - 1;
		}

		return source;
	}

	public int offsetListSize() {
		return offsetListSize(documentSizeY, viewportSizeY, elementSizeY);
	}

	public static int offsetListSize(int documentSizeY, int contentSizeY, int scrollbarElementSizeY) {
		int documentOverlapSizeY = documentSizeY - contentSizeY;

		if (documentOverlapSizeY < 1) {
			// document fits within viewport, nothing to scroll
			return 1;
		}

		int elementListSize = documentOverlapSizeY / scrollbarElementSizeY;

		if ((documentOverlapSizeY % scrollbarElementSizeY) > 0) {
			// one partial offset at the end
			elementListSize += 1;
		}

		elementListSize++;

		return elementListSize;
	}

	public boolean isEmpty() {
		return documentSizeY == 0;
	}

	public int thumbSizeY() {
		if (isEmpty()) {
			return troughSizeY;
		}

		// thumb height is basically trough height divided by number of possible
		// offset list positions

		// normalized height
		double thumbSizeYNormalize = 1d / (double)offsetListSize();

		assert RangeDouble.Unit.isContain(thumbSizeYNormalize);

		// expand into actual height relative to trough
		int result = (int)((double)troughSizeY * thumbSizeYNormalize);

		// shouldn't need capping but do it anyway
		result = thumbSizeYCap(result);

		return result;
	}

	protected int thumbSizeYCap(int source) {
		if (source < thumbSizeYMinimum) {
			return thumbSizeYMinimum;
		}

		if (source > troughSizeY) {
			return troughSizeY;
		}

		return source;
	}

	public int offsetToThumbPositionY() {
		if (offsetListSize() <= 1) {
			// nowhere for thumb to move, must be at offset 0
			return 0;
		}

		// position is just the offset effectively
		double positionNormalize = (double)scrollOffset / (double)(offsetListSize() - 1);

		// scale to actual trough size
		double resultDouble = (double)thumbPositionYMaximum() * positionNormalize;

		// shouldn't need capping but do it anyway
		return thumbPositionYCap((int)resultDouble);
	}

	protected int thumbPositionYMaximum() {
		if (offsetListSize() <= 1) {
			// nowhere for thumb to move, must be at offset 0
			return 0;
		}

		if (troughSizeY <= 1) {
			// trough too small to bother
			return 0;
		}

		int proposed = (troughSizeY - thumbSizeY()) - 0;

		////// badly named, more of a trough position cap
		return thumbPositionYCap(proposed);
	}

	protected int thumbPositionYCap(int source) {
		if (source < 0) {
			return 0;
		}

		//// shouldn't this subtract thumb size?

		if (source > troughSizeY - 1) {
			return troughSizeY - 1;
		}

		return source;
	}

	/**
	 * Relative to top left of trough.
	 */
	public int troughPositionYToOffsetCap(int troughPositionY) {
		assert troughPositionYCheck(troughPositionY);

		if (offsetListSize() <= 1) {
			// nowhere for thumb to move, must be at offset 0
			return 0;
		}

		if (troughSizeY <= 1) {
			// trough too small to divide
			return 0;
		}

		// Convert trough position to thumb. If further than thumb can go (due
		// to thumb's height)
		// then just cap it.
		int newThumbPositionY = thumbPositionYCap(troughPositionY);

		int thumbPositionYMaximum = thumbPositionYMaximum();
		if (thumbPositionYMaximum < 1) {
			// thumb can't move
			return 0;
		}

		double positionYNormalize = (double)newThumbPositionY / (double)thumbPositionYMaximum;

		double offsetDouble = (double)offsetListSize() * positionYNormalize;

		int offset = (int)offsetDouble + 1;

		return offsetCap(offset);
	}

	/**
	 * Rounds down.
	 */
	public int elementPerPage() {
		if (elementSizeY < 1) {
			return 0;
		}

		return viewportSizeY / elementSizeY;
	}

	public int troughPositionYCap(int source) {
		if (source < 0) {
			return 0;
		}

		if (troughSizeY < 1) {
			// trough has no size, should never happen
			return 0;
		}

		if (source > troughSizeY - 1) {
			return troughSizeY - 1;
		}

		return source;
	}

	public boolean troughPositionYCheck(int item) {
		boolean isCap = (item == troughPositionYCap(item));
		return isCap;
	}

	public DisplaySize troughSizeMinimum() {
		return DisplaySize.of(1, thumbSizeYMinimum);
	}

	public int documentScrollSizeY() {
		return documentScrollSizeY(documentSizeY, viewportSizeY, elementSizeY, scrollOffset);
	}

	protected static int documentScrollSizeY(int documentSizeY, int contentSizeY, int scrollbarElementSizeY,
			int scrollOffset) {
		if (documentSizeY < 1) {
			// not enough content to ever require scrolling
			return 0;
		}

		int result = scrollOffset * scrollbarElementSizeY;

		result = documentScrollSizeYCap(result, documentSizeY, contentSizeY);

		assert documentScrollSizeYCheck(result, documentSizeY, contentSizeY);

		return result;
	}

	protected static boolean documentScrollSizeYCheck(int item, int documentSizeY, int contentSizeY) {
		if (item < 0) {
			return false;
		}

		int maximumScrollPositionY = documentMaximumScrollSizeY(documentSizeY, contentSizeY);
		if (item > maximumScrollPositionY) {
			return false;
		}

		return true;
	}

	protected static int documentScrollSizeYCap(int source, int documentSizeY, int contentSizeY) {
		if (source < 0) {
			return 0;
		}

		int maximumScrollPositionY = documentMaximumScrollSizeY(documentSizeY, contentSizeY);
		if (source > maximumScrollPositionY) {
			return maximumScrollPositionY;
		}

		return source;
	}

	protected static int documentMaximumScrollSizeY(int documentSizeY, int contentSizeY) {
		int result = documentSizeY - contentSizeY;

		if (result < 0) {
			result = 0;
		}

		return result;
	}
}