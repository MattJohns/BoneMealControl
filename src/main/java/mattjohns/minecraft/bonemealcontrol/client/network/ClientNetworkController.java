package mattjohns.minecraft.bonemealcontrol.client.network;

import java.util.ArrayList;
import java.util.function.Consumer;

import mattjohns.common.general.IdGenerator;
import mattjohns.minecraft.bonemealcontrol.client.network.dialog.Dialog;
import mattjohns.minecraft.bonemealcontrol.common.PacketFromServerReceiver;
import mattjohns.minecraft.common.general.EntityWait;
import mattjohns.minecraft.common.general.EntityWaitList;
import mattjohns.minecraft.common.network.NetworkChannel;
import mattjohns.minecraft.common.network.packet.PacketToClientResponse;
import mattjohns.minecraft.common.network.packet.PacketToServerRequest;
import mattjohns.minecraft.common.system.SystemUtility;

/**
 * Handles requests and dialogs. Requests send a packet to server and wait for a
 * response (asynchronously). Dialogs are groups of requests in a custom
 * sequence.
 * 
 * Each request packet should have a corresponding requestSend*() method in this
 * class. The caller's consumer gets notified when the response comes back via
 * the receive*() method.
 * 
 * Also every dialog request should have a method here to create and start the
 * dialog. No code is needed for 'receiving' the end of the dialog, unlike
 * requests which have a receive*() method to handle the response packet.
 */
public class ClientNetworkController implements PacketFromServerReceiver {
	protected static int RequestTimeOutMillisecond = 30 * 1000;

	// number of ticks to wait before checking on request wait list
	protected static int RequestUpdateTickPeriod = SystemUtility.updateFrequencyToTickPeriod(0.5d);

	protected static int DialogTimeOutMillisecond = RequestTimeOutMillisecond;
	protected static int DialogUpdateTickPeriod = RequestUpdateTickPeriod;

	// Dialogs take care of calling consumers themselves, so only need
	// to monitor which ids are active. Can use a single list for all dialog
	// types because there's no (typed) consumer to call.
	protected EntityWaitList<Void> dialogWaitList = new EntityWaitList<>(DialogTimeOutMillisecond,
			DialogUpdateTickPeriod, this::dialogTimeout);

	// Each request / response packet pair has a shared id, for each instance of
	// the pair. This is sent inside the packets. Note this is not the packet
	// id.
	protected IdGenerator requestIdGenerator = IdGenerator.of();

	// Dialogs have a separate id that is unrelated to any request ids they use.
	protected IdGenerator dialogIdGenerator = IdGenerator.of();

	protected NetworkChannel network;

	public ClientNetworkController(NetworkChannel network) {
		this.network = network;
	}

	/**
	 * Sends a request packet to the server and waits for a response with the
	 * same request id.
	 */
	protected <TRequest extends PacketToServerRequest, TResponse extends PacketToClientResponse> void requestSend(
			TRequest requestPacket, EntityWaitList<Consumer<TResponse>> requestWaitList, Consumer<TResponse> consumer) {

		requestWaitList.add(requestPacket.requestId(), consumer);

		network.sendToServer(requestPacket);
	}

	/**
	 * Finds the waiting request and sends the packet to its consumer. Also
	 * removes the wait request from the list.
	 */
	protected <TPacket extends PacketToClientResponse> void responseReceive(TPacket responsePacket,
			EntityWaitList<Consumer<TPacket>> requestWaitList) {
		int requestId = responsePacket.requestId();

		if (!requestWaitList.isContainId(requestId)) {
			// Response without a request. Should never happen, silently fail.
			return;
		}

		EntityWait<Consumer<TPacket>> waitEntity = requestWaitList.waitGetById(requestId);

		Consumer<TPacket> packetConsumer = waitEntity.entity();

		// notify consumer
		try {
			packetConsumer.accept(responsePacket);
		} finally {
			// response needs to get removed from queue even if it
			// the consumer threw an exception
			waitEntity.removeSet();
		}
	}

	protected int requestIdConsume() {
		return requestIdGenerator.consume();
	}

	protected int dialogIdConsume() {
		return dialogIdGenerator.consume();
	}

	protected void dialogStart(Dialog dialog) {
		// dialogs take care of notifying consumers so no need to store any
		// consumers in the wait list (unlike requests)
		dialogWaitList.add(dialog.id(), null);

		dialog.start();
	}

	/**
	 * Automatically called by base dialog to signal it's ending.
	 */
	public void dialogEnd(int dialogId) {
		dialogWaitList.removeSetById(dialogId);
	}

	/**
	 * Longest amount of time a request or dialog has been waiting to end.
	 */
	public int waitLongestMillisecond(long currentTime) {
		int longest = 0;

		for (EntityWaitList<?> list : waitListAll()) {
			int listLongest = list.longestWaitTime(currentTime);
			if (listLongest > longest) {
				longest = listLongest;
			}
		}

		return longest;
	}

	public void tick() {
		// Tick is good enough, don't need a separate thread for the queues.
		// This just does housekeeping like deleting queue items that are marked
		// as deleted. Consumers aren't notified here so this doesn't need to
		// be fast (e.g. a 10 second tick period would be fine or even 10
		// minutes really).
		waitListForEach(x -> x.tick());
	}

	protected ArrayList<EntityWaitList<?>> waitListAll() {
		ArrayList<EntityWaitList<?>> result = new ArrayList<EntityWaitList<?>>();

		result.add(dialogWaitList);

		return result;
	}

	/**
	 * Both request and dialog lists.
	 */
	protected void waitListForEach(Consumer<EntityWaitList<?>> consumer) {
		for (EntityWaitList<?> list : waitListAll()) {
			consumer.accept(list);
		}
	}

	public <TEntity extends Consumer<? extends PacketToClientResponse>> void requestOnTimeout(
			EntityWait<TEntity> requestWait) {
		// just remove it
		requestWait.removeSet();
	}

	public void dialogTimeout(EntityWait<Void> dialogWait) {
		// just remove it and silently fail
		dialogWaitList.removeSetById(dialogWait.id());
	}
}