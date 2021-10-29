package dev.prestige.base.clickgui.settingbutton.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.prestige.base.PrestigeBase;
import dev.prestige.base.clickgui.settingbutton.Button;
import dev.prestige.base.modules.core.ClickGui;
import dev.prestige.base.settings.Setting;
import dev.prestige.base.settings.impl.KeySetting;
import dev.prestige.base.utils.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class KeyButton extends Button {
    KeySetting keySetting;

    public KeyButton(Setting setting, KeySetting keySetting) {
        super(setting);
        this.keySetting = keySetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(x - 2, y, x + width + 2, y + height, ClickGui.getInstance().backgroundColor.getColor().getRGB());
        if (isInside(mouseX, mouseY))
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
        PrestigeBase.mc.fontRenderer.drawStringWithShadow(keySetting.isTyping ? keySetting.getName() + " ..." : keySetting.getName() + " " + ChatFormatting.GRAY + (keySetting.getKey() == -1 ? "None" : Keyboard.getKeyName(keySetting.getKey())), x + 2, y + (height / 2f) - (PrestigeBase.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isInside(mouseX, mouseY)) {
            keySetting.isTyping = !keySetting.isTyping;
            PrestigeBase.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (!keySetting.isTyping)
            return;
        if (keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_ESCAPE)
            keySetting.setBind(0);
        else keySetting.setBind(keyCode);

        PrestigeBase.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        keySetting.isTyping = !keySetting.isTyping;
    }
}
