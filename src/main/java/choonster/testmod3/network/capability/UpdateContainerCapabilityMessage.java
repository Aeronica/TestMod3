package choonster.testmod3.network.capability;

import choonster.testmod3.client.util.ClientUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Base class for messages that update capability data for a single slot of a {@link Container}.
 *
 * @param <HANDLER> The capability handler type
 * @param <DATA>    The data type written to and read from the buffer
 * @author Choonster
 */
public abstract class UpdateContainerCapabilityMessage<HANDLER, DATA> {
	/**
	 * The {@link Capability} instance to update.
	 */
	final Capability<HANDLER> capability;

	/**
	 * The {@link Direction} to get the capability handler from.
	 */
	@Nullable
	final Direction facing;

	/**
	 * The ID of the {@link Container}.
	 */
	final int containerID;

	/**
	 * The slot's index in the {@link Container}.
	 */
	final int slotNumber;

	/**
	 * The capability data instance.
	 */
	final DATA capabilityData;

	public UpdateContainerCapabilityMessage(
			final Capability<HANDLER> capability,
			@Nullable final Direction facing,
			final int containerID,
			final int slotNumber,
			final HANDLER handler,
			final CapabilityContainerUpdateMessageUtils.CapabilityDataConverter<HANDLER, DATA> capabilityDataConverter
	) {
		this.capability = capability;
		this.facing = facing;
		this.containerID = containerID;
		this.slotNumber = slotNumber;
		capabilityData = capabilityDataConverter.convert(handler);
	}

	protected UpdateContainerCapabilityMessage(
			final Capability<HANDLER> capability,
			@Nullable final Direction facing,
			final int containerID,
			final int slotNumber,
			@Nullable final DATA capabilityData
	) {
		this.capability = capability;
		this.facing = facing;
		this.containerID = containerID;
		this.slotNumber = slotNumber;
		this.capabilityData = capabilityData;
	}

	/**
	 * Is there any capability data to sync?
	 *
	 * @return Is there any capability data to sync?
	 */
	public final boolean hasData() {
		return capabilityData != null;
	}

	/**
	 * Decodes an update message from the network.
	 *
	 * @param buffer                The packet buffer
	 * @param capabilityDataDecoder A function that decodes a data instance from the buffer
	 * @param messageFactory        A function to create the message instance
	 * @param <HANDLER>             The capability handler type
	 * @param <DATA>                The data type written to and read from the buffer
	 * @param <MESSAGE>             The message type
	 * @return The decoded message
	 */
	protected static <
			HANDLER,
			DATA,
			MESSAGE extends UpdateContainerCapabilityMessage<HANDLER, DATA>
			>
	MESSAGE decode(
			final PacketBuffer buffer,
			final CapabilityContainerUpdateMessageUtils.CapabilityDataDecoder<DATA> capabilityDataDecoder,
			final MessageFactory<HANDLER, DATA, MESSAGE> messageFactory
	) {
		final boolean hasFacing = buffer.readBoolean();
		final Direction facing;
		if (hasFacing) {
			facing = buffer.readEnum(Direction.class);
		} else {
			facing = null;
		}

		final int windowID = buffer.readInt();
		final int slotNumber = buffer.readInt();

		final boolean hasData = buffer.readBoolean();
		final DATA capabilityData;
		if (hasData) {
			capabilityData = capabilityDataDecoder.decode(buffer);
		} else {
			capabilityData = null;
		}

		return messageFactory.createMessage(facing, windowID, slotNumber, capabilityData);
	}

	/**
	 * Encodes an update message to be sent over the network.
	 *
	 * @param message               The message to encode
	 * @param buffer                The packet buffer
	 * @param capabilityDataEncoder A function that encodes a data instance to the buffer
	 * @param <HANDLER>             The capability handler type
	 * @param <DATA>                The data type written to and read from the buffer
	 * @param <MESSAGE>             The message type
	 */
	protected static <
			HANDLER,
			DATA,
			MESSAGE extends UpdateContainerCapabilityMessage<HANDLER, DATA>
			>
	void encode(
			final MESSAGE message,
			final PacketBuffer buffer,
			final CapabilityContainerUpdateMessageUtils.CapabilityDataEncoder<DATA> capabilityDataEncoder
	) {
		final boolean hasFacing = message.facing != null;
		buffer.writeBoolean(hasFacing);

		if (hasFacing) {
			buffer.writeEnum(message.facing);
		}

		buffer.writeInt(message.containerID);
		buffer.writeInt(message.slotNumber);

		final boolean hasData = message.hasData();
		buffer.writeBoolean(hasData);

		if (hasData) {
			capabilityDataEncoder.encode(message.capabilityData, buffer);
		}
	}

	/**
	 * Handles an update message.
	 *
	 * @param message               The message to handle
	 * @param ctx                   The network context
	 * @param capabilityDataApplier A function that applies the capability data from a data instance to a capability handler instance
	 * @param <HANDLER>             The capability handler type
	 * @param <DATA>                The data type written to and read from the buffer
	 * @param <MESSAGE>             The message type
	 */
	protected static <
			HANDLER,
			DATA,
			MESSAGE extends UpdateContainerCapabilityMessage<HANDLER, DATA>
			>
	void handle(
			final MESSAGE message,
			final Supplier<NetworkEvent.Context> ctx,
			final CapabilityContainerUpdateMessageUtils.CapabilityDataApplier<HANDLER, DATA> capabilityDataApplier
	) {
		if (!message.hasData()) { // Don't do anything if no data was sent
			ctx.get().setPacketHandled(true);
			return;
		}

		ctx.get().enqueueWork(() -> {
			final PlayerEntity player = ClientUtil.getClientPlayer();

			if (player == null) {
				return;
			}

			final Container container;
			if (message.containerID == 0) {
				container = player.inventoryMenu;
			} else if (message.containerID == player.containerMenu.containerId) {
				container = player.containerMenu;
			} else {
				return;
			}

			CapabilityContainerUpdateMessageUtils.applyCapabilityDataToContainerSlot(
					container,
					message.slotNumber,
					message.capability,
					message.facing,
					message.capabilityData,
					capabilityDataApplier
			);
		});

		ctx.get().setPacketHandled(true);
	}

	/**
	 * A function that creates update message instances from network data.
	 *
	 * @param <HANDLER> The capability handler type
	 * @param <DATA>    The data type written to and read from the buffer
	 * @param <MESSAGE> The message type
	 */
	@FunctionalInterface
	public interface MessageFactory<HANDLER, DATA, MESSAGE extends UpdateContainerCapabilityMessage<HANDLER, DATA>> {
		MESSAGE createMessage(
				@Nullable Direction facing,
				int windowID,
				int slotNumber,
				@Nullable DATA capabilityData
		);
	}
}
