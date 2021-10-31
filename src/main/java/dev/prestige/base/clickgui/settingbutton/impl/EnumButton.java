package dev.prestige.base.clickgui.settingbutton.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.prestige.base.PrestigeBase;
import dev.prestige.base.clickgui.settingbutton.Button;
import dev.prestige.base.modules.core.ClickGui;
import dev.prestige.base.settings.Setting;
import dev.prestige.base.settings.impl.EnumSetting;
import dev.prestige.base.utils.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.awt.*;


public class EnumButton extends Button {
    Setting setting;
    public EnumSetting enumSetting;

    public EnumButton(Setting setting, EnumSetting modeSetting) {
        super(setting);
        this.setting = setting;
        this.enumSetting = modeSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(x - 2, y, x + width + 2, y + height, ClickGui.getInstance().backgroundColor.getColor().getRGB());
        if (isInside(mouseX, mouseY))
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
        PrestigeBase.mc.fontRenderer.drawStringWithShadow(enumSetting.getName() + " " + ChatFormatting.GRAY + enumSetting.getValueAsString(), x + 2, y + (height / 2f) - (PrestigeBase.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
        int y = this.y;
        if (enumSetting.droppedDown) {
            for (String string : enumSetting.getModes()) {
                y += 10;
                RenderUtil.drawRect(x - 2, y, x + width + 2, y + height, ClickGui.getInstance().backgroundColor.getColor().getRGB());
                if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height)
                    RenderUtil.drawRect(x+ 3, y, x + width - 1, y + 10, new Color(0, 0, 0, 100).getRGB());
                PrestigeBase.mc.fontRenderer.drawStringWithShadow(enumSetting.getValue().equals(string) ? string : ChatFormatting.GRAY + string, (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) ? x + 5 : x + 4, y, -1);
            }
            RenderUtil.drawOutlineRect(x + 3, this.y + height - 1, x + width - 1, y + height - 2, ClickGui.getInstance().color.getColor(), 1f);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY) && mouseButton == 1) {
            enumSetting.droppedDown = !enumSetting.droppedDown;
        }
        int y = this.y;
        if (enumSetting.droppedDown)
            for (String string : enumSetting.getModes()) {
                y += 10;
                if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height && mouseButton == 0) {
                    enumSetting.setValue(string);
                }

            }

        PrestigeBase.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }
}
