package mattjohns.minecraft.common.item;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;

@HasResult
public class ItemBaseEvent extends Event {
	public WorldServer world;

	public ItemBaseEvent(WorldServer world) {
		this.world = world;
	}

	/**
	 * Server received message from client.
	 */
	public static class OnServerItemUse extends ItemBaseEvent {
		public EntityPlayerMP player;
		public boolean isLeftHand; 
		public ItemStack stack;

		public OnServerItemUse(WorldServer world, EntityPlayerMP player, boolean isLeftHand, ItemStack stack) {
			super(world);

			this.player = player;
			this.isLeftHand = isLeftHand;
			this.stack = stack;
		}
	}
}
