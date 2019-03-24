package mattjohns.common.immutable.userinterface.display;

import mattjohns.common.immutable.Immutable;

public final class DisplayPadding extends Immutable<DisplayPadding> {
	public static final DisplayPadding None = DisplayPadding.ofEach(0, 0);

	public final int left;
	public final int right;
	public final int top;
	public final int bottom;

	protected DisplayPadding(int left, int right, int top, int bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;

		assert this.left >= 0;
		assert this.right >= 0;
		assert this.top >= 0;
		assert this.bottom >= 0;
	}

	public static DisplayPadding of(int left, int right, int top, int bottom) {
		return new DisplayPadding(left, right, top, bottom);
	}

	public static DisplayPadding ofEach(int xEach, int yEach) {
		return new DisplayPadding(xEach, xEach, yEach, yEach);
	}

	public static DisplayPadding ofEach(int each) {
		return new DisplayPadding(each, each, each, each);
	}

	@Override
	protected DisplayPadding concreteCopy(Immutable<?> source) {
		return new DisplayPadding(left, right, top, bottom);
	}

	public DisplayPadding add(DisplayPadding item) {
		return DisplayPadding.of(left + item.left, right + item.right, top + item.top, bottom + item.bottom);
	}

	public int xTotal() {
		return left + right;
	}

	public int yTotal() {
		return top + bottom;
	}

	public DisplayBound bound(DisplaySize parent) {
		return DisplayBound.of(topLeft(), size(parent));
	}

	public DisplayPosition topLeft() {
		return DisplayPosition.of(left, top);
	}

	public DisplayPosition bottomRight() {
		return DisplayPosition.of(right, bottom);
	}

	public DisplaySize size(DisplaySize parent) {
		return DisplaySize.of(parent.x - xTotal(), parent.y - yTotal());
	}

	public DisplaySize sizeTotal() {
		return DisplaySize.of(xTotal(), yTotal());
	}

	public DisplayPadding largest(DisplayPadding source) {
		int top = Math.max(this.top, source.top);
		int left = Math.max(this.left, source.left);
		int bottom = Math.max(this.bottom, source.bottom);
		int right = Math.max(this.right, source.right);

		return DisplayPadding.of(left, right, top, bottom);
	}

}
