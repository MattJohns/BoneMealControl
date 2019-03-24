package mattjohns.minecraft.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * All events that get fired by BlockBase class.
 */
public class BlockBaseEvent extends Event {
	public World world;
	public BlockPos position;

	public BlockBaseEvent(World world, BlockPos position) {
		this.world = world;
		this.position = position;
	}

	/**
	 * Player clicked 'use' button on block.
	 */
	public static class OnBlockActivated extends BlockBaseEvent {
		public IBlockState state;
		public EntityPlayer player;
		public EnumHand hand;
		public EnumFacing side;
		public float hitX;
		public float hitY;
		public float hitZ;

		public OnBlockActivated(World world, BlockPos position, IBlockState state, EntityPlayer player, EnumHand hand,
				EnumFacing side, float hitX, float hitY, float hitZ) {
			super(world, position);

			this.state = state;
			this.player = player;
			this.hand = hand;
			this.side = side;
			this.hitX = hitX;
			this.hitY = hitY;
			this.hitZ = hitZ;
		}
	}

	/**
	 * Block was added to the world by a player (not called during generation i
	 * believe).
	 */
	public static class OnBlockAdded extends BlockBaseEvent {
		public OnBlockAdded(World world, BlockPos position) {
			super(world, position);
		}
	}

	/**
	 * Block destroyed by player or some other means (i.e. explosion).
	 */
	public static class OnBlockDestroyed extends BlockBaseEvent {
		public OnBlockDestroyed(World world, BlockPos position) {
			super(world, position);
		}
	}

	/**
	 * Neighbor block changed state.
	 */
	public static class NeighborChanged extends BlockBaseEvent {
		public NeighborChanged(World world, BlockPos position) {
			super(world, position);
		}
	}
}
