package mattjohns.minecraft.common.userinterface.control.system;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.common.immutable.userinterface.font.Font;
import mattjohns.common.immutable.userinterface.font.FontSymbol;
import mattjohns.common.immutable.userinterface.font.FontSymbolList;
import mattjohns.common.text.TextUtility;
import mattjohns.minecraft.common.userinterface.style.DisplayColor;
import net.minecraft.client.gui.FontRenderer;

public final class ControlFontMinecraftSystem extends ControlFont<ControlFontMinecraftSystem> {
	protected final FontRenderer fontRenderer;

	protected ControlFontMinecraftSystem(FontRenderer fontRenderer) {
		this.fontRenderer = fontRenderer;
	}

	public static ControlFontMinecraftSystem of(FontRenderer fontRenderer) {
		return new ControlFontMinecraftSystem(fontRenderer);
	}

	protected ControlFontMinecraftSystem copy(FontRenderer fontRenderer) {
		return new ControlFontMinecraftSystem(fontRenderer);
	}

	@Override
	protected ControlFontMinecraftSystem concreteCopy(Immutable<?> source) {
		return copy(fontRenderer);
	}

	@Override
	public int sizeY() {
		int result = fontRenderer.FONT_HEIGHT;
		return result;
	}

	@Override
	public int symbolSizeX(char item) {
		int result = fontRenderer.getCharWidth(item);
		return result;
	}

	@Override
	public int symbolSizeY(char text) {
		// assume all characters same height as actual font (might not be true
		// though)
		return sizeY();
	}

	// may be zero
	public int textSizeX(String text) {
		assert text.length() > 0;

		return fontRenderer.getStringWidth(text);
	}

	public int textSizeY(String text) {
		assert text.length() > 0;

		return sizeY();
	}

	/**
	 * Smallest width of any kind of wrapped text. If you try to wrap with a
	 * very small width then the wrap function never ends, because it keeps
	 * trying to fit a character into each line and fails.
	 */
	@Override
	public int wrapMinimumSizeX() {
		// This string should be wider than any single character in the font.

		/// better to just search all characters and find the widest

		final String LargestPossibleCharacterText = "MWQ";

		return fontRenderer.getStringWidth(LargestPossibleCharacterText);
	}

	public int wrapSizeY(String text, int wrapSizeX) {
		return fontRenderer.getWordWrappedHeight(text, wrapSizeX);
	}

	public FontRenderer fontRenderer() {
		return fontRenderer;
	}

	public String textTruncate(String text, int sizeX) {
		return fontRenderer.trimStringToWidth(text, sizeX);
	}

	@Override
	public void textRenderWrap(String text, DisplayBound bound, DisplayColor color) {
		fontRenderer.drawSplitString(text, bound.left(), bound.top(), bound.size().x, color.toInteger());
	}

	@Override
	public void textRender(String text, DisplayBound bound, DisplayColor color, boolean shadowIs) {
		fontRenderer.drawString(text, bound.left(), bound.top(), color.toInteger(), shadowIs);
	}

	/**
	 * Create a font for the user interface models.
	 * 
	 * @param fontRenderer
	 * The Minecraft font renderer to convert. Unicode not supported.
	 */
	@Override
	public Font renderModelCreateFont() {
		// assert display != null;

		int sizeY = sizeY();

		assert sizeY > 0;

		FontSymbolList.Builder symbolBuilder = new FontSymbolList.Builder();

		// just store standard displayable characters (need to change this
		// when going to unicode)
		int numberOfCharacters = TextUtility.CHARACTER_DISPLAYABLE.length();

		final int InitialId = 2;
		int id = InitialId;

		for (int characterIndex = 0; characterIndex < numberOfCharacters; characterIndex++) {
			char character = TextUtility.CHARACTER_DISPLAYABLE.charAt(characterIndex);

			int sizeX = symbolSizeX(character);
			DisplaySize size = DisplaySize.of(sizeX, sizeY);

			FontSymbol symbol = FontSymbol.of(id, character, size);
			symbolBuilder.add(symbol);

			id++;
		}

		// tab
		final int TabNumberOfSpace = 4;
		String TabAsSpace = "";
		for (int i = 0; i < TabNumberOfSpace; i++) {
			TabAsSpace += " ";
		}

		char character = TextUtility.TAB;
		int sizeX = symbolSizeX(' ') * TabNumberOfSpace;
		DisplaySize size = DisplaySize.of(sizeX, sizeY);

		// use space for display otherwise you get control code showing
		FontSymbol symbol = FontSymbol.of(id, character, size)
				.withDisplayText(TabAsSpace);
		symbolBuilder.add(symbol);

		id++;

		FontSymbolList symbolList = symbolBuilder.build();

		return Font.of(symbolList, sizeY);
	}
}
