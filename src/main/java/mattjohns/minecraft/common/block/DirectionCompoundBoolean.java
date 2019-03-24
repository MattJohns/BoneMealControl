package mattjohns.minecraft.common.block;

import net.minecraft.util.EnumFacing;

/**
 * Can't use DirectionCompound because generics can't be used with value types.
 * So need this class which is a copy of it.
 */
public class DirectionCompoundBoolean {
	public boolean north;
	public boolean south;
	public boolean east;
	public boolean west;
	public boolean up;
	public boolean down;

	public DirectionCompoundBoolean() {
		setAll(false);
	}

	public boolean set(EnumFacing direction, boolean item) {
		boolean stateOld = get(direction);
		if (item == stateOld)
			return false;

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

		return true;
	}

	public void setAll(boolean item) {
		north = item;
		south = item;
		east = item;
		west = item;
		up = item;
		down = item;
	}

	public boolean get(EnumFacing direction) {
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
			return false;/////
		}
	}

	public DirectionCompoundBoolean copy() {
		DirectionCompoundBoolean returnValue = new DirectionCompoundBoolean();
		copyTo(returnValue);
		return returnValue;
	}

	protected void copyTo(DirectionCompoundBoolean destination) {
		destination.north = north;
		destination.south = south;
		destination.east = east;
		destination.west = west;
		destination.up = up;
		destination.down = down;
	}

	public boolean equals(DirectionCompoundBoolean source) {
		if (north != source.north)
			return false;
		if (south != source.south)
			return false;
		if (east != source.east)
			return false;
		if (west != source.west)
			return false;
		if (up != source.up)
			return false;
		if (down != source.down)
			return false;

		return true;
	}
}
