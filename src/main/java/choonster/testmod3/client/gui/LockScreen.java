package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.network.SetLockCodeMessage;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;

/**
 * Allows a player to lock an {@link ILock}.
 *
 * @author Choonster
 */
public class LockScreen extends Screen {
	/**
	 * The lock.
	 */
	private final ILock lock;

	/**
	 * The position.
	 */
	private final BlockPos pos;

	/**
	 * The facing.
	 */
	private final Direction facing;

	/**
	 * The lock code text field.
	 */
	private TextFieldWidget lockCodeTextField;
	private Button doneButton;
	private Button cancelButton;

	public LockScreen(final ILock lock, final BlockPos pos, @Nullable final Direction facing) {
		super(NarratorChatListener.NO_TITLE);
		this.lock = lock;
		this.pos = pos;
		this.facing = facing;
	}

	@Override
	public void tick() {
		lockCodeTextField.tick();
	}


	@Override
	protected void init() {
		minecraft.keyboardHandler.setSendRepeatsToGui(true);

		doneButton = addButton(new Button(width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20, new TranslationTextComponent("gui.done"), button -> done()));

		cancelButton = addButton(new Button(width / 2 + 4, height / 4 + 120 + 12, 150, 20, new TranslationTextComponent("gui.cancel"), button -> removed()));

		lockCodeTextField = new TextFieldWidget(font, width / 2 - 150, 50, 300, 20, new TranslationTextComponent("gui.testmod3.lock.lock_code"));
		lockCodeTextField.setMaxLength(32500);
		lockCodeTextField.setFocus(true);
		children.add(lockCodeTextField);
	}

	private void done() {
		TestMod3.network.sendToServer(new SetLockCodeMessage(pos, facing, lockCodeTextField.getValue()));
		minecraft.setScreen(null);
	}

	@Override
	public void removed() {
		minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}

	@Override
	public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			return super.keyPressed(keyCode, scanCode, modifiers);
		} else if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
			done();
			return true;
		}

		return lockCodeTextField.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int mouseButton) {
		return lockCodeTextField.mouseClicked(mouseX, mouseY, mouseButton) || super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
		renderBackground(matrixStack);
		drawCenteredString(matrixStack, font, I18n.get(TestMod3Lang.LOCK_SET_LOCK_CODE.getTranslationKey()), width / 2, 20, 0xffffff);
		drawString(matrixStack, font, I18n.get(TestMod3Lang.LOCK_LOCK_CODE.getTranslationKey()), width / 2 - 150, 37, 0xa0a0a0);
		lockCodeTextField.render(matrixStack, mouseX, mouseY, partialTicks);
		drawString(matrixStack, font, "", width / 2 - 150, 75 * font.lineHeight, 0xa0a0a0);

		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}
