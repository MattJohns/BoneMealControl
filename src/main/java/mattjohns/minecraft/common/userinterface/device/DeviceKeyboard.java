package mattjohns.minecraft.common.userinterface.device;

import net.minecraft.client.gui.GuiScreen;

public class DeviceKeyboard extends Device {
	protected DeviceKeyboard() {
	}

	public static DeviceKeyboard of() {
		return new DeviceKeyboard();
	}

	public boolean keyboardShiftIsDown() {
		return GuiScreen.isShiftKeyDown();
	}

	public boolean keyboardControlIsDown() {
		return GuiScreen.isCtrlKeyDown();
	}

	public boolean keyboardAltIsDown() {
		return GuiScreen.isAltKeyDown();
	}

}
