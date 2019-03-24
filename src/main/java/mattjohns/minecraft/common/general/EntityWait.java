package mattjohns.minecraft.common.general;

/**
 * An object that gets waited on. The object should have an id associated with
 * it (hence 'entity').
 */
public class EntityWait<TEntity> {
	// just to prevent overflow of integer
	protected static final int WaitTimeCapMillisecond = 1000000000;

	protected int id;

	protected long startTime;

	protected TEntity entity;

	protected boolean isRemove = false;

	protected EntityWait(int id, TEntity entity) {
		this.id = id;
		this.entity = entity;

		start();
	}

	protected void start() {
		startTime = System.currentTimeMillis();
	}

	public static <TEntity> EntityWait<TEntity> of(int id, TEntity entity) {
		return new EntityWait<>(id, entity);
	}

	public int id() {
		return id;
	}

	public TEntity entity() {
		return entity;
	}

	public long startTime() {
		return startTime;
	}

	// don't call if ended
	public int waitTime(long currentTime) {
		long delta = currentTime - startTime;
		if (delta < 0) {
			return 0;
		}

		// cap just makes the cast safe
		if (delta > startTime) {
			delta = startTime;
		}

		return (int) delta;
	}

	public boolean isRemove() {
		return isRemove;
	}

	public void removeSet() {
		isRemove = true;
	}
}
