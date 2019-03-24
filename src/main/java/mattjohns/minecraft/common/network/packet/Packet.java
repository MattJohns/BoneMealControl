package mattjohns.minecraft.common.network.packet;

import io.netty.buffer.ByteBuf;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import mattjohns.common.immutable.list.ListImmutableInteger;
import mattjohns.common.immutable.list.ListImmutableString;

/**
 * Network packet for client or server.
 * 
 * Instantiate concrete packets with a static of() method that calls a protected
 * constructor. That way you can ensure the empty constructor never gets used
 * except for the when the networking system calls it internally.
 * 
 * Note that all subclasses must implement an empty public constructor,
 * otherwise the network system will fail when it tries to instantiate it.
 */
public abstract class Packet implements IMessage {
	/**
	 * When a packet is received the network system instantiates the packet with
	 * default constructor and then calls fromBytes() to decode the information
	 * into a packet.
	 * 
	 * So a packet only becomes valid once fromBytes() is called.  This flag is
	 * set to true automatically during that call.
	 */
	protected boolean isContentValid;

	/**
	 * Must always have a default constructor because packets get automatically
	 * instantiated by the network system during decoding. It doesn't care about
	 * the actual content.
	 */
	public Packet() {
		isContentValid = false;
	}
	
	public boolean isContentValid() {
		return isContentValid;
	};

	public void contentSetValid() {
		isContentValid = true;
	}

	@Override
	public String toString() {
		return "isContentValid: " + isContentValid;
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		if (!isContentValid)
			return;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		isContentValid = true;
	}

	protected boolean readBoolean(ByteBuf buffer) {
		return buffer.readBoolean();
	}

	protected void writeBoolean(ByteBuf buffer, boolean value) {
		buffer.writeBoolean(value);
	}

	protected int readInteger(ByteBuf buffer) {
		return buffer.readInt();
	}

	protected void writeInteger(ByteBuf buffer, int value) {
		buffer.writeInt(value);
	}

	protected String readString(ByteBuf buffer) {
		return ByteBufUtils.readUTF8String(buffer);
	}

	protected void writeString(ByteBuf buffer, String value) {
		ByteBufUtils.writeUTF8String(buffer, value);
	}

	protected ListImmutableInteger readIntegerList(ByteBuf buffer) {
		String text = readString(buffer);

		return ListImmutableInteger.deserialize(text);
	}

	protected void writeIntegerList(ByteBuf buffer, ListImmutableInteger value) {
		writeString(buffer, value.serialize());
	}

	protected ListImmutableString readStringList(ByteBuf buffer, String separator) {
		String text = readString(buffer);

		return ListImmutableString.deserialize(text, separator);
	}

	protected void writeStringList(ByteBuf buffer, ListImmutableString value, String separator) {
		writeString(buffer, value.serialize(separator));
	}
}
