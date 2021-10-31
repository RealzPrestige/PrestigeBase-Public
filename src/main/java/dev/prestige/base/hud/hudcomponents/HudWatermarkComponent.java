package dev.prestige.base.hud.hudcomponents;

import dev.prestige.base.PrestigeBase;
import dev.prestige.base.hud.HudModule;
import dev.prestige.base.modules.core.ClickGui;
import dev.prestige.base.utils.RenderUtil;

import java.awt.*;

public class HudWatermarkComponent extends HudModule {
    int dragX;
    int dragY;
    boolean isDragging;

    public HudWatermarkComponent() {
        super("Watermark");
        renderX = 0;
        renderY = 0;
    }

    public void dragScreen(int mouseX, int mouseY) {
        if (!isDragging)
            return;
        renderX = dragX + mouseX;
        renderY = dragY + mouseY;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        dragScreen(mouseX, mouseY);
        if (getValue()) {
            if (isInsideDragField(mouseX, mouseY)) {
                RenderUtil.drawRect(renderX, renderY, renderX + PrestigeBase.mc.fontRenderer.getStringWidth("Mint 0.1.1"), renderY + PrestigeBase.mc.fontRenderer.FONT_HEIGHT, new Color(0, 0, 0, 100).getRGB());
                RenderUtil.drawRect(renderX + PrestigeBase.mc.fontRenderer.getStringWidth("Mint 0.1.1") + 3, renderY - 7, renderX + PrestigeBase.mc.fontRenderer.getStringWidth("Mint 0.1.1") + 3 + PrestigeBase.mc.fontRenderer.getStringWidth("renderX: " + renderX + " renderY: " + renderY), renderY - 7 + PrestigeBase.mc.fontRenderer.FONT_HEIGHT, new Color(0, 0, 0, 100).getRGB());
                PrestigeBase.mc.fontRenderer.drawStringWithShadow("renderX: " + renderX + " renderY: " + renderY, renderX + PrestigeBase.mc.fontRenderer.getStringWidth("Mint 0.1.1") + 3, renderY - 7, -1);
            }
            drawText();
        }
    }

    public void drawText() {
        PrestigeBase.mc.fontRenderer.drawStringWithShadow("Mint 0.1.1", renderX, renderY, ClickGui.getInstance().color.getColor().getRGB());
    }

    public boolean isInsideDragField(int mouseX, int mouseY) {
        return (mouseX > renderX && mouseX < renderX + PrestigeBase.mc.fontRenderer.getStringWidth("Mint 0.1.1")) && (mouseY > renderY && mouseY < renderY + PrestigeBase.mc.fontRenderer.FONT_HEIGHT);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isInsideDragField(mouseX, mouseY)) {
            dragX = renderX - mouseX;
            dragY = renderY - mouseY;
            isDragging = true;
        }
        if (mouseButton == 0 && isInside(mouseX, mouseY))
            value = !value;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0)
            isDragging = false;
    }
}
