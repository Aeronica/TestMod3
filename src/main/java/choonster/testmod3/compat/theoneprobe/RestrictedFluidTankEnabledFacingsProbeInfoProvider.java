package choonster.testmod3.compat.theoneprobe;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.level.block.RestrictedFluidTankBlock;
import choonster.testmod3.world.level.block.entity.RestrictedFluidTankBlockEntity;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Adds a line to the probe displaying the enabled facings of a {@link RestrictedFluidTankBlockEntity}.
 *
 * @author Choonster
 */
public class RestrictedFluidTankEnabledFacingsProbeInfoProvider<BLOCK extends RestrictedFluidTankBlock> extends BaseProbeInfoProvider<BLOCK> {
	public RestrictedFluidTankEnabledFacingsProbeInfoProvider(
			final ResourceLocation id, final Class<BLOCK> blockClass
	) {
		super(id, blockClass);
	}

	@Override
	protected void addBlockProbeInfo(
			final ProbeMode mode, final IProbeInfo probeInfo, final Player player,
			final Level world, final BlockState blockState, final IProbeHitData data
	) {
		final BlockPos pos = data.getPos();
		final BlockEntity tileEntity = world.getBlockEntity(pos);

		if (tileEntity instanceof RestrictedFluidTankBlockEntity) {
			final String enabledFacingsString = ((RestrictedFluidTankBlock) blockState.getBlock())
					.getEnabledFacingsString(world, pos);

			probeInfo.text(new TranslatableComponent(
					TestMod3Lang.BLOCK_DESC_FLUID_TANK_RESTRICTED_ENABLED_FACINGS.getTranslationKey(),
					enabledFacingsString
			));
		}
	}
}
