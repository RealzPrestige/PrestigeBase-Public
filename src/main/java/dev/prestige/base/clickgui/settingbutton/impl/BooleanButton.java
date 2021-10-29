package dev.prestige.base.clickgui.settingbutton.impl;

import dev.prestige.base.PrestigeBase;
import dev.prestige.base.clickgui.settingbutton.Button;
import dev.prestige.base.modules.core.ClickGui;
import dev.prestige.base.settings.Setting;
import dev.prestige.base.utils.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.awt.*;

public class BooleanButton extends Button {
    Setting setting;

    public BooleanButton(Setting setting) {
        super(setting);
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(x - 2, y, x + width + 2, y + height, ClickGui.getInstance().backgroundColor.getColor().getRGB());
        if ((boolean) setting.getValue())
            RenderUtil.drawRect(x, y, x + width, y + height, ClickGui.getInstance().color.getColor().getRGB());

        if (isInside(mouseX, mouseY))
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
        PrestigeBase.mc.fontRenderer.drawStringWithShadow(setting.getName(), x + 2, y + (height / 2f) - (PrestigeBase.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isInside(mouseX, mouseY)) {
            if (getValue())
                setting.setValue(false);
            else setting.setValue(true);
            PrestigeBase.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }

    }

    public boolean getValue() {
        return (boolean) setting.getValue();
    }

}
