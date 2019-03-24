package mattjohns.minecraft.common.network.packet;

import io.netty.buffer.ByteBuf;

public abstract class PacketToClientResponse extends PacketToClient {
	protected int requestId;

	public PacketToClientResponse() {
		this.requestId = -1;
	}

	protected PacketToClientResponse(int requestId) {
		super();

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
