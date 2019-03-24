package mattjohns.minecraft.common.block;

import net.minecraft.util.EnumFacing;

/**
 * Creates an instance of the given class for each of the 6 possible directions.
 * 
 * @param <T>
 * The class to instantiate for each direction.
 */
public abstract class DirectionCompound<T> {
	public T north;
	public T south;
	public T east;
	public T west;
	public T up;
	public T down;

	public void set(EnumFacing direction, T item) {
		switch (direction) {
		case NORTH:
			this.north = item;
			break;
		case SOUTH:
			this.south = item;
			break;
		case EAST:
			this.east = item;
			break;
		case WEST:
			this.west = item;
			break;
		case UP:
			this.up = item;
			break;
		case DOWN:
			this.down = item;
			break;
		}
	}

	// be careful if a copy was intended instead of reference assignment
	public void setAll(T item) {
		north = item;
		south = item;
		east = item;
		west = item;
		up = item;
		down = item;
	}

	public T get(EnumFacing direction) {
		switch (direction) {
		case NORTH:
			return north;
		case SOUTH:
			return south;
		case EAST:
			return east;
		case WEST:
			return west;
		case UP:
			return up;
		case DOWN:
			return down;
		default:
			return null;
		}
	}

	public boolean checkIfExist(EnumFacing direction) {
		return get(direction) != null;
	}

	/**
	 * Override in base classes.  Don't do any deep copying at all,
	 * only copy references across.
	 * 
	 * @param returnValue
	 * The new object that will receive a copy of this object.
	 */
	public void copyVeryShallow(DirectionCompound<T> returnValue) {
		returnValue.north = north;
		returnValue.south = south;
		returnValue.east = east;
		returnValue.west = west;
		returnValue.up = up;
		returnValue.down = down;
	}

	protected abstract int compare(T source, T destination);

	public int compare(DirectionCompound<T> source) {
		int result;

		result = compare(north, source.north);
		if (result != 0)
			return result;
		result = compare(south, source.south);
		if (result != 0)
			return result;
		result = compare(east, source.east);
		if (result != 0)
			return result;
		result = compare(west, source.west);
		if (result != 0)
			return result;
		result = compare(up, source.up);
		if (result != 0)
			return result;
		result = compare(down, source.down);
		if (result != 0)
			return result;

		return 0;
	}

	/**
	 * Deep copy
	 */
	protected abstract T copy(T source);

	/**
	 * Deep copy.
	 */
	public void copyTo(DirectionCompound<T> destination) {
		destination.north = copy(north);
		destination.south = copy(south);
		destination.east = copy(east);
		destination.west = copy(west);
		destination.up = copy(up);
		destination.down = copy(down);
	}
}
