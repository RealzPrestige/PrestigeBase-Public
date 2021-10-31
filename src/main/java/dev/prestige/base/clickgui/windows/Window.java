package dev.prestige.base.clickgui.windows;

import dev.prestige.base.PrestigeBase;
import dev.prestige.base.clickgui.settingbutton.impl.EnumButton;
import dev.prestige.base.modules.Module;
import dev.prestige.base.modules.core.ClickGui;
import dev.prestige.base.settings.Setting;
import dev.prestige.base.settings.impl.ColorSetting;
import dev.prestige.base.settings.impl.EnumSetting;
import dev.prestige.base.utils.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;

public class Window {

    String name;
    int x;
    int y;
    int width;
    int height;
    Module.Category category;

    boolean isDragging;
    int dragX;
    int dragY;
    boolean isOpened;

    ArrayList<ModuleWindow> modules = new ArrayList<>();

    public Window(String name, int x, int y, int width, int height, Module.Category category) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.category = category;
        isOpened = true;
    }

    public void dragScreen(int mouseX, int mouseY) {
        if (!isDragging)
            return;
        x = dragX + mouseX;
        y = dragY + mouseY;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        dragScreen(mouseX, mouseY);
        RenderUtil.drawRect(x - 1, y, x + width + 1, y + height, ClickGui.getInstance().color.getColor().getRGB());
        PrestigeBase.mc.fontRenderer.drawStringWithShadow(name, x + (width / 2f) - (PrestigeBase.mc.fontRenderer.getStringWidth(name) / 2f), y + (height / 2f) - (PrestigeBase.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
        if (isOpened) {
            modules.clear();
            int y = this.y;
            assert PrestigeBase.moduleInitializer != null;
            for (Module module : PrestigeBase.moduleInitializer.getCategoryModules(category)) {
                int openedHeight = 0;
                if (module.isOpened) {
                    assert PrestigeBase.settingInitializer != null;
                    for (Setting settingsRewrite : module.getSettings()) {
                        if (settingsRewrite.getName().equals("Enabled"))
                            continue;
                        if (settingsRewrite.isVisible())
                            openedHeight += 10;

                        if (settingsRewrite instanceof ColorSetting && settingsRewrite.isVisible()) {
                            if (((ColorSetting) settingsRewrite).isOpen()) {
                                openedHeight += 112;
                                if (((ColorSetting) settingsRewrite).isSelected())
                                    openedHeight += 10;
                            }
                        }
                        if (settingsRewrite instanceof EnumSetting)
                            if (((EnumSetting) settingsRewrite).droppedDown)
                                openedHeight += ((EnumSetting) settingsRewrite).getModes().size() * 10;
                    }
                }
                modules.add(new ModuleWindow(module.getName(), x, y += height, width, height, ClickGui.getInstance().backgroundColor.getColor(), ClickGui.getInstance().color.getColor(), module));
                y += openedHeight;
            }
            RenderUtil.drawOutlineRect(x, this.y + height, x + width, y + height, ClickGui.getInstance().color.getColor(), 1.5f);
        }
        RenderUtil.drawOutlineRect(x, this.y, x + width, this.y + height, ClickGui.getInstance().color.getColor(), 1.5f);
        if (isOpened)
            modules.forEach(modules -> modules.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isInside(mouseX, mouseY)) {
            dragX = x - mouseX;
            dragY = y - mouseY;
            isDragging = true;
        }
        if (mouseButton == 1 && isInside(mouseX, mouseY)) {
            isOpened = !isOpened;
            PrestigeBase.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
        if (isOpened)
            modules.forEach(modules -> modules.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (isOpened)
            modules.forEach(modules -> modules.onKeyTyped(typedChar, keyCode));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0)
            isDragging = false;
    }

    public void initGui() {
        if (isOpened)
            modules.forEach(ModuleWindow::initGui);
    }

    public boolean isInside(int mouseX, int mouseY) {
        return (mouseX > x && mouseX < x + width) && (mouseY > y && mouseY < y + height);
    }
}
