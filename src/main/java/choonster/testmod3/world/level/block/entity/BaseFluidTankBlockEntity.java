package choonster.testmod3.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.TileFluidHandler;

import javax.annotation.Nullable;

/**
 * A base class for this mod's fluid tank {@link BlockEntity}s.
 */
public abstract class BaseFluidTankBlockEntity extends TileFluidHandler {
	public BaseFluidTankBlockEntity(final BlockEntityType<?> blockEntityType, final BlockPos pos, final BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return save(new CompoundTag());
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(getBlockPos(), 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(final Connection net, final ClientboundBlockEntityDataPacket pkt) {
		load(pkt.getTag());
	}
}
