package choonster.testmod3.network;

import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.client.gui.LockScreen;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.NetworkUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.LockCode;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Sent to the server by {@link LockScreen} to set the new lock code.
 *
 * @author Choonster
 */
public class SetLockCodeMessage {
	private final BlockPos pos;
	private final Direction facing;
	private final String lockCode;

	public SetLockCodeMessage(final BlockPos pos, @Nullable final Direction facing, final String lockCode) {
		this.pos = pos;
		this.facing = facing;
		this.lockCode = lockCode;
	}

	public static SetLockCodeMessage decode(final FriendlyByteBuf buffer) {
		final BlockPos pos = BlockPos.of(buffer.readLong());
		final Direction facing = NetworkUtil.readNullableFacing(buffer);
		final String lockCode = buffer.readUtf(Short.MAX_VALUE);

		return new SetLockCodeMessage(pos, facing, lockCode);
	}

	public static void encode(final SetLockCodeMessage message, final FriendlyByteBuf buffer) {
		buffer.writeLong(message.pos.asLong());
		NetworkUtil.writeNullableFacing(message.facing, buffer);
		buffer.writeUtf(message.lockCode);
	}


	public static void handle(final SetLockCodeMessage message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final ServerPlayer player = ctx.get().getSender();
			final Level world = player.level;

			player.resetLastActionTime();

			if (world.isAreaLoaded(message.pos, 1)) {
				LockCapability.getLock(world, message.pos, message.facing).ifPresent((lock) -> {
					if (lock.isLocked()) {
						player.sendMessage(new TranslatableComponent(TestMod3Lang.LOCK_ALREADY_LOCKED.getTranslationKey()), Util.NIL_UUID);
					}

					lock.setLockCode(new LockCode(message.lockCode));
				});
			}
		});

		ctx.get().setPacketHandled(true);
	}

}
