package dev.prestige.base.clickgui.settingbutton.impl;


import com.mojang.realmsclient.gui.ChatFormatting;
import dev.prestige.base.PrestigeBase;
import dev.prestige.base.clickgui.settingbutton.Button;
import dev.prestige.base.modules.core.ClickGui;
import dev.prestige.base.settings.Setting;
import dev.prestige.base.settings.impl.IntegerSetting;
import dev.prestige.base.utils.RenderUtil;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class IntegerButton extends Button {

    int minimax;
    IntegerSetting integerSetting;

    public IntegerButton(Setting setting, IntegerSetting integerSetting) {
        super(setting);
        this.integerSetting = integerSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        dragSlider(mouseX, mouseY);
        RenderUtil.drawRect(x - 2, y, x + width + 2, y + height, ClickGui.getInstance().backgroundColor.getColor().getRGB());
        RenderUtil.drawRect(x, y, ((Number) integerSetting.getValue()).floatValue() <= integerSetting.getMinimum() ? x : x + ((float) width + 2f) * ((((Number) integerSetting.getValue()).floatValue() - integerSetting.getMinimum()) / (integerSetting.getMaximum() - integerSetting.getMinimum())) - 2, y + (float) height, ClickGui.getInstance().color.getColor().getRGB());
        if (isInside(mouseX, mouseY))
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
        PrestigeBase.mc.fontRenderer.drawStringWithShadow(integerSetting.getName() + " " + ChatFormatting.GRAY + integerSetting.getValue(), x + 2, y + (height / 2f) - (PrestigeBase.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
    }

    void dragSlider(int mouseX, int mouseY) {
        if (isInsideExtended(mouseX, mouseY) && Mouse.isButtonDown(0))
            setSliderValue(mouseX);
    }

    public boolean isInsideExtended(int mouseX, int mouseY) {
        return (mouseX > x && mouseX < x + width + 5) && (mouseY > y && mouseY < y + height);
    }

    void setSliderValue(int mouseX) {
        float percent = ((float) mouseX - x - 1) / ((float) width - 5);
        integerSetting.setValue((int) (integerSetting.getMinimum() + minimax * percent));

        float diff = Math.min(width, Math.max(0, mouseX - x));
        float min = integerSetting.getMinimum();
        float max = integerSetting.getMaximum();
        if (diff == 0) {
            integerSetting.setValue(integerSetting.getMinimum());
        } else {
            float value = roundNumber(diff / width * (max - min) + min, 1);
            integerSetting.setValue((int) value);
        }
    }

    public static float roundNumber(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(places, RoundingMode.FLOOR);
        return decimal.floatValue();
    }

}
