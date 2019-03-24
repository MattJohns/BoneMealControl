package mattjohns.common.list;

import java.util.ArrayList;

public class Queue<TElement> {
	protected ArrayList<TElement> internalList;
	
	protected Queue() {
		internalList = new ArrayList<>();
	}
	
	public static <TElement> Queue<TElement> of() {
		return new Queue<TElement>();
	}
	
	public int size() {
		return internalList.size();
	}
	
	public boolean isEmpty() {
		return internalList.isEmpty();
	}
	
	public TElement consume() {
		assert !isEmpty();
		
		TElement result = internalList.get(0);
		
		internalList.remove(0);
		
		return result;
	}
	
	public TElement peek() {
		assert !isEmpty();
		
		return internalList.get(0);
	}
	
	public void append(TElement item) {
		internalList.add(item);
	}
}
