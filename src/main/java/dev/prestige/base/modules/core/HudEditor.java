package dev.prestige.base.modules.core;

import dev.prestige.base.hud.HudWindow;
import dev.prestige.base.modules.Module;
import dev.prestige.base.modules.ModuleInfo;

@ModuleInfo(name = "Hud Editor", category = Module.Category.Core, description = "Edits the hud ye")
public class HudEditor extends Module {

    @Override
    public void onEnable() {
        mc.displayGuiScreen(HudWindow.getInstance());
        disableModule();
    }
}
