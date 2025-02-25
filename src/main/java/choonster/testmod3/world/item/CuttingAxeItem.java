package choonster.testmod3.world.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

import java.util.Random;

/**
 * An axe that loses durability when used in crafting recipes
 *
 * @author Choonster
 */
public class CuttingAxeItem extends AxeItem {
	private final Random random = new Random();

	public CuttingAxeItem(final Tier tier, final float attackDamage, final float attackSpeed, final Properties properties) {
		super(tier, attackDamage, attackSpeed, properties);
	}

	@Override
	public boolean hasContainerItem(final ItemStack stack) {
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		stack = stack.copy();
		stack.hurt(1, random, null);
		return stack;
	}
}
