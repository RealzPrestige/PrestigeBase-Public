package dev.prestige.base.clickgui.settingbutton.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.prestige.base.PrestigeBase;
import dev.prestige.base.clickgui.settingbutton.Button;
import dev.prestige.base.modules.core.ClickGui;
import dev.prestige.base.settings.Setting;
import dev.prestige.base.settings.impl.ColorSetting;
import dev.prestige.base.utils.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public class ColorButton extends Button {
    Setting setting;
    ColorSetting colorSetting;
    private Color finalColor;
    boolean pickingColor = false;
    boolean pickingHue = false;
    boolean pickingAlpha = false;
    public static Tessellator tessellator;
    public static BufferBuilder builder;

    static {
        tessellator = Tessellator.getInstance();
        builder = tessellator.getBuffer();
    }

    public ColorButton(Setting setting, ColorSetting colorSetting) {
        super(setting);
        this.setting = setting;
        this.colorSetting = colorSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(x + width - 12, y + 1, x + width - 3, y + height - 1, colorSetting.getColor().getRGB());
        RenderUtil.drawOutlineRect(x + width - 12, y + 1, x + width - 3, y + height - 1, new Color(0, 0, 0), 0.1f);
        if (colorSetting.isOpen()) {
            setHeight(height + 112);
            if (colorSetting.isSelected())
                setHeight(height + 10);
        }
        RenderUtil.drawRect(x - 2, y, x + width + 2, y + height, ClickGui.getInstance().backgroundColor.getColor().getRGB());
        if (isInsideButtonOnly(mouseX, mouseY))
            RenderUtil.drawRect(x, y, x + width, y + 10, new Color(0, 0, 0, 100).getRGB());
        PrestigeBase.mc.fontRenderer.drawStringWithShadow(colorSetting.getName(), x + 2, y, -1);
        String hex = String.format("#%06x", colorSetting.getColor().getRGB() & 0xFFFFFF);
        if (colorSetting.isOpen()) {
            drawPicker(colorSetting, x + 1, y + 12, x + 111, y + 12, x + 1, y + 94, mouseX, mouseY);
            RenderUtil.drawRect(x + 1, y + 107, x + 109, y + (colorSetting.isSelected() ? 130 : 120), ClickGui.getInstance().backgroundColor.getColor().getRGB());
            PrestigeBase.mc.fontRenderer.drawStringWithShadow(colorSetting.isSelected() ? ChatFormatting.UNDERLINE + hex : hex, x + 109 / 2f - (PrestigeBase.mc.fontRenderer.getStringWidth(hex) / 2f), y + 109 + (11 / 2f) - (PrestigeBase.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
            if (colorSetting.isSelected()) {
                PrestigeBase.mc.fontRenderer.drawStringWithShadow(isInsideCopy(mouseX, mouseY) ? ChatFormatting.UNDERLINE + "Copy" : "Copy", (x + ((107) / 8f) * 2) - (PrestigeBase.mc.fontRenderer.getStringWidth("Copy") / 2f), y + 120, -1);
                PrestigeBase.mc.fontRenderer.drawStringWithShadow(isInsidePaste(mouseX, mouseY) ? ChatFormatting.UNDERLINE + "Paste" : "Paste", (x + ((107) / 8f) * 6) - (PrestigeBase.mc.fontRenderer.getStringWidth("Paste") / 2f), y + 120, -1);
            }
            colorSetting.setColor(finalColor);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (colorSetting.isSelected() && isInsideCopy(mouseX, mouseY) && mouseButton == 0) {
            String hex = String.format("#%06x", colorSetting.getColor().getRGB() & 0xFFFFFF);
            StringSelection selection = new StringSelection(hex);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            PrestigeBase.mc.player.sendMessage(new TextComponentString("Color has been successfully copied to clipboard!"));
        }
        if (colorSetting.isSelected() && isInsidePaste(mouseX, mouseY) && mouseButton == 0) {
            if (readClipboard() != null) {
                if (Objects.requireNonNull(readClipboard()).startsWith("#")) {
                    colorSetting.setColor(Color.decode(Objects.requireNonNull(readClipboard())));
                } else {
                    PrestigeBase.mc.player.sendMessage(new TextComponentString("The color your pasting is not a hex-type color."));
                }
            }
        }
        if (isInsideHex(mouseX, mouseY) && mouseButton == 1)
            colorSetting.setSelected(!colorSetting.isSelected());
        if (isInsideButtonOnly(mouseX, mouseY) && mouseButton == 1)
            colorSetting.setOpen(!colorSetting.isOpen());
    }

    public boolean isInsideCopy(int mouseX, int mouseY) {
        return (mouseX > x + 1 && mouseX < x + (107 / 2f)) && (mouseY > y + 120 && mouseY < y + 130);
    }

    public boolean isInsidePaste(int mouseX, int mouseY) {
        return (mouseX > x + (107 / 2f) && mouseX < x + 109) && (mouseY > y + 120 && mouseY < y + 130);
    }

    public boolean isInsideHex(int mouseX, int mouseY) {
        return (mouseX > x + 1 && mouseX < x + 109) && (mouseY > y + 107 && mouseY < y + 120);
    }

    public boolean isInsideButtonOnly(int mouseX, int mouseY) {
        return (mouseX > x && mouseX < x + width) && (mouseY > y && mouseY < y + 10);
    }

    public ColorSetting getColorSetting(){
        return colorSetting;
    }

    public void drawPicker(ColorSetting setting, int pickerX, int pickerY, int hueSliderX, int hueSliderY,
                           int alphaSliderX, int alphaSliderY, int mouseX, int mouseY) {
        float[] color = new float[]{
                0, 0, 0, 0
        };

        try {
            color = new float[]{
                    Color.RGBtoHSB(setting.getColor().getRed(), setting.getColor().getGreen(), setting.getColor().getBlue(), null)[0], Color.RGBtoHSB(setting.getColor().getRed(), setting.getColor().getGreen(), setting.getColor().getBlue(), null)[1], Color.RGBtoHSB(setting.getColor().getRed(), setting.getColor().getGreen(), setting.getColor().getBlue(), null)[2], setting.getColor().getAlpha() / 255f
            };
        } catch (Exception ignored) {

        }

        int pickerWidth = 108;
        int pickerHeight = 80;

        int hueSliderWidth = 14;
        int hueSliderHeight = 105;

        int alphaSliderWidth = 108;
        int alphaSliderHeight = 12;

        if (!pickingColor && !pickingHue && !pickingAlpha) {
            if (Mouse.isButtonDown(0) && mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY)) {
                pickingColor = true;
            } else if (Mouse.isButtonDown(0) && mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight, mouseX, mouseY)) {
                pickingHue = true;
            } else if (Mouse.isButtonDown(0) && mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + alphaSliderWidth, alphaSliderY + alphaSliderHeight, mouseX, mouseY))
                pickingAlpha = true;
        }

        if (pickingHue) {
            float restrictedY = (float) Math.min(Math.max(hueSliderY, mouseY), hueSliderY + hueSliderHeight);
            color[0] = (restrictedY - (float) hueSliderY) / hueSliderHeight;
            color[0] = (float) Math.min(0.97, color[0]);
        }

        if (pickingAlpha) {
            float restrictedX = (float) Math.min(Math.max(alphaSliderX, mouseX), alphaSliderX + pickerWidth);
            color[3] = 1 - (restrictedX - (float) alphaSliderX) / pickerWidth;
        }

        if (pickingColor) {
            float restrictedX = (float) Math.min(Math.max(pickerX, mouseX), pickerX + pickerWidth);
            float restrictedY = (float) Math.min(Math.max(pickerY, mouseY), pickerY + pickerHeight);
            color[1] = (restrictedX - (float) pickerX) / pickerWidth;
            color[2] = 1 - (restrictedY - (float) pickerY) / pickerHeight;
            color[2] = (float) Math.max(0.04000002, color[2]);
            color[1] = (float) Math.max(0.022222223, color[1]);
        }

        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);

        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;

        drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, 255);

        drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);

        int cursorX = (int) (pickerX + color[1] * pickerWidth);
        int cursorY = (int) ((pickerY + pickerHeight) - color[2] * pickerHeight);

        Gui.drawRect(cursorX - 2, cursorY - 2, cursorX + 2, cursorY + 2, -1);

        finalColor = getColor(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), color[3]);

        drawAlphaSlider(alphaSliderX, alphaSliderY, pickerWidth, alphaSliderHeight, finalColor.getRed() / 255f, finalColor.getGreen() / 255f, finalColor.getBlue() / 255f, color[3]);
    }

    public static boolean mouseOver(int minX, int minY, int maxX, int maxY, int mX, int mY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }

    public static Color getColor(Color color, float alpha) {
        final float red = (float) color.getRed() / 255;
        final float green = (float) color.getGreen() / 255;
        final float blue = (float) color.getBlue() / 255;
        return new Color(red, green, blue, alpha);
    }

    public static void drawPickerBase(int pickerX, int pickerY, int pickerWidth, int pickerHeight, float red, float green, float blue, float alpha) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_POLYGON);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glVertex2f(pickerX, pickerY);
        glVertex2f(pickerX, pickerY + pickerHeight);
        glColor4f(red, green, blue, alpha);
        glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        glVertex2f(pickerX + pickerWidth, pickerY);
        glEnd();
        glDisable(GL_ALPHA_TEST);
        glBegin(GL_POLYGON);
        glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        glVertex2f(pickerX, pickerY);
        glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        glVertex2f(pickerX, pickerY + pickerHeight);
        glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        glVertex2f(pickerX + pickerWidth, pickerY);
        glEnd();
        glEnable(GL_ALPHA_TEST);
        glShadeModel(GL_FLAT);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step = 0;
        if (height > width) {
            RenderUtil.drawRect(x, y, x + width, y + 4, 0xFFFF0000);
            y += 4;

            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                drawGradientRect(x, y + step * (height / 6f), x + width, y + (step + 1) * (height / 6f), previousStep, nextStep, false);
                step++;
            }
            int sliderMinY = (int) (y + height * hue) - 4;
            RenderUtil.drawRect(x, sliderMinY - 1, x + width, sliderMinY + 1, -1);
        } else {
            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                gradient(x + step * (width / 6), y, x + (step + 1) * (width / 6), y + height, previousStep, nextStep, true);
                step++;
            }

            int sliderMinX = (int) (x + (width * hue));
            RenderUtil.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
        }
    }

    public void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue,
                                float alpha) {
        boolean left = true;
        int checkerBoardSquareSize = height / 2;

        for (int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                RenderUtil.drawRect(x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, 0xFFFFFFFF);
                RenderUtil.drawRect(x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, 0xFF909090);

                if (squareIndex < width - checkerBoardSquareSize) {
                    int minX = x + squareIndex + checkerBoardSquareSize;
                    int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
                    RenderUtil.drawRect(minX, y, maxX, y + height, 0xFF909090);
                    RenderUtil.drawRect(minX, y + checkerBoardSquareSize, maxX, y + height, 0xFFFFFFFF);
                }
            }

            left = !left;
        }

        drawLeftGradientRect(x, y, x + width, y + height, new Color(red, green, blue, 1).getRGB(), 0);
        int sliderMinX = (int) (x + width - (width * alpha));
        RenderUtil.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
    }


    public static String readClipboard() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (IOException | UnsupportedFlavorException exception) {
            return null;
        }
    }

    public static void drawGradientRect(final double leftpos, final double top, final double right, final double bottom, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(leftpos, top);
        GL11.glVertex2d(leftpos, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL_SMOOTH);
        builder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(right, top, 0).color((float) (endColor >> 24 & 255) / 255.0F, (float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(left, top, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(left, bottom, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(right, bottom, 0).color((float) (endColor >> 24 & 255) / 255.0F, (float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void gradient(int minX, int minY, int maxX, int maxY, int startColor, int endColor, boolean left) {
        if (left) {
            final float startA = (startColor >> 24 & 0xFF) / 255.0f;
            final float startR = (startColor >> 16 & 0xFF) / 255.0f;
            final float startG = (startColor >> 8 & 0xFF) / 255.0f;
            final float startB = (startColor & 0xFF) / 255.0f;

            final float endA = (endColor >> 24 & 0xFF) / 255.0f;
            final float endR = (endColor >> 16 & 0xFF) / 255.0f;
            final float endG = (endColor >> 8 & 0xFF) / 255.0f;
            final float endB = (endColor & 0xFF) / 255.0f;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glBegin(GL11.GL_POLYGON);
            {
                GL11.glColor4f(startR, startG, startB, startA);
                GL11.glVertex2f(minX, minY);
                GL11.glVertex2f(minX, maxY);
                GL11.glColor4f(endR, endG, endB, endA);
                GL11.glVertex2f(maxX, maxY);
                GL11.glVertex2f(maxX, minY);
            }
            GL11.glEnd();
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        } else drawGradientRect(minX, minY, maxX, maxY, startColor, endColor);
    }

    public static int gradientColor(int color, int percentage) {
        int r = (((color & 0xFF0000) >> 16) * (100 + percentage) / 100);
        int g = (((color & 0xFF00) >> 8) * (100 + percentage) / 100);
        int b = ((color & 0xFF) * (100 + percentage) / 100);
        return new Color(r,g,b).hashCode();
    }

    public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor, boolean hovered) {
        if (hovered) {
            startColor = gradientColor(startColor, -20);
            endColor = gradientColor(endColor, -20);
        }
        float c = (float) (startColor >> 24 & 255) / 255.0F;
        float c1 = (float) (startColor >> 16 & 255) / 255.0F;
        float c2 = (float) (startColor >> 8 & 255) / 255.0F;
        float c3 = (float) (startColor & 255) / 255.0F;
        float c4 = (float) (endColor >> 24 & 255) / 255.0F;
        float c5 = (float) (endColor >> 16 & 255) / 255.0F;
        float c6 = (float) (endColor >> 8 & 255) / 255.0F;
        float c7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(left, top, 0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(left, bottom, 0).color(c5, c6, c7, c4).endVertex();
        bufferbuilder.pos(right, bottom, 0).color(c5, c6, c7, c4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}