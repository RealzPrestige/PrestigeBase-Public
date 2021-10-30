package dev.prestige.base.modules.movement;

import dev.prestige.base.modules.Module;
import dev.prestige.base.modules.ModuleInfo;

@ModuleInfo(name = "Sprint", category = Module.Category.Movement, description = "Auto Sprint")
public class Sprint extends Module {

    @Override
    public void onTick() {
        if (mc.world == null || mc.player == null)
            return;
        if (mc.gameSettings.keyBindForward.isPressed() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown())
            mc.player.setSprinting(true);
    }
}
