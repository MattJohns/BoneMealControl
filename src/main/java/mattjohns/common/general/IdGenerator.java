package mattjohns.common.general;

public class IdGenerator {
	// start at 2 just to prove the value isn't a mistake (e.g. an uninitialized
	// id field in an object)
	protected int nextFreeId = 2;

	protected IdGenerator() {
	}
	
	public static IdGenerator of() {
		return new IdGenerator();
	}
	
	public int consume() {
		int result = nextFreeId;
		nextFreeId++;

		return result;
	}
}
