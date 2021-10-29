package dev.prestige.base.clickgui.settingbutton;

import dev.prestige.base.settings.Setting;

public class Button {
    public int x;
    public int y;
    public int width;
    public int height;
    boolean isTyping;
    Setting setting;

    public Button(Setting setting){
        this.setting = setting;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
    }

    public void onKeyTyped(char typedChar, int keyCode) {
    }
    public void initGui() {
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public boolean isVisible() {
        return setting.isVisible();
    }

    public boolean isTyping(){
        return isTyping;
    }

    public boolean isInside(int mouseX, int mouseY) {
        return (mouseX > x && mouseX < x + width) && (mouseY > y && mouseY < y + height);
    }

    public Setting getSetting(){
        return setting;
    }

}
