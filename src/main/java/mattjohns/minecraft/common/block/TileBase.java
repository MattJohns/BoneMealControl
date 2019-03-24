package mattjohns.minecraft.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;

/**
 * Tile entities allow you to do processing and logic for certain block types.
 * For example a tile entity will allow you to have a screen that opens when the
 * player uses a block. The player can modify the content of that particular
 * block and processing can occur.
 * <p>
 * Extend this when creating a custom tile entity and you will be able to
 * receive events which is not normally possible with vanilla classes.
 * <p>
 * Tile entities are sometimes confused with block states. Block states should
 * be used when a block needs to display in different ways. For any kind of
 * processing or player interaction you will need a tile entity, and often both.
 * <p>
 * Tile entities can also store data for each block and there is no practical
 * limit on the size of it. readFromNBT() and writeToNBT() must be overridden so
 * your custom tile entity data for this particular placed block can be saved
 * during world save. There are helper functions for writing most of the common
 * data types.
 */
public abstract class TileBase extends TileEntity {
	/**
	 * Used in network packet from server to client indicating it needs to
	 * redraw the block.
	 */
	private static final String PACKET_TAG_IS_RENDER_DIRTY = "is_render_dirty";

	/**
	 * Block requires redrawing because the server state was changed in a way
	 * that will have an effect on the appearance.
	 */
	private boolean isRenderDirty = false;

	/**
	 * Constructor must have no parameters because Minecraft will automatically
	 * instantiate this class during world load, for each block in the world.
	 */
	public TileBase() {
	}

	/**
	 * Used by the game for networking.  No need to override.
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	/**
	 * Called by the client when a tile entity has changed for a world block.  Should fill the class
	 * with the given nbt data that was received from the server.
	 * <p>
	 * No need to override this, just override readFromNBT().  It might be useful to override
	 * if you want to fire an event every time a block receives an update from the server.
	 */
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

	/**
	 * Called by the server to update the client with any changes.  Should create a network packet
	 * that contains all of the nbt data.
	 * <p>
	 * No need to override, instead override writeToNBT.  Sends an event to any listeners.
	 */
	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		writeToNBT(nbtTag);

		int metadata = getBlockMetadata();
		if (world != null) {
			if (!world.isRemote) {
				MinecraftForge.EVENT_BUS.post(new TileBaseEvent.GetUpdatePacketEvent(world, getPos()));
			}
		}

		return new SPacketUpdateTileEntity(this.pos, metadata, nbtTag);
	}

	/**
	 * Called by the client when an update package is received.  No need to override.
	 */
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		NBTTagCompound compound = packet.getNbtCompound();
		readFromNBT(compound);

		if (world != null) {
			if (world.isRemote) {
				MinecraftForge.EVENT_BUS.post(new TileBaseClientEvent.OnDataPacketEvent(world, getPos()));
			}
		}

		if (isRenderDirty) {
			isRenderDirty = false;
			clientScheduleRender();
		}
	}

	/**
	 * This should be overriden to fill the object with the given nbt data.  Ensure you
	 * call super() first so the base classes can get set.
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		isRenderDirty = compound.getBoolean(PACKET_TAG_IS_RENDER_DIRTY);
	}

	/**
	 * Override to build your custom nbt data into a bundle that is returned.
	 * Ensure super() is called first so base classes can add their own
	 * custom nbt data first.
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setBoolean(PACKET_TAG_IS_RENDER_DIRTY, isRenderDirty);

		return compound;
	}

	/**
	 * Without this the tile entity information gets lost in certain situations.  No need to override. 
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldIdOnly, IBlockState newIdOnly) {
		// only recreate tile if id changes
		if (oldIdOnly.getBlock() != newIdOnly.getBlock())
			return true;

		return false;
	}

	/**
	 * Tile entity data has changed and client needs updating.
	 * 
	 * @param isRenderDirty
	 * The block will need to be redrawn.
	 */
	public void clientScheduleUpdateFromServer(boolean isRenderDirty) {
		if (world == null)
			return;

		if (!world.isRemote) {
			markDirty();

			this.isRenderDirty = isRenderDirty;

			BlockPos position = getPos();
			world.notifyBlockUpdate(position, world.getBlockState(position), world.getBlockState(position), 3);
		}
	}

	/**
	 * Called on client to schedule a redraw for the block.
	 */
	protected void clientScheduleRender() {
		if (world.isRemote)
			world.markBlockRangeForRenderUpdate(pos, pos);
	}

	/**
	 * Get the dimension id of the block's world.
	 */
	public int dimensionGetId() {
		if (world == null)
			return -1;
		
        if (world == null)
            return -1;
        if (world.provider == null)
            return -1;
        
        return world.provider.getDimension();
	}

	@Override
	public void onLoad() {
		super.onLoad();

		if (world != null) {
			if (!world.isRemote) {
				MinecraftForge.EVENT_BUS.post(new TileBaseEvent.LoadEvent(world, getPos()));
			}
		}
	}
}
