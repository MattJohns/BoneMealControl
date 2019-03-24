package mattjohns.minecraft.common.userinterface.style;

import mattjohns.common.immutable.Immutable;

public final class TextFormatCode extends Immutable<TextFormatCode> {
	public static final char PrefixCharacter = 167;
	public static final String Prefix = Character.toString(PrefixCharacter);

	public static final String None = "";

	public static final String Reset = Prefix + "r";

	public static final String Bold = Prefix + "l";
	public static final String Underline = Prefix + "n";
	public static final String Italic = Prefix + "o";

	public static final String Black = Prefix + "0";
	public static final String DarkBlue = Prefix + "1";
	public static final String DarkGreen = Prefix + "2";
	public static final String DarkAqua = Prefix + "3";
	public static final String DarkRed = Prefix + "4";
	public static final String DarkPurple = Prefix + "5";
	public static final String Gold = Prefix + "6";
	public static final String Gray = Prefix + "7";
	public static final String DarkGray = Prefix + "8";
	public static final String Blue = Prefix + "9";
	public static final String Green = Prefix + "a";
	public static final String Aqua = Prefix + "b";
	public static final String Red = Prefix + "c";
	public static final String LightPurple = Prefix + "d";
	public static final String Yellow = Prefix + "e";
	public static final String White = Prefix + "f";

	protected TextFormatCode() {
	}

	public static TextFormatCode of() {
		return new TextFormatCode();
	}

	@Override
	protected TextFormatCode concreteCopy(Immutable<?> source) {
		return new TextFormatCode();
	}
}
