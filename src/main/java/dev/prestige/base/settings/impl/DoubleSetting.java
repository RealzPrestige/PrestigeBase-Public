package dev.prestige.base.settings.impl;

import dev.prestige.base.modules.Module;
import dev.prestige.base.settings.Setting;

import java.util.function.Predicate;

public class DoubleSetting extends Setting<Double> {

    double minimum;
    double maximum;

    public DoubleSetting(String name, double value, double minimum, double maximum, Module module) {
        super(name, value, module);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public DoubleSetting(String name, double value, double minimum, double maximum, Module module, Predicate<Double> shown) {
        super(name, value, module, shown);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Double getValue() {
        return value;
    }

    public double getMaximum() {
        return maximum;
    }

    public double getMinimum() {
        return minimum;
    }

    public DoubleSetting setParent(ParentSetting parentSetting){
        this.parentSetting = parentSetting;
        hasParentSetting = true;

        return this;
    }

}
