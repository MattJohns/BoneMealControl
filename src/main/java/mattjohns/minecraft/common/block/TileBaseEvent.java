package mattjohns.minecraft.common.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Events fired by tile base.
 */
public class TileBaseEvent extends Event {
	public World world;
	public BlockPos position;

	public TileBaseEvent(World world, BlockPos position) {
		this.world = world;
		this.position = position;
	}

	/**
	 * Tile was created either by generation or the player.
	 */
	public static class LoadEvent extends TileBaseEvent {
		public LoadEvent(World world, BlockPos position) {
			super(world, position);
		}
	}

	/**
	 * Server is building a network packet to send changes to clients.
	 */
	public static class GetUpdatePacketEvent extends TileBaseEvent {
		public GetUpdatePacketEvent(World world, BlockPos position) {
			super(world, position);
		}
	}
}
