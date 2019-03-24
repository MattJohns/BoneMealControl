package mattjohns.minecraft.common.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Events fired by TileBase client code.
 */
public class TileBaseClientEvent extends TileBaseEvent {
	public TileBaseClientEvent(World world, BlockPos position) {
		super(world, position);
	}

	/**
	 * Network packet was received from server for the tile.
	 */
	public static class OnDataPacketEvent extends TileBaseClientEvent {
		public OnDataPacketEvent(World world, BlockPos position) {
			super(world, position);
		}
	}
}
