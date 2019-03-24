package mattjohns.minecraft.common.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Keeps a list of entities that are being waited on. 'Entity' here means an
 * object with an id as opposed to a minecraft entity.
 */
public class EntityWaitList<TEntity> {
	protected int tickSinceUpdate = 0;

	protected HashMap<Integer, EntityWait<TEntity>> internalMap = new HashMap<>();

	protected Consumer<EntityWait<TEntity>> consumeTimeout;

	protected int timeOutMillsecond;
	protected int updateTickPeriod;

	public EntityWaitList(int timeOutMillisecond, int updateTickPeriod, Consumer<EntityWait<TEntity>> consumeTimeout) {
		this.consumeTimeout = consumeTimeout;
		this.timeOutMillsecond = timeOutMillisecond;
		this.updateTickPeriod = updateTickPeriod;
	}

	public void tick() {
		tickSinceUpdate++;

		if (tickSinceUpdate >= updateTickPeriod) {
			tickSinceUpdate = 0;

			long currentTime = System.currentTimeMillis();

			update(currentTime);
		}
	}

	protected void update(long currentTime) {
		for (EntityWait<TEntity> item : internalMap.values()) {
			// don't include deleted items
			if (item.isRemove) {
				continue;
			}

			int waitTime = item.waitTime(currentTime);

			if (waitTime >= timeOutMillsecond) {
				// procedure timeout
				onTimeout(item);
			}
		}

		ArrayList<Integer> removeIdList = new ArrayList<>();

		for (EntityWait<TEntity> item : internalMap.values()) {
			if (item.isRemove()) {
				removeIdList.add(item.id);
			}
		}

		for (int removeId : removeIdList) {
			this.removeId(removeId);
		}
	}

	protected void onTimeout(EntityWait<TEntity> item) {
		consumeTimeout.accept(item);
	}

	public Optional<EntityWait<TEntity>> longestWaitItem(long currentTime) {
		int longestTime = 0;
		Optional<EntityWait<TEntity>> longestItem = Optional.empty();

		for (EntityWait<TEntity> item : internalMap.values()) {
			// don't include deleted items
			if (item.isRemove) {
				continue;
			}
			
			int waitTime = item.waitTime(currentTime);

			if (waitTime > longestTime) {
				longestTime = waitTime;
				longestItem = Optional.of(item);
			}
		}

		return longestItem;
	}

	public int longestWaitTime(long currentTime) {
		Optional<EntityWait<TEntity>> longestItem = longestWaitItem(currentTime);
		if (!longestItem.isPresent()) {
			return 0;
		}

		return longestItem.get().waitTime(currentTime);
	}

	public void add(int id, TEntity entity) {
		EntityWait<TEntity> wait = new EntityWait<TEntity>(id, entity);

		internalMap.put(id, wait);
	}

	protected boolean removeId(int id) {
		return internalMap.remove(id) != null;
	}

	public boolean removeSetById(int id) {
		if (!isContainId(id)) {
			return false;
		}

		waitGetById(id).removeSet();

		return true;
		// return internalMap.remove(id) != null;
	}

	public boolean isContainId(int id) {
		return internalMap.containsKey(id);
	}

	public int size() {
		return internalMap.size();
	}

	public TEntity entityGetById(int id) {
		return waitGetById(id).entity();
	}

	public EntityWait<TEntity> waitGetById(int id) {
		return internalMap.get(id);
	}
}
