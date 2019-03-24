package mattjohns.minecraft.common.userinterface.device;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.Optional;

import org.lwjgl.opengl.GL11;

import mattjohns.common.immutable.list.StackImmutable;
import mattjohns.common.immutable.math.geometry.dimension2.VectorDouble;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPadding;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.minecraft.common.userinterface.control.system.ControlFont;
import mattjohns.minecraft.common.userinterface.style.DisplayColor;
import mattjohns.minecraft.common.userinterface.style.StyleBorder;
import mattjohns.minecraft.common.userinterface.style.StyleGradient;

// shouldn't really have style stuff in here, should be independent display methods
public class DeviceDisplay extends Device {
	protected final DisplayGui gui;

	protected StackImmutable<DisplayBound> cropStack;

	protected DeviceDisplay() {
		this.gui = new DisplayGui();

		this.cropStack = StackImmutable.of();

		assert this.gui != null;
		assert this.cropStack != null;
	}

	public static DeviceDisplay of() {
		return new DeviceDisplay();
	}

	protected Minecraft minecraft() {
		return Minecraft.getMinecraft();
	}

	public void renderPoint(DisplayPosition position, DisplayColor color) {
		renderRectangleSolid(DisplayBound.of(position, DisplaySize.of(1, 1)), color);
	}

	public void renderLineX(DisplayPosition left, int sizeX, DisplayColor color) {
		if (sizeX <= 0) {
			return;
		}

		DisplaySize size = DisplaySize.of(sizeX, 1);

		renderRectangleSolid(DisplayBound.of(left, size), color);
	}

	public void renderLineY(DisplayPosition top, int sizeY, DisplayColor color) {
		if (sizeY <= 0) {
			return;
		}

		DisplaySize size = DisplaySize.of(1, sizeY);

		renderRectangleSolid(DisplayBound.of(top, size), color);
	}

	public void renderRectangleOutline(DisplayBound bound, DisplayColor color) {
		renderRectangleOutline(bound, color, Optional.empty());
	}

	public void renderRectangleOutline(DisplayBound bound, DisplayColor color, Optional<DisplayBound> cropBound) {
		cropStart(cropBound);

		// top
		renderLineX(bound.topLeft, bound.sizeX(), color);

		// left
		renderLineY(bound.topLeft, bound.sizeY(), color);

		// bottom
		renderLineX(bound.bottomLeftInclusive(), bound.sizeX(), color);

		// right
		renderLineY(bound.topRightInclusive(), bound.sizeY(), color);

		cropStop(cropBound);
	}

	public void renderRectangleSolid(DisplayBound bound, DisplayColor color) {
		renderRectangleSolid(bound, color, Optional.empty());
	}

	public void renderRectangleSolid(DisplayBound bound, DisplayColor color, Optional<DisplayBound> cropBound) {
		// cropBound = Optional.of(DisplayBound.of(250, 51, 200, 101));/////

		cropStart(cropBound);

		Gui.drawRect(bound.left(), bound.top(), bound.rightExclusive(), bound.bottomExclusive(), color.toInteger());

		cropStop(cropBound);
	}

	public void renderText(String text, ControlFont<?> font, DisplayBound bound, DisplayColor color) {
		renderText(text, font, bound, color, false, false, false, Optional.empty());
	}

	public void renderText(String text, ControlFont<?> font, DisplayBound bound, DisplayColor color, boolean wrapIs,
			boolean shadowIs, boolean trimIs, Optional<DisplayBound> cropBound) {
		cropStart(cropBound);

		if (wrapIs) {
			font.textRenderWrap(text, bound, color);
			// fontRenderer.drawSplitString(text, bound.left(), bound.top(),
			// bound.size().x, color.toInteger());
		}
		else {

			String trimText;
			if (trimIs) {
				trimText = font.textTruncate(text, bound.size().x);
				// trimText = fontRenderer.trimStringToWidth(text,
				// bound.size().x);
			}
			else {
				trimText = text;
			}

			font.textRender(trimText, bound, color, shadowIs);
			// fontRenderer.drawString(trimText, bound.left(), bound.top(),
			// color.toInteger(), shadowIs);
		}

		cropStop(cropBound);
	}

	/**
	 * Uses a stack to remember existing crops. If the new crop is outside the
	 * existing crop then that part doesn't get drawn. In other words all child
	 * crop operations are limited to the area of their parent crops.
	 * 
	 * Returns true if everything is cropped and no drawing should take place
	 * until the next cropStop().
	 */
	public boolean cropStart(Optional<DisplayBound> cropBound) {
		if (cropBound.isPresent()) {
			if (cropStack.isEmpty()) {
				// no existing crops
				cropStack = cropStack.withPush(cropBound.get());
			}
			else {
				// already cropping, only crop within the existing crop
				DisplayBound existingCrop = cropStack.top();

				if (!cropBound.get()
						.isIntersect(existingCrop)) {
					// don't intersect, need to display nothing
					return true;
				}

				DisplayBound cropIntersect = existingCrop.withIntersect(cropBound.get());

				// don't push the given crop bound, instead push the
				// intersection with the existing crop
				cropStack = cropStack.withPush(cropIntersect);

				// stop old crop
				cropStopDirect();
			}

			cropStartDirect(cropStack.top());
		}

		return false;
	}

	public void cropStop(Optional<DisplayBound> cropBound) {
		if (cropBound.isPresent()) {
			assert !cropStack.isEmpty() : "Attempt to stop crop without corresponding start.";

			cropStack = cropStack.withPopDiscard();

			cropStopDirect();

			if (!cropStack.isEmpty()) {
				// there was a crop in progress before this one was pushed,
				// restore it
				cropStartDirect(cropStack.top());
			}
		}
	}

	protected void cropStartDirect(DisplayBound cropBound) {
		GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		DisplayBound cropBoundGl = boundMinecraftToGl(cropBound);

		GL11.glScissor(cropBoundGl.left(), cropBoundGl.top(), cropBoundGl.sizeX(), cropBoundGl.sizeY());
	}

	protected void cropStopDirect() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glPopAttrib();
	}

	protected VectorDouble dimensionMinecraftToActualScale() {
		ScaledResolution minecraftScreenSize = new ScaledResolution(minecraft());

		double scaleFactor = minecraftScreenSize.getScaleFactor();

		return VectorDouble.of(scaleFactor, scaleFactor);
	}

	protected DisplaySize screenSizeActual() {
		return DisplaySize.of(minecraft().displayWidth, minecraft().displayHeight);
	}

	protected DisplaySize screenSize() {
		ScaledResolution minecraftScreenSize = new ScaledResolution(minecraft());

		int sizeX = minecraftScreenSize.getScaledWidth();
		if (sizeX < 1) {
			// resolution somehow bad, at least return a valid size
			sizeX = 1;
		}

		int sizeY = minecraftScreenSize.getScaledHeight();
		if (sizeY < 1) {
			// resolution somehow bad, at least return a valid size
			sizeY = 1;
		}

		return DisplaySize.of(sizeX, sizeY);
	}

	protected int positionMinecraftXToActual(int minecraftX) {
		double actualX = (double)minecraftX * dimensionMinecraftToActualScale().x;
		return (int)actualX;
	}

	protected int positionMinecraftYToActual(int minecraftY) {
		double actualY = (double)minecraftY * dimensionMinecraftToActualScale().y;
		return (int)actualY;
	}

	protected DisplayPosition positionMinecraftToActual(DisplayPosition minecraft) {
		return DisplayPosition.of(positionMinecraftXToActual(minecraft.x), positionMinecraftYToActual(minecraft.y));
	}

	protected int sizeMinecraftXToActual(int minecraftX) {
		double actualX = (double)minecraftX * dimensionMinecraftToActualScale().x;
		return (int)actualX;
	}

	protected int sizeMinecraftYToActual(int minecraftY) {
		double actualY = (double)minecraftY * dimensionMinecraftToActualScale().y;
		return (int)actualY;
	}

	protected DisplaySize sizeMinecraftToActual(DisplaySize minecraft) {
		return DisplaySize.of(sizeMinecraftXToActual(minecraft.x), sizeMinecraftYToActual(minecraft.y));
	}

	protected DisplayBound boundMinecraftToActual(DisplayBound minecraft) {
		return DisplayBound.of(positionMinecraftToActual(minecraft.topLeft), sizeMinecraftToActual(minecraft.size()));
	}

	protected int positionActualXToGl(int actualX) {
		return actualX;
	}

	protected int positionActualYToGl(int actualY) {
		// gl screen y is flipped
		return screenSizeActual().y - actualY;
	}

	protected DisplayPosition positionActualToGl(DisplayPosition actual) {
		return DisplayPosition.of(positionActualXToGl(actual.x), positionActualYToGl(actual.y));
	}

	protected int sizeActualXToGl(int actualX) {
		return actualX;
	}

	protected int sizeActualYToGl(int actualY) {
		return actualY;
	}

	protected DisplaySize sizeActualToGl(DisplaySize actual) {
		return DisplaySize.of(sizeActualXToGl(actual.x), sizeActualYToGl(actual.y));
	}

	protected DisplayBound boundActualToGl(DisplayBound actual) {
		// Note that gl treats bounds as bottom left up to top right, rather
		// than top left to bottom right. So just use bottom left instead of
		// bottom right to convert.
		DisplayPosition positionGl = positionActualToGl(actual.bottomLeftExclusive());
		DisplaySize sizeGl = actual.size();

		return DisplayBound.of(positionGl, sizeGl);
	}

	protected int positionMinecraftXToGl(int minecraftX) {
		int actual = positionMinecraftXToActual(minecraftX);
		return positionActualXToGl(actual);
	}

	protected int positionMinecraftYToGl(int minecraftY) {
		int actual = positionMinecraftYToActual(minecraftY);
		return positionActualYToGl(actual);
	}

	/**
	 * Warning: Don't use this when converting bounds as gl treats them from
	 * bottom left to top right. Use boundMinecraftToGl() instead.
	 */
	protected DisplayPosition positionMinecraftToGl(DisplayPosition minecraft) {
		DisplayPosition actual = positionMinecraftToActual(minecraft);
		return positionActualToGl(actual);
	}

	protected int sizeMinecraftXToGl(int minecraftX) {
		int actual = sizeMinecraftXToActual(minecraftX);
		return sizeActualXToGl(actual);
	}

	protected int sizeMinecraftYToGl(int minecraftY) {
		int actual = sizeMinecraftYToActual(minecraftY);
		return sizeActualYToGl(actual);
	}

	protected DisplaySize sizeMinecraftToGl(DisplaySize minecraft) {
		DisplaySize actual = sizeMinecraftToActual(minecraft);
		return sizeActualToGl(actual);
	}

	protected DisplayBound boundMinecraftToGl(DisplayBound minecraft) {
		DisplayBound actual = boundMinecraftToActual(minecraft);
		return boundActualToGl(actual);
	}

	public void renderGradient(DisplayBound bound, DisplayColor color1, DisplayColor color2,
			StyleGradient.EnumType type) {
		switch (type) {
		case TopToBottom: {
			gui.drawGradientRect(bound.left(), bound.top(), bound.rightExclusive(), bound.bottomExclusive(),
					color1.toInteger(), color2.toInteger());
			break;
		}

		default: {
			break;
		}
		}
	}

	public void renderTexture(DisplayBound boundAbsolute, ResourceLocation texture) {

		Tessellator tessellator = Tessellator.getInstance();

		int left = boundAbsolute.left();
		int right = boundAbsolute.rightExclusive();
		int top = boundAbsolute.top();
		int bottom = boundAbsolute.bottomExclusive();

		int amountScrolled = 0;

		BufferBuilder buffer = tessellator.getBuffer();
		minecraft().getTextureManager()
				.bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		buffer.pos((double)left, (double)bottom, 0.0D)
				.tex((double)((float)left / f), (double)((float)(bottom + (int)amountScrolled) / f))
				.color(32, 32, 32, 255)
				.endVertex();
		buffer.pos((double)right, (double)bottom, 0.0D)
				.tex((double)((float)right / f), (double)((float)(bottom + (int)amountScrolled) / f))
				.color(32, 32, 32, 255)
				.endVertex();
		buffer.pos((double)right, (double)top, 0.0D)
				.tex((double)((float)right / f), (double)((float)(top + (int)amountScrolled) / f))
				.color(32, 32, 32, 255)
				.endVertex();
		buffer.pos((double)left, (double)top, 0.0D)
				.tex((double)((float)left / f), (double)((float)(top + (int)amountScrolled) / f))
				.color(32, 32, 32, 255)
				.endVertex();
		tessellator.draw();
	}

	public void renderTexture2(DisplayBound boundAbsolute, ResourceLocation texture) {
		// int left = boundAbsolute.left();
		// int right = boundAbsolute.rightExclusive();
		// int top = boundAbsolute.top();
		// int bottom = boundAbsolute.bottomExclusive();

		GuiUtils.drawContinuousTexturedBox(0, 0, 30, 30, 100, 100, 50, 50, 10, 0);
	}

	public void renderTextureTest2(DisplayBound boundAbsolute, ResourceLocation texture) {

		int left = boundAbsolute.left();
		int top = boundAbsolute.top();

		minecraft().getTextureManager()
				.bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		int sizeX = 248;
		int sizeY = 166;
		GuiUtils.drawContinuousTexturedBox(left, top, 0, 0, boundAbsolute.sizeX(), boundAbsolute.sizeY(), sizeX, sizeY,
				10, 0);

	}

	public void renderShadow(DisplayBound boundAbsolute, DisplayColor color, DisplayPosition offset) {
		DisplayBound shadowBound = boundAbsolute.withTranslate(offset);

		Gui.drawRect(shadowBound.left(), shadowBound.top(), shadowBound.rightExclusive(), shadowBound.bottomExclusive(),
				color.toInteger());
	}

	public void renderBorder(DisplayBound bound, DisplayPadding thick, StyleBorder.BorderColor color) {
		DisplayBound cornerTopLeft = DisplayBound.of(bound.topLeft, DisplaySize.of(thick.left, thick.top));

		DisplayBound cornerTopRight = DisplayBound.of(bound.topRightExclusive()
				.withTranslateX(thick.right * -1), DisplaySize.of(thick.right, thick.top));

		DisplayBound cornerBottomLeft = DisplayBound.of(bound.bottomLeftExclusive()
				.withTranslateY(thick.bottom * -1), DisplaySize.of(thick.left, thick.bottom));

		DisplayBound cornerBottomRight = DisplayBound.of(
				bound.bottomRightExclusive.withTranslate(thick.right * -1, thick.bottom * -1),
				DisplaySize.of(thick.right, thick.bottom));

		DisplayBound edgeTop = DisplayBound.of(cornerTopLeft.top(), cornerTopLeft.rightExclusive(),
				cornerTopRight.bottomExclusive(), cornerTopRight.left());

		DisplayBound edgeLeft = DisplayBound.of(cornerTopLeft.bottomExclusive(), cornerTopLeft.left(),
				cornerBottomLeft.top(), cornerBottomLeft.rightExclusive());

		DisplayBound edgeBottom = DisplayBound.of(cornerBottomLeft.top(), cornerBottomLeft.rightExclusive(),
				cornerBottomRight.bottomExclusive(), cornerBottomRight.left());

		DisplayBound edgeRight = DisplayBound.of(cornerTopRight.bottomExclusive(), cornerTopRight.left(),
				cornerBottomRight.top(), cornerBottomRight.rightExclusive());

		renderRectangleSolid(cornerTopLeft, color.cornerTopLeft);
		renderRectangleSolid(cornerTopRight, color.cornerOther);
		renderRectangleSolid(cornerBottomLeft, color.cornerOther);
		renderRectangleSolid(cornerBottomRight, color.cornerBottomRight);

		renderRectangleSolid(edgeTop, color.edgeTopLeft);
		renderRectangleSolid(edgeLeft, color.edgeTopLeft);
		renderRectangleSolid(edgeBottom, color.edgeBottomRight);
		renderRectangleSolid(edgeRight, color.edgeBottomRight);
	}

	/**
	 * Minecraft Gui class doesn't expose certain useful methods publicly and so
	 * forces you to use inheritance rather than composition.
	 */
	public static class DisplayGui extends Gui {
		public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
			super.drawGradientRect(left, top, right, bottom, startColor, endColor);
		}
	}

}