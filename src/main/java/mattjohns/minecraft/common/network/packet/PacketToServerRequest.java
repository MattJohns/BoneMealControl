package mattjohns.minecraft.common.network.packet;

import io.netty.buffer.ByteBuf;

public abstract class PacketToServerRequest extends PacketToServer {
	protected int requestId;

	public PacketToServerRequest() {
	}

	protected PacketToServerRequest(int requestId) {
		this.requestId = requestId;
	}

	public int requestId() {
		return requestId;
	}
	
	@Override
	public void fromBytes(ByteBuf buffer) {
		super.fromBytes(buffer);
		
		requestId = readInteger(buffer);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		super.toBytes(buffer);
		
		writeInteger(buffer, requestId);
	}
}
