package choonster.testmod3.compat.waila;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.level.block.*;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

/**
 * Waila compatibility.
 *
 * @author Choonster
 */
@WailaPlugin
public class WailaCompat implements IWailaPlugin {

	@Override
	public void register(final IRegistrar registrar) {
		registrar.registerComponentProvider(new RotatableHUDHandler(ColoredRotatableBlock.FACING), TooltipPosition.BODY, ColoredRotatableBlock.class);
		registrar.registerComponentProvider(new MultiRotatableHUDHandler(ColoredMultiRotatableBlock.FACE_ROTATION), TooltipPosition.BODY, ColoredMultiRotatableBlock.class);

		registrar.registerComponentProvider(new RotatableHUDHandler(RotatableLampBlock.FACING), TooltipPosition.BODY, RotatableLampBlock.class);
		registrar.registerComponentProvider(new RotatableHUDHandler(ModChestBlock.FACING), TooltipPosition.BODY, ModChestBlock.class);

		registrar.registerComponentProvider(new RotatableHUDHandler(PlaneBlock.HORIZONTAL_ROTATION, TestMod3Lang.BLOCK_DESC_PLANE_HORIZONTAL_ROTATION.getTranslationKey()), TooltipPosition.BODY, PlaneBlock.class);
		registrar.registerComponentProvider(new VerticalRotatableHUDHandler(PlaneBlock.VERTICAL_ROTATION), TooltipPosition.BODY, PlaneBlock.class);

		registrar.registerComponentProvider(new RestrictedFluidTankEnabledFacingsHUDHandler(), TooltipPosition.BODY, RestrictedFluidTankBlock.class);
	}
}
