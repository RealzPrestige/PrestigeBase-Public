package dev.prestige.base;

import dev.prestige.base.event.EventListener;
import dev.prestige.base.hud.HudComponentInitializer;
import dev.prestige.base.initializers.ConfigInitializer;
import dev.prestige.base.initializers.FriendInitializer;
import dev.prestige.base.modules.ModuleInitializer;
import dev.prestige.base.settings.SettingInitializer;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

public class PrestigeBase {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static PrestigeBase INSTANCE = new PrestigeBase();

    public static ConfigInitializer configInitializer;
    public static EventListener eventListener;
    public static ModuleInitializer moduleInitializer;
    public static SettingInitializer settingInitializer;
    public static FriendInitializer friendInitializer;
    public static HudComponentInitializer hudComponentInitializer;

    public void init() {
        Display.setTitle("Prestige Base 1.0");
        settingInitializer = new SettingInitializer();
        eventListener = new EventListener();
        eventListener.init(true);
        moduleInitializer = new ModuleInitializer();
        friendInitializer = new FriendInitializer();
        hudComponentInitializer = new HudComponentInitializer();
        configInitializer = new ConfigInitializer();
        configInitializer.init();
    }
}
