package mattjohns.minecraft.common.userinterface.control.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import mattjohns.common.general.IdGenerator;
import mattjohns.common.immutable.list.ListImmutableInteger;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.common.list.IdList;
import mattjohns.common.math.Vector2F;
import mattjohns.minecraft.common.userinterface.device.DeviceMouse;
import mattjohns.minecraft.common.userinterface.device.DeviceSet;
import mattjohns.minecraft.common.userinterface.style.DisplayColor;

public abstract class Window extends GuiScreen {
	protected DisplayBound boundAbsolute = DisplayBound.Tiny;

	protected boolean isShadow = false;
	protected DisplayColor shadowColor = DisplayColor.of();
	protected DisplayPosition shadowOffset = DisplayPosition.of();

	protected ControlFontMinecraftSystem fontSystem;

	// mutable
	protected IdGenerator controlIdGenerator = IdGenerator.of();

	protected IdList mouseoverIdList = IdList.of();

	// mutable
	protected DeviceSet deviceSet;

	public Window() {
		super();
	}

	@Override
	public void initGui() {
		super.initGui();

		deviceSet = DeviceSet.of();
		deviceSet.mouse()
				.mouseClickEventAddSink(this::mouseClickEventConsume);
		deviceSet.mouse()
				.mouseScrollEventAddSink(this::mouseScrollEventConsume);

		fontSystem = ControlFontMinecraftSystem.of(this.fontRenderer);

		containerCreate(this::idConsume, deviceSet);
	}

	protected abstract void containerCreate(IdSupply idSupply, DeviceSet deviceSet);

	protected abstract Container<?> containerControl();

	protected abstract void containerSave(Container<?> container);

	protected int idConsume() {
		return controlIdGenerator.consume();
	}

	@Override
	public void onGuiClosed() {
		buttonList = new ArrayList<>();

		super.onGuiClosed();
	}

	public static Vector2F screenGetSizeMinecraftScale() {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

		return new Vector2F((float)scaledResolution.getScaledWidth(), (float)scaledResolution.getScaledHeight());
	}

	public static DisplaySize fullScreenSize() {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

		return DisplaySize.of(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		tick();
	}

	protected void tick() {
	}

	@Override
	protected void keyTyped(char character, int keyCode) throws IOException {
		// handle esc last after all other controls have had a chance to process
		// it.
		if (keyCode == Keyboard.KEY_ESCAPE) {
			close();
			return;
		}
	}

	public void close() {
		close(true);
	}

	protected boolean close(boolean isRestorePreviousScreen) {
		this.mc.displayGuiScreen((GuiScreen)null);

		if (isRestorePreviousScreen) {
			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
				return true;
			}
		}

		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ListImmutableInteger displayIdList = containerControl().componentDisplayIdList();

		ComponentList displayComponentList = containerControl().componentFromId(displayIdList);

		for (Component<?> component : displayComponentList) {
			component.render();
		}
	}

	protected void mouseoverUpdate() {
	}

	@Override
	public void handleMouseInput() throws IOException {
		// record position
		int mousePositionX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int mousePositionY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

		DisplayPosition mousePosition = DisplayPosition.of(mousePositionX, mousePositionY);
		deviceSet.mouse()
				.positionCurrentSet(mousePosition);

		// raises mouseClicked()
		super.handleMouseInput();

		// wheel
		int wheelDelta = Mouse.getEventDWheel();
		if (wheelDelta != 0) {
			if (wheelDelta > 0) {
				// down
				wheelDelta = -1;
			}
			else {
				// up
				wheelDelta = 1;
			}

			deviceSet.mouse()
					.mouseScrollEventConsume(mousePosition, wheelDelta);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		DisplayPosition mousePosition = DisplayPosition.of(mouseX, mouseY);

		deviceSet.mouse()
				.mouseClickEventConsume(mousePosition, mouseButton, false);
	}

	// Control replicates this dragging mechanism
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);

		DisplayPosition mousePosition = DisplayPosition.of(mouseX, mouseY);

		deviceSet.mouse()
				.mouseClickEventConsume(mousePosition, state, true);
	}

	public static boolean isCurrentExist() {
		return screenCurrent().isPresent();
	}

	public static Optional<GuiScreen> screenCurrent() {
		if (Minecraft.getMinecraft().currentScreen == null) {
			return Optional.empty();
		}

		return Optional.of(Minecraft.getMinecraft().currentScreen);
	}

	public static boolean screenShow(GuiScreen screen) {
		return screenShow(screen, false);
	}

	public static boolean screenShow(GuiScreen screen, boolean force) {
		if (!force) {
			if (Window.isCurrentExist()) {
				// already a screen showing
				return false;
			}
		}

		Minecraft.getMinecraft()
				.displayGuiScreen(screen);
		return true;
	}

	protected void drawScreenBackround() {
	}

	/**
	 * Gets all controls that currently have focus. If there is a modal control
	 * active then that is returned, otherwise all controls are returned.
	 */
	protected ComponentList controlCaptureList() {
		return containerControl().componentList();
	}

	protected void modalEndAll() {
	}

	public DisplayBound boundAbsolute() {
		return boundAbsolute;
	}

	protected void boundAbsoluteSet(DisplayBound boundAbsolute) {
		this.boundAbsolute = boundAbsolute;

		// make control same as this window
		containerSave(containerControl().withBoundDesire(boundAbsolute));
	}

	protected void mouseClickEventConsume(DeviceMouse.MouseClickEvent event) {
	}

	protected void mouseScrollEventConsume(DeviceMouse.MouseScrollEvent event) {
	}
}
