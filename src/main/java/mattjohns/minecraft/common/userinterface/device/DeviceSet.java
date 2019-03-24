package mattjohns.minecraft.common.userinterface.device;

public class DeviceSet {
	protected DeviceKeyboard keyboard;
	protected DeviceMouse mouse;
	protected DeviceDisplay display;

	protected DeviceSet(DeviceKeyboard keyboard, DeviceMouse mouse, DeviceDisplay display) {
		this.keyboard = keyboard;
		this.mouse = mouse;
		this.display = display;
	}

	public static DeviceSet of() {
		return new DeviceSet(DeviceKeyboard.of(), DeviceMouse.of(), DeviceDisplay.of());
	}
	
	public DeviceKeyboard keyboard() {
		return keyboard;
	}
	
	public DeviceMouse mouse() {
		return mouse;
	}
	
	public DeviceDisplay display() {
		return display;
	}
}
