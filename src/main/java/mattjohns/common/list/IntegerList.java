package mattjohns.common.list;

import java.util.ArrayList;

public class IntegerList extends ArrayList<Integer> {
	private static final long serialVersionUID = 1L;
	
	protected IntegerList() {
	}
	
	public static IntegerList of() {
		return new IntegerList();
	}
	
	public boolean contains(int item) {
		for (Integer listItem : this) {
			if (listItem == item) {
				return true;
			}
		}
		
		return false;
	}

	public boolean containAny(IntegerList criteriaList) {
		for (Integer listItem : this) {
			if (criteriaList.contains(listItem)) {
				return true;
			}
		}
		
		return false;
	}
	
	public IntegerList intersect(IntegerList sourceList) {
		IntegerList result = IntegerList.of();
		
		for (Integer listItem : this) {
			if (sourceList.contains(listItem)) {
				result.add(listItem);
			}
		}
		
		return result;
	}
}
