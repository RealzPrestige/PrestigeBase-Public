package dev.prestige.base.modules.core;

import dev.prestige.base.modules.Module;
import dev.prestige.base.modules.ModuleInfo;
import dev.prestige.base.settings.impl.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "Test Module", category = Module.Category.Core, description = "Module Test")
public class TestModule extends Module {

    public BooleanSetting booleanSetting = new BooleanSetting("Boolean Test", false, this);
    public ParentSetting parentSetting = new ParentSetting("Parent Test", false, this);
    public IntegerSetting integerSetting = new IntegerSetting("Integer Test", 1, 0, 10, this);
    public FloatSetting floatSetting = new FloatSetting("Float Test", 1.0f, 0.0f, 10.0f, this);
    public DoubleSetting doubleSetting = new DoubleSetting("Double Test", 1.0, 0.0, 10.0f, this);
    public StringSetting stringSetting = new StringSetting("String Test", "Boink", this);
    public KeySetting keySetting = new KeySetting("Key Test", Keyboard.KEY_NONE, this);
    public ColorSetting colorSetting = new ColorSetting("Color Test", new Color(255, 255, 255), this);
    public EnumSetting enumSetting = new EnumSetting("Enum Test", Test.One, this);

    public enum Test {One, Two, Three}
}
