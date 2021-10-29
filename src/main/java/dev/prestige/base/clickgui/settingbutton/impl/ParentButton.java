package dev.prestige.base.clickgui.settingbutton.impl;

import dev.prestige.base.PrestigeBase;
import dev.prestige.base.clickgui.settingbutton.Button;
import dev.prestige.base.modules.core.ClickGui;
import dev.prestige.base.settings.Setting;
import dev.prestige.base.settings.impl.ParentSetting;
import dev.prestige.base.utils.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.awt.*;

public class ParentButton extends Button {
    Setting setting;
    ParentSetting parentSetting;

    public ParentButton(Setting setting, ParentSetting parentSetting) {
        super(setting);
        this.setting = setting;
        this.parentSetting = parentSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(x - 2, y, x + width + 2, y + height, ClickGui.getInstance().backgroundColor.getColor().getRGB());
        RenderUtil.drawRect(x, y, x + width, y + height, ClickGui.getInstance().color.getColor().getRGB());
        if (isInside(mouseX, mouseY))
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
        PrestigeBase.mc.fontRenderer.drawStringWithShadow(parentSetting.isOpen ? "-" : "+", x + width - PrestigeBase.mc.fontRenderer.getStringWidth(parentSetting.isOpen ? "-" : "+") - 2, y + (height / 2f) - (PrestigeBase.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
        PrestigeBase.mc.fontRenderer.drawStringWithShadow(parentSetting.getName(), x + 2, y + (height / 2f) - (PrestigeBase.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 && isInside(mouseX, mouseY)) {
            parentSetting.isOpen = !parentSetting.isOpen;
            PrestigeBase.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }
}
