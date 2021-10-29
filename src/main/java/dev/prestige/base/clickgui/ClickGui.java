package dev.prestige.base.clickgui;

import dev.prestige.base.PrestigeBase;
import dev.prestige.base.clickgui.windows.Window;
import dev.prestige.base.modules.Module;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGui extends GuiScreen {
    static ClickGui INSTANCE = new ClickGui();
    ArrayList<Window> windows = new ArrayList<>();

    public ClickGui() {
        setInstance();
        load();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        windows.forEach(windows -> windows.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        windows.forEach(windows -> windows.mouseClicked(mouseX, mouseY, clickedButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releasedButton) {
        windows.forEach(windows -> windows.mouseReleased(mouseX, mouseY, releasedButton));
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        windows.forEach(windows -> windows.onKeyTyped(typedChar, keyCode));
    }

    @Override
    public void initGui() {
        super.initGui();
        windows.forEach(Window::initGui);
    }

    public void load() {
        int x = -130;
        assert PrestigeBase.moduleInitializer != null;
        for (Module.Category categories : PrestigeBase.moduleInitializer.getCategories())
            windows.add(new Window(categories.toString(), x += 134, 10, 130, 10, categories));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
