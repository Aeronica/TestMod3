package choonster.testmod3.item;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.client.gui.GuiIDs;
import choonster.testmod3.util.NetworkUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * A key that can lock {@link ILock}s.
 *
 * @author Choonster
 */
public class KeyItem extends Item {
	public KeyItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType useOn(final ItemUseContext context) {
		return LockCapability.getLock(context.getLevel(), context.getClickedPos(), context.getClickedFace())
				.map(lock -> {
					if (!context.getLevel().isClientSide && context.getPlayer() != null) {
						if (lock.isLocked()) {
							context.getPlayer().sendMessage(new TranslationTextComponent("testmod3.lock.already_locked"), Util.NIL_UUID);
						} else {
							NetworkUtil.openClientGui((ServerPlayerEntity) context.getPlayer(), GuiIDs.Client.LOCK, buffer -> {
								buffer.writeBlockPos(context.getClickedPos());
								NetworkUtil.writeNullableFacing(context.getClickedFace(), buffer);
							});
						}
					}

					return ActionResultType.SUCCESS;
				})
				.orElse(ActionResultType.PASS);
	}
}
