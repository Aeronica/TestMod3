package choonster.testmod3.compat.waila;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * Adds a line to the Waila tooltip body displaying the current value of a {@link Direction} property.
 *
 * @author Choonster
 */
public class RotatableHUDHandler extends EnumPropertyHUDHandler<Direction> {

	public RotatableHUDHandler(final Property<Direction> property) {
		this(property, TestMod3Lang.DESC_ROTATABLE_FACING.getTranslationKey());
	}

	public RotatableHUDHandler(final Property<Direction> property, final String tooltipTranslationKey) {
		super(property, tooltipTranslationKey, TestMod3Lang.PREFIX_FACING.getTranslationKey());
	}
}
