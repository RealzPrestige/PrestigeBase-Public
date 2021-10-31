package dev.prestige.base.clickgui.windows;

import dev.prestige.base.PrestigeBase;
import dev.prestige.base.clickgui.settingbutton.Button;
import dev.prestige.base.clickgui.settingbutton.impl.*;
import dev.prestige.base.modules.Module;
import dev.prestige.base.modules.core.ClickGui;
import dev.prestige.base.settings.Setting;
import dev.prestige.base.settings.impl.*;
import dev.prestige.base.utils.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.awt.*;
import java.util.ArrayList;

public class ModuleWindow {
    public String name;
    public int x;
    public int y;
    public int width;
    public int height;
    public Color disabledColor;
    public Color enabledColor;
    public Module module;
    ArrayList<Button> newButton = new ArrayList<>();

    public ModuleWindow(String name, int x, int y, int width, int height, Color disabledColor, Color enabledColor, Module module) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.disabledColor = disabledColor;
        this.enabledColor = enabledColor;
        this.module = module;
        getSettings();
    }

    public void getSettings() {
        ArrayList<Button> settingList = new ArrayList<>();
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting && !setting.getName().equals("Enabled"))
                settingList.add(new BooleanButton(setting));
            if (setting instanceof IntegerSetting)
                settingList.add(new IntegerButton(setting, (IntegerSetting) setting));
            if (setting instanceof FloatSetting)
                settingList.add(new FloatButton(setting, (FloatSetting) setting));
            if (setting instanceof DoubleSetting)
                settingList.add(new DoubleButton(setting, (DoubleSetting) setting));
            if (setting instanceof EnumSetting)
                settingList.add(new EnumButton(setting, (EnumSetting) setting));
            if (setting instanceof StringSetting)
                settingList.add(new StringButton(setting, (StringSetting) setting));
            if (setting instanceof ColorSetting)
                settingList.add(new ColorButton(setting, (ColorSetting) setting));
            if (setting instanceof ParentSetting)
                settingList.add(new ParentButton(setting, (ParentSetting) setting));
            if (setting instanceof KeySetting)
                settingList.add(new KeyButton(setting, (KeySetting) setting));
        }
        newButton = settingList;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(x, y, x + width, y + height, ClickGui.getInstance().backgroundColor.getColor().getRGB());
        if (module.isEnabled())
            RenderUtil.drawRect(x + 1, y, x + width - 1, y + height, enabledColor.getRGB());
        if (isInside(mouseX, mouseY))
            RenderUtil.drawRect(x + 1, y, x + width - 1, y + height, new Color(0, 0, 0, 100).getRGB());
        PrestigeBase.mc.fontRenderer.drawStringWithShadow(name, isInside(mouseX, mouseY) ? x + 2 : x + 1, y + (height / 2f) - (PrestigeBase.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
        if (module.isOpened) {
            int y = this.y;
            for (Button button : newButton) {
                if (button.isVisible() && !button.getSetting().getName().equals("Enabled")) {
                    button.setX(x + 2);
                    button.setY(y += height);
                    button.setWidth(width - 4);
                    button.setHeight(height);
                    button.drawScreen(mouseX, mouseY, partialTicks);
                    if (button instanceof ColorButton && ((ColorButton) button).getColorSetting().isOpen()) {
                        y += 112;
                        if (((ColorButton) button).getColorSetting().isSelected())
                            y += 10;
                    }
                    if (button instanceof EnumButton)
                        if (((EnumButton) button).enumSetting.droppedDown)
                            y += ((EnumButton) button).enumSetting.getModes().size() * 10;
                }
            }
            RenderUtil.drawOutlineRect(x + 2, this.y + height, x + width - 2, y + height - 1, ClickGui.getInstance().color.getColor(), 1f);
        }
    }


    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 && isInside(mouseX, mouseY)) {
            module.isOpened = !module.isOpened;
            PrestigeBase.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
        if (isInside(mouseX, mouseY) && mouseButton == 0)
            if (module.isEnabled())
                module.disableModule();
            else module.enableModule();

        newButton.forEach(newButton -> newButton.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void initGui() {
        if (module.isOpened)
            newButton.forEach(Button::initGui);
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        newButton.forEach(newButton -> newButton.onKeyTyped(typedChar, keyCode));
    }

    public boolean isInside(int mouseX, int mouseY) {
        return (mouseX > x && mouseX < x + width) && (mouseY > y && mouseY < y + height);
    }

    public int getHeight() {
        return height;
    }
}
