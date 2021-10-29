package dev.prestige.base;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = PrestigeBaseMod.MOD_ID, name = PrestigeBaseMod.MOD_NAME, version = PrestigeBaseMod.VERSION)
public class PrestigeBaseMod {

    public static final String MOD_ID = "prestigebase";
    public static final String MOD_NAME = "PrestigeBase";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PrestigeBase.INSTANCE.init();
    }
}
