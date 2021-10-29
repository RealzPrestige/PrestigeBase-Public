package dev.prestige.base.settings.impl;

import dev.prestige.base.modules.Module;
import dev.prestige.base.settings.Setting;

import java.awt.*;
import java.util.function.Predicate;

public class ColorSetting extends Setting<Color> {

    boolean isOpen = false;
    boolean isSelected = false;

    public ColorSetting(String name, Color value, Module module) {
        super(name, value, module);
    }

    public ColorSetting(String name, Color value, Module module, Predicate<Color> shown) {
        super(name, value, module, shown);
    }

    public Color getColor() {
        return value;
    }

    public void setColor(Color value) {
        this.value = value;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getColorAsString() {
        return String.valueOf(value.getRGB());
    }

    public ColorSetting setParent(ParentSetting parentSetting){
        this.parentSetting = parentSetting;
        hasParentSetting = true;

        return this;
    }
}
