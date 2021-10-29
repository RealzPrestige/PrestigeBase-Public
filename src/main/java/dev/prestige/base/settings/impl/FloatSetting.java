package dev.prestige.base.settings.impl;

import dev.prestige.base.modules.Module;
import dev.prestige.base.settings.Setting;

import java.util.function.Predicate;

public class FloatSetting extends Setting<Float> {

    float minimum;
    float maximum;

    public FloatSetting(String name, float value, float minimum, float maximum, Module module) {
        super(name, value, module);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public FloatSetting(String name, float value, float minimum, float maximum, Module module, Predicate<Float> shown) {
        super(name, value, module, shown);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Float getValue() {
        return value;
    }

    public float getMaximum() {
        return maximum;
    }

    public float getMinimum() {
        return minimum;
    }

    public FloatSetting setParent(ParentSetting parentSetting){
        this.parentSetting = parentSetting;
        hasParentSetting = true;

        return this;
    }
}
