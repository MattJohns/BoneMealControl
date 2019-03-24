package mattjohns.minecraft.common.userinterface.device;

import java.util.function.Consumer;

import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.minecraft.common.userinterface.control.system.SinkEvent;
import mattjohns.minecraft.common.userinterface.control.system.SinkEventList;

public class DeviceMouse extends Device {
	protected DisplayPosition positionCurrent = DisplayPosition.Zero;

	protected SinkEventList<MouseClickEvent> mouseClickEventSink = SinkEventList.of();
	protected SinkEventList<MouseScrollEvent> mouseScrollEventSink = SinkEventList.of();

	protected DeviceMouse() {
	}

	public static DeviceMouse of() {
		return new DeviceMouse();
	}

	public void mouseClickEventAddSink(Consumer<MouseClickEvent> sink) {
		mouseClickEventSink = mouseClickEventSink.add(sink);
	}

	public void mouseClickEventConsume(MouseClickEvent event) {
		mouseClickEventSink.consume(event);
	}

	public void mouseClickEventConsume(DisplayPosition positionAbsolute, int buttonIndex, boolean upIs) {
		mouseClickEventConsume(MouseClickEvent.of(this, positionAbsolute, buttonIndex, upIs));
	}

	public void mouseScrollEventAddSink(Consumer<MouseScrollEvent> sink) {
		mouseScrollEventSink = mouseScrollEventSink.add(sink);
	}

	public void mouseScrollEventConsume(MouseScrollEvent event) {
		mouseScrollEventSink.consume(event);
	}

	public void mouseScrollEventConsume(DisplayPosition positionAbsolute, int wheelDelta) {
		mouseScrollEventConsume(MouseScrollEvent.of(this, positionAbsolute, wheelDelta));
	}

	public DisplayPosition positionCurrent() {
		return positionCurrent;
	}

	public void positionCurrentSet(DisplayPosition item) {
		this.positionCurrent = item;
	}

	public static abstract class MouseEvent<TConcrete extends MouseEvent<TConcrete>>
			extends SinkEvent<DeviceMouse, TConcrete> {

		protected MouseEvent(DeviceMouse sender) {
			super(sender);
		}
	}

	public static final class MouseClickEvent extends MouseEvent<MouseClickEvent> {
		public final DisplayPosition positionAbsolute;
		public final int buttonIndex;
		public final boolean upIs;

		protected MouseClickEvent(DeviceMouse sender, DisplayPosition positionAbsolute, int buttonIndex, boolean upIs) {
			super(sender);
			this.positionAbsolute = positionAbsolute;
			this.buttonIndex = buttonIndex;
			this.upIs = upIs;
		}

		public static MouseClickEvent of(DeviceMouse sender, DisplayPosition positionAbsolute, int buttonIndex,
				boolean upIs) {
			return new MouseClickEvent(sender, positionAbsolute, buttonIndex, upIs);
		}

		@Override
		protected MouseClickEvent concreteThis() {
			return this;
		}

		@Override
		protected MouseClickEvent copy(DeviceMouse sender) {
			return new MouseClickEvent(sender, positionAbsolute, buttonIndex, upIs);
		}
	}

	public static final class MouseScrollEvent extends MouseEvent<MouseScrollEvent> {
		public final DisplayPosition positionAbsolute;
		public final int wheelDelta;

		protected MouseScrollEvent(DeviceMouse sender, DisplayPosition positionAbsolute, int wheelDelta) {
			super(sender);
			this.positionAbsolute = positionAbsolute;
			this.wheelDelta = wheelDelta;
		}

		public static MouseScrollEvent of(DeviceMouse sender, DisplayPosition positionAbsolute, int wheelDelta) {
			return new MouseScrollEvent(sender, positionAbsolute, wheelDelta);
		}

		@Override
		protected MouseScrollEvent concreteThis() {
			return this;
		}

		@Override
		protected MouseScrollEvent copy(DeviceMouse sender) {
			return new MouseScrollEvent(sender, positionAbsolute, wheelDelta);
		}
	}
}
