package dev.prestige.base;

import dev.prestige.base.event.EventListener;
import dev.prestige.base.modules.ModuleInitializer;
import dev.prestige.base.settings.SettingInitializer;
import net.minecraft.client.Minecraft;

public class PrestigeBase {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static PrestigeBase INSTANCE = new PrestigeBase();
    public static SettingInitializer settingRewriteInitializer;
    public static EventListener eventListener;
    public static ModuleInitializer moduleInitializer;


    public void init() {
        settingRewriteInitializer = new SettingInitializer();
        eventListener = new EventListener();
        eventListener.init(true);
        moduleInitializer = new ModuleInitializer();
    }
}
