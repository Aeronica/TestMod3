package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.*;
import choonster.testmod3.network.capability.fluidhandler.UpdateMenuFluidTankMessage;
import choonster.testmod3.network.capability.hiddenblock.UpdateMenuHiddenBlockRevealerMessage;
import choonster.testmod3.network.capability.lastusetime.UpdateMenuLastUseTimeMessage;
import choonster.testmod3.network.capability.pigspawner.UpdateMenuPigSpawnerFiniteMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class ModNetwork {
	public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(TestMod3.MODID, "network");

	public static final String NETWORK_VERSION = new ResourceLocation(TestMod3.MODID, "2").toString();

	public static SimpleChannel getNetworkChannel() {
		final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
				.clientAcceptedVersions(version -> true)
				.serverAcceptedVersions(version -> true)
				.networkProtocolVersion(() -> NETWORK_VERSION)
				.simpleChannel();

		channel.messageBuilder(SaveSurvivalCommandBlockMessage.class, 1)
				.decoder(SaveSurvivalCommandBlockMessage::decode)
				.encoder(SaveSurvivalCommandBlockMessage::encode)
				.consumer(SaveSurvivalCommandBlockMessage::handle)
				.add();

		channel.messageBuilder(FluidTankContentsMessage.class, 2)
				.decoder(FluidTankContentsMessage::decode)
				.encoder(FluidTankContentsMessage::encode)
				.consumer(FluidTankContentsMessage::handle)
				.add();

		channel.messageBuilder(SetLockCodeMessage.class, 3)
				.decoder(SetLockCodeMessage::decode)
				.encoder(SetLockCodeMessage::encode)
				.consumer(SetLockCodeMessage::handle)
				.add();

		channel.messageBuilder(UpdateChunkEnergyValueMessage.class, 4)
				.decoder(UpdateChunkEnergyValueMessage::decode)
				.encoder(UpdateChunkEnergyValueMessage::encode)
				.consumer(UpdateChunkEnergyValueMessage::handle)
				.add();

		channel.messageBuilder(UpdateMenuFluidTankMessage.class, 6)
				.decoder(UpdateMenuFluidTankMessage::decode)
				.encoder(UpdateMenuFluidTankMessage::encode)
				.consumer(UpdateMenuFluidTankMessage::handle)
				.add();

		channel.messageBuilder(UpdateMenuHiddenBlockRevealerMessage.class, 8)
				.decoder(UpdateMenuHiddenBlockRevealerMessage::decode)
				.encoder(UpdateMenuHiddenBlockRevealerMessage::encode)
				.consumer(UpdateMenuHiddenBlockRevealerMessage::handle)
				.add();

		channel.messageBuilder(UpdateMenuLastUseTimeMessage.class, 10)
				.decoder(UpdateMenuLastUseTimeMessage::decode)
				.encoder(UpdateMenuLastUseTimeMessage::encode)
				.consumer(UpdateMenuLastUseTimeMessage::handle)
				.add();

		channel.messageBuilder(UpdateMenuPigSpawnerFiniteMessage.class, 12)
				.decoder(UpdateMenuPigSpawnerFiniteMessage::decode)
				.encoder(UpdateMenuPigSpawnerFiniteMessage::encode)
				.consumer(UpdateMenuPigSpawnerFiniteMessage::handle)
				.add();

		channel.messageBuilder(OpenClientScreenMessage.class, 13)
				.decoder(OpenClientScreenMessage::decode)
				.encoder(OpenClientScreenMessage::encode)
				.consumer(OpenClientScreenMessage::handle)
				.add();

		return channel;
	}
}
