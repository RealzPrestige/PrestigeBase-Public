package dev.prestige.base.event;

import dev.prestige.base.PrestigeBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class EventListener {

    public void init(boolean load) {
        if (load)
            MinecraftForge.EVENT_BUS.register(this);
        else MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState())
            PrestigeBase.moduleInitializer.onKey(Keyboard.getEventKey());
    }
}
