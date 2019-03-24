package mattjohns.minecraft.common.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Base for custom blocks. Allows events to come from blocks and be sent to the
 * proxy or other listeners.
 * <p>
 * The block gets registered during construction. This should happen during the
 * game pre-initialization.
 * <p>
 * Note that making a new block also requires json resources added to assets
 * (since Minecraft 1.8 or so).
 */
public abstract class BlockBase extends Block {
	// the name used by Minecraft to identify the block, does not include
	// metadata etc.
	protected String nameInternal;

	// The name of the localization entry for this block. The lang file in
	// assets needs to be updated when creating new blocks.
	private String nameDisplayUnlocalized;

	// the block when it is tossed on the ground or shown in inventory etc..
	protected ItemBlock itemBlock;

	/**
	 * Registers the block with Minecraft.
	 * 
	 * @param material
	 *            Determines how the player interacts physically with the block
	 *            when placed in the world. Use one of the constants defined by
	 *            Minecraft like Material.GROUND .
	 * 
	 * @param nameInternal
	 *            The name used by the game to refer to this block. Does not
	 *            include any metadata, mod prefix, colons or paths. Just the
	 *            block name with no spaces and all lower case.
	 */
	public BlockBase(Material material, String nameInternal) {
		super(material);

		// name
		this.nameInternal = nameInternal;
		setRegistryName(this.nameInternal);

		this.nameDisplayUnlocalized = nameInternal + "_name_unlocalized";
		setUnlocalizedName(this.nameDisplayUnlocalized);

		// hardness etc.
		attributeInitialize();

		// Any classes that extend this should override blockStateGetDefault().
		// Then this base class automatically uses that as the default block
		// state.
		IBlockState blockStateDefault = blockStateGetDefault(blockState.getBaseState());
		setDefaultState(blockStateDefault);

		//// need to use registry event during game initialization now
		
		// register
//		GameRegistry.registerTileEntity(tileEntityClass, this);

		// register item
		itemBlock = new ItemBlock(this);
		itemBlock.setRegistryName(getRegistryName());
		// GameRegistry.register(itemBlock);
	}

	public void register(IForgeRegistry<Block> registry) {
		registry.register(this);
	}

	public void registerItem(IForgeRegistry<Item> registry) {
		registry.register(itemBlock);
	}

	/**
	 * Override this in most cases to set the hardness etc. for the block. Some
	 * defaults are provided.
	 */
	protected void attributeInitialize() {
		setHardness(5F);
		setResistance(5F);
		setHarvestLevel("pickaxe", 2);
		setCreativeTab(CreativeTabs.MISC);
	}

	/**
	 * Always override this if you have your own block state properties. Return
	 * the block state you want for new blocks when they are created in a world.
	 * <p>
	 * If you have custom properties for your block then use IBlockState
	 * newState = partialState.withProperty(MyPropertyVariable,
	 * MyPropertyDefaultValue) to set your own defaults.
	 * 
	 * @param partialState
	 *            Some base state that is partially built by base classes.
	 * 
	 * @return The default block state of the block. It's suggested to include
	 *         the base state by convention, although it is up to your own
	 *         implementation to decide.
	 */
	protected IBlockState blockStateGetDefault(IBlockState partialState) {
		return partialState;
	}

	/**
	 * No longer needed after Minecraft version 1.8 . Was previously used to
	 * convert a block state into an integer state. See general wiki entries on
	 * block meta data for details.
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		// complains if not zero when meta is not used for anything
		return 0;
	}

	/**
	 * Should no be overriden, use propertyAppend() to instantiate your custom
	 * properties for this block.
	 * 
	 * @return Property container that holds all of the custom properties.
	 */
	@Override
	protected BlockStateContainer createBlockState() {
		// Base class is Minecraft's Block class which doesn't have any
		// properties by default.
		// So just start with a new property list and then subclasses will only
		ArrayList<IProperty<?>> propertyList = new ArrayList<IProperty<?>>();
		propertyAppend(propertyList);

		IProperty<?>[] propertyArray = propertyList.toArray(new IProperty<?>[propertyList.size()]);

		return new BlockStateContainer(this, propertyArray);
	}

	/**
	 * Override this to supply your custom properties. Ensure you call super so
	 * any base classes can add their properties before you add yours, even if
	 * there are no custom properties for your particular subclass.
	 * <p>
	 * You should not override createBlockState().
	 * 
	 * @param partialList
	 *            An existing list of properties built up by subclasses. Add to
	 *            this list using partialList.add(MyPropertyVariable) .
	 */
	protected void propertyAppend(ArrayList<IProperty<?>> partialList) {
	}

	/**
	 * Do not override this method, instead override
	 * onBlockActivatedInheritable() .
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// return super.onBlockActivatedInheritable(worldIn, pos, state,
		// playerIn, hand, facing, hitX, hitY, hitZ);
		return onBlockActivatedInheritable(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	/**
	 * Can't inherit normal active method because you can't call
	 * super.onBlockActivated(), the system chains it for you instead.
	 *
	 * Doesn't matter if the inheritor forgets to implement this method. It just
	 * means the event listener won't get called.
	 */
	public boolean onBlockActivatedInheritable(World world, BlockPos position, IBlockState state, EntityPlayer player,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			MinecraftForge.EVENT_BUS.post(
					new BlockBaseEvent.OnBlockActivated(world, position, state, player, hand, side, hitX, hitY, hitZ));

			/// need to get return value from event somehow
		}

		return true;
	}

	/**
	 * No need to override. Sends an event to any listeners.
	 */
	@Override
	public void onBlockAdded(World world, BlockPos position, IBlockState state) {
		super.onBlockAdded(world, position, state);

		if (!world.isRemote) {
			MinecraftForge.EVENT_BUS.post(new BlockBaseEvent.OnBlockAdded(world, position));
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(world, pos, state);

		if (!world.isRemote)
			onBlockDestroyed(world, pos);
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion explosionIn) {
		super.onBlockDestroyedByExplosion(world, pos, explosionIn);

		if (!world.isRemote)
			onBlockDestroyed(world, pos);
	}

	protected void onBlockDestroyed(World world, BlockPos position) {
		if (!world.isRemote) {
			MinecraftForge.EVENT_BUS.post(new BlockBaseEvent.OnBlockDestroyed(world, position));
		}
	}

	/**
	 * Gets the state of the placed block instance in the given world.
	 * <p>
	 * Use IBlockState newState = partialState.withProperty(MyPropertyVariable,
	 * propertyValueForParticularBlockInstance) to build a block state that
	 * represents the actual placed block's state.
	 * <p>
	 * Ensure super() is called before setting your own state, otherwise those
	 * base properties will be missing.
	 * 
	 * @param partialState
	 *            The state set by any base classes.
	 * 
	 * @param world
	 *            The world the placed block resides in.
	 * 
	 * @param position
	 *            The position of the block relative to world origin.
	 * 
	 * @return The block state of the placed block.
	 */
	@Override
	public IBlockState getActualState(IBlockState partialState, IBlockAccess world, BlockPos position) {
		return partialState;
	}

	/**
	 * Not actually deprecated. Forge authors have marked this method deprecated
	 * to mean it should only be overridden in a careful way. It is entirely
	 * proper to use it.
	 * <p>
	 * A neighboring block changed block state.
	 * <p>
	 * Be careful you don't get endless loops of neighbors updating each other
	 * when implementing logic that uses this method. You don't want to detect a
	 * neighbor changed and then try to update your block state, which in turn
	 * causes the neighbor to get a notification. It needs logic that prevents
	 * that recursion from occuring.
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

		if (!worldIn.isRemote) {
			MinecraftForge.EVENT_BUS.post(new BlockBaseEvent.NeighborChanged(worldIn, pos));
		}
	}
}
