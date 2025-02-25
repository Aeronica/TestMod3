package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.client.gui.GuiIDs;
import choonster.testmod3.init.ModBlockEntities;
import choonster.testmod3.util.NetworkUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

/**
 * A Command Block that's accessible outside Creative Mode.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @author Choonster
 */
public class SurvivalCommandBlockEntity extends CommandBlockEntity {

	private final SurvivalCommandBlock survivalCommandBlock = new SurvivalCommandBlock(SurvivalCommandBlock.Type.BLOCK) {
		@Override
		public void setCommand(final String command) {
			super.setCommand(command);
			setChanged();
		}

		@Override
		public void onUpdated() {
			final BlockState state = getLevel().getBlockState(worldPosition);
			getLevel().sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
		}

		@Override
		public ServerLevel getLevel() {
			return (ServerLevel) level;
		}

		@Override
		public InteractionResult usedBy(final Player player) {
			if (!player.getCommandSenderWorld().isClientSide) {
				final ServerPlayer playerMP = (ServerPlayer) player;
				NetworkUtil.openClientGui(playerMP, GuiIDs.Client.SURVIVAL_COMMAND_BLOCK, worldPosition);
				sendToClient(playerMP);
			}

			return InteractionResult.SUCCESS;
		}

		@Override
		public Vec3 getPosition() {
			return new Vec3(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D);
		}

		@Override
		public CommandSourceStack createCommandSourceStack() {
			return new CommandSourceStack(
					this,
					new Vec3(worldPosition.getX() + 0.5d, worldPosition.getY() + 0.5d, worldPosition.getZ() + 0.5D),
					Vec2.ZERO,
					getLevel(),
					2,
					getName().getString(),
					getName(),
					getLevel().getServer(),
					null
			);
		}
	};

	public SurvivalCommandBlockEntity(final BlockPos pos, final BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModBlockEntities.SURVIVAL_COMMAND_BLOCK.get();
	}

	@Override
	public SurvivalCommandBlock getCommandBlock() {
		return survivalCommandBlock;
	}

	@Override
	public void load(final CompoundTag tag) {
		super.load(tag);

		getCommandBlock().load(tag.getCompound("SurvivalCommandBlockLogic"));
	}

	@Override
	public CompoundTag save(final CompoundTag compound) {
		super.save(compound);

		compound.put("SurvivalCommandBlockLogic", getCommandBlock().save(new CompoundTag()));

		return compound;
	}

	/**
	 * Send an update packet for this command block to the specified player.
	 *
	 * @param player The player.
	 */
	private void sendToClient(final ServerPlayer player) {
		setSendToClient(true);

		final ClientboundBlockEntityDataPacket updatePacket = getUpdatePacket();
		if (updatePacket != null) {
			player.connection.send(updatePacket);
		}
	}

	@Override
	public Mode getMode() {
		return ((choonster.testmod3.world.level.block.SurvivalCommandBlock) getBlockState().getBlock()).getCommandBlockMode();
	}
}
