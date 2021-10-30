package dev.prestige.base.settings;

import dev.prestige.base.PrestigeBase;
import dev.prestige.base.modules.Module;
import dev.prestige.base.settings.impl.ParentSetting;

import java.util.function.Predicate;

public class Setting<T> {
    public String name;
    public Module module;
    public T value;
    public Predicate<T> shown;
    public ParentSetting parentSetting;
    public boolean hasParentSetting = false;

    public Setting(String name, T value, Module module) {
        this.name = name;
        this.value = value;
        this.module = module;
        assert PrestigeBase.settingInitializer != null;
        PrestigeBase.settingInitializer.addSetting(this);
        module.settings.add(this);
    }

    public Setting(String name, T value, Module module, Predicate<T> shown) {
        this.name = name;
        this.value = value;
        this.module = module;
        this.shown = shown;
        assert PrestigeBase.settingInitializer != null;
        PrestigeBase.settingInitializer.addSetting(this);
        module.settings.add(this);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public Module getModule() {
        return module;
    }

    public String getValueAsString() {
        return value.toString();
    }

    public boolean isVisible() {
        if (hasParentSetting && !parentSetting.isOpen)
            return false;

        if (shown == null)
            return true;

        return shown.test(getValue());
    }
}
