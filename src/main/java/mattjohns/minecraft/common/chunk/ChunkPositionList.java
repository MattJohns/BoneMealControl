package mattjohns.minecraft.common.chunk;

import java.util.HashSet;
import java.util.Set;

import mattjohns.common.math.Vector2I;

public class ChunkPositionList {
	Set<ChunkPosSortable> positionList;

	public ChunkPositionList() {
		positionList = positionListCreate();
	}

	public boolean contains(ChunkPosSortable item) {
		return positionList.contains(item);
	}
	
	protected Set<ChunkPosSortable> positionListCreate() {
		 return new HashSet<>();
	}
	
	public void addUnique(ChunkPosSortable item) {
		positionList.add(item);
	}

	public boolean take(ChunkPosSortable position) {
		return positionList.remove(position);
	}

	public void clear() {
		positionList.clear();
	}

	public void remove(ChunkPosSortable position) {
		positionList.remove(position);
	}

	public ChunkPosSortable takeClosest(ChunkPosSortable position, int radius) {
		ChunkPosSortable closest = getClosest(position, radius);
		if (closest == null) {
			return null;
		}

		if (!take(closest)) {
			// somehow wasn't in the list, don't bother with it
			return null;
		}

		return closest;
	}
	
	// square radius
	public ChunkPosSortable getClosest(ChunkPosSortable position, int radius) {
		ChunkPosSortable closestPosition = null;
		double closestDistance = 0;

		for (ChunkPosSortable candidate : positionList) {
			
			Vector2I delta = position.subtract(candidate);
			delta = delta.absolute();
			
			if (delta.x <= radius && delta.y <= radius) {
				double distanceDirect = delta.length();
				
				if (closestPosition == null || distanceDirect < closestDistance) {
					closestPosition = candidate;
					closestDistance = distanceDirect;
				}
			}
		}

		if (closestPosition == null) {
			return null;
		}

		// make a copy

		/// need to work out a better way than copying these structs
		return new ChunkPosSortable(closestPosition);
	}
}
