package dev.prestige.base.settings.impl;

import dev.prestige.base.modules.Module;
import dev.prestige.base.settings.Setting;

import java.util.function.Predicate;

public class ParentSetting extends Setting<Boolean> {
    public boolean isOpen;
    public ParentSetting(String name, Boolean value, Module module) {
        super(name, value, module);
    }

    public ParentSetting(String name, boolean value, Module module, Predicate<Boolean> shown) {
        super(name, value, module, shown);
    }

    public Boolean getValue() {
        return value;
    }

    public void toggleValue() {
        value = !value;
    }
}
