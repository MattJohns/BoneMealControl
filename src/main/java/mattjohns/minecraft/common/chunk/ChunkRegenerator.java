package mattjohns.minecraft.common.chunk;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class ChunkRegenerator {
	private boolean isActive = false;

	// chunks are often handled in threads so ensure this is thread safe
	private ChunkPositionListConcurrent generatedList = new ChunkPositionListConcurrent();

	private int serverTickCounter;
	private static final int TICK_DELAY = 10;

	private WorldServer worldServer;

	public ChunkRegenerator(WorldServer worldServer) {
		this.worldServer = worldServer;
	}

	public boolean isActiveGet() {
		return isActive;
	}

	// restarts regardless of current state
	public void restart() {
		isActive = true;

		generatedList.clear();
		serverTickCounter = 0;
	}

	public void stop() {
		isActive = false;

		// clear it again so you aren't carrying around useless memory
		generatedList.clear();
	}

	public void setGenerated(ChunkPosSortable position) {
		if (!isActive) {
			return;
		}

		generatedList.addUnique(position);
	}

	public ChunkPosSortable takeClosest(ChunkPosSortable position, int radius) {
		for (int ring = 0; ring <= radius; ring++) {
			int x1 = ring * -1;
			int x2 = ring * 1;
			int z1 = ring * -1;
			int z2 = ring * 1;

			for (int x = x1; x <= x2; x++) {
				for (int z = z1; z <= z2; z++) {
					ChunkPosSortable candidatePosition = new ChunkPosSortable(position.x + x,
							position.z + z);

					if (!generatedList.contains(candidatePosition)) {
						return candidatePosition;
					}
				}
			}
		}

		return null;
	}

	public void regenerateRadius(int chunkX, int chunkZ, int radius) {
		int x1 = chunkX - radius;
		int x2 = chunkX + radius;
		int z1 = chunkZ - radius;
		int z2 = chunkZ + radius;

		for (int x = x1; x <= x2; x++) {
			for (int z = z1; z <= z2; z++) {
				regenerate(new ChunkPos(x, z));
			}
		}
	}

	protected void regenerate(ChunkPos chunkPosition) {
		Chunk oldChunk = worldServer.getChunkFromChunkCoords(chunkPosition.x, chunkPosition.z);

		Chunk newChunk = worldServer.getChunkProvider().chunkGenerator.generateChunk(chunkPosition.x,
				chunkPosition.z);


		// just swap storage arrays
		oldChunk.setStorageArrays(newChunk.getBlockStorageArray());

		// send new chunk to all clients
		SPacketChunkData packet = new SPacketChunkData(newChunk, 65535);
		worldServer.getMinecraftServer().getPlayerList().sendPacketToAllPlayers(packet);

		// won't save to disk without this
		BlockPos basePosition = chunkPosition.getBlock(0, 0, 0);
		worldServer.markChunkDirty(new BlockPos(basePosition.getX(), 0, basePosition.getZ()), null);

		// whitelist chunk so it doesn't get regenerated again
		generatedList.addUnique(new ChunkPosSortable(chunkPosition));

		// fix player positions so they are not squashed when new chunk arrives

		/// probably should be sent before the new chunk packet above, otherwise player
		/// might suffocate for a period of time between the 2 packets arriving on slow
		/// connections
		for (EntityPlayer playerEntity : worldServer.playerEntities) {
			if (playerEntity.chunkCoordX == chunkPosition.x
					&& playerEntity.chunkCoordZ == chunkPosition.z) {
				// player is over this chunk
				
				// lift them up to top of chunk
				BlockPos playerPosition = new BlockPos(playerEntity.posX, playerEntity.posY, playerEntity.posZ);

				int newGroundY = newChunk.getHeight(playerPosition);

				// check if player is already above the new height (only possible in creative)
				int deltaY = playerPosition.getY() - newGroundY + 1;
				
				if (deltaY > 0 && playerEntity.isCreative() && !playerEntity.onGround) {
					// flying in creative and will automatically pushed above the ground in new chunk
				}
				else {
					// not creative flying, bring them up or down to meet new surface

					// preserves player's sub-block position
					playerEntity.setPositionAndUpdate(playerEntity.posX, newGroundY + 0, playerEntity.posZ);
				}
			}
		}
	}

	public void tick() {
		if (!isActive) {
			return;
		}

		serverTickCounter++;
		if (serverTickCounter < TICK_DELAY) {
			return;
		}

		serverTickCounter = 0;

		// regenerate 1 chunk for each player
		List<EntityPlayerMP> playerList = playerGetAll(worldServer);
		
		for (EntityPlayerMP player : playerList) {
			ChunkPosSortable playerChunkPosition = new ChunkPosSortable(player.chunkCoordX, player.chunkCoordZ);
			
			ChunkPosSortable closestChunk = takeClosest(playerChunkPosition, 3);////
			if (closestChunk == null) {
				// no old chunks nearby, don't need to do anything for this player
				return;
			}
	
			// automatically whitelisted
			regenerate(closestChunk);
		}
	}

	private List<EntityPlayerMP> playerGetAll(World world) {
		List<EntityPlayerMP> returnValue = world.<EntityPlayerMP> getPlayers(EntityPlayerMP.class,
				new Predicate<EntityPlayerMP>() {
					public boolean apply(@Nullable EntityPlayerMP item) {
						return true;
					}
				});
		
		return returnValue;
	}

	// returns new state
	public boolean toggle() {
		if (isActive) {
			stop();
			return false;
		}
		else {
			restart();
			return true;
		}
	}
}
