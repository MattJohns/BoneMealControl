package mattjohns.common.list;

public class IdList extends IntegerList {
	private static final long serialVersionUID = 1L;

	protected IdList() {
	}

	public static IdList of() {
		return new IdList();
	}

	public IdList intersect(IntegerList sourceList) {
		IntegerList result = super.intersect(sourceList);
		
		IdList castResult = IdList.of();
		
		castResult.addAll(result);
		
		return castResult;
	}
}
