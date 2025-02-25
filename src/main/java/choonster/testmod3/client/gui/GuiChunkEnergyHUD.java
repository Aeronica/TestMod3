package choonster.testmod3.client.gui;

import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.config.TestMod3Config;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.language.I18n;

/**
 * Displays the chunk energy in the player's current chunk.
 *
 * @author Choonster
 */
public class GuiChunkEnergyHUD extends GuiComponent {
	private static final Minecraft minecraft = Minecraft.getInstance();

	public void drawHUD(final PoseStack matrixStack) {
		if (minecraft.level == null || minecraft.player == null) return;

		ChunkEnergyCapability.getChunkEnergy(minecraft.level.getChunkAt(minecraft.player.blockPosition()))
				.ifPresent(chunkEnergy -> {
					final String text = I18n.get(TestMod3Lang.CHUNK_ENERGY_HUD.getTranslationKey(), chunkEnergy.getEnergyStored(), chunkEnergy.getMaxEnergyStored());
					final TestMod3Config.Client.HUDPos hudPos = TestMod3Config.CLIENT.chunkEnergyHUDPos;
					drawString(matrixStack, minecraft.font, text, hudPos.x.get(), hudPos.y.get(), 0xFFFFFF);
				});
	}
}
