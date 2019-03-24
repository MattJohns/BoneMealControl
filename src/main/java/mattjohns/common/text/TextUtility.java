package mattjohns.common.text;

import mattjohns.common.immutable.list.ListImmutable;

public class TextUtility {
	public static final String CHARACTER_DISPLAYABLE_ALPHA = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; 
	public static final String CHARACTER_DISPLAYABLE_SYMBOL = "~`!@#$%^&*()-=_+[]{}\\|;':\",.<>/?"; 
	public static final String CHARACTER_DISPLAYABLE = CHARACTER_DISPLAYABLE_ALPHA + CHARACTER_DISPLAYABLE_SYMBOL;

	public static final char UnicodeUpArrow = 9650;
	public static final char UnicodeDownArrow = 9660;
	
	public static final char UnicodeUpArrowAndTail = 11014;
	public static final char UnicodeDownArrowAndTail = 11015;
	
	public static final char CARRIAGE_RETURN = '\n';
	public static final char TAB = '\t';

	public static boolean isDisplayable(char character) {
		return CHARACTER_DISPLAYABLE.contains(Character.toString(character));
	}
	
	public static ListImmutable<String> splitEnter(String source) {
		ListImmutable.Builder<String> builder = ListImmutable.Builder.of();
		
		int baseIndex = 0;
		
		int nextCrIndex = source.indexOf(CARRIAGE_RETURN, baseIndex);
		while (nextCrIndex != -1) {
			assert nextCrIndex >= 0;
			assert nextCrIndex < source.length();

			// exclusive end
			String lineText = source.substring(baseIndex, nextCrIndex);

			//// strip off any LF, maybe also ensure it's not just LF and no CR as separator
			
			// line can be empty
			builder.add(lineText);

			baseIndex = nextCrIndex + 1;
			
			nextCrIndex = source.indexOf(CARRIAGE_RETURN, baseIndex);
		}
		
		// last part
		if (baseIndex <= source.length()) {
			// still some left
			String lineText = source.substring(baseIndex, source.length() + 0);

			builder.add(lineText);
		}
		
		return builder.build();
	}
}
