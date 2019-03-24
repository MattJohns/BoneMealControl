package mattjohns.minecraft.bonemealcontrol.server;

import mattjohns.common.immutable.Immutable;
import mattjohns.minecraft.bonemealcontrol.common.PacketFromServerReceiver;

// dummy packet handler
public final class PacketFromServerReceiverVoid extends Immutable<PacketFromServerReceiverVoid>
		implements PacketFromServerReceiver {
	protected PacketFromServerReceiverVoid() {
	}

	public static PacketFromServerReceiverVoid of() {
		return new PacketFromServerReceiverVoid();
	}

	@Override
	protected PacketFromServerReceiverVoid concreteCopy(Immutable<?> source) {
		return new PacketFromServerReceiverVoid();
	}
}
