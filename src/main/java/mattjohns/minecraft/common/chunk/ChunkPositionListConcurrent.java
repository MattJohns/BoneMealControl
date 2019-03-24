package mattjohns.minecraft.common.chunk;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkPositionListConcurrent extends ChunkPositionList {
	@Override
	protected Set<ChunkPosSortable> positionListCreate() {
		 return ConcurrentHashMap.newKeySet();
	}
}
