package dev.prestige.base.modules.misc;

import com.mojang.authlib.GameProfile;
import dev.prestige.base.modules.Module;
import dev.prestige.base.modules.ModuleInfo;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

@ModuleInfo(name = "Fake Player", category = Module.Category.Misc, description = "Spawns fake entity")
public class FakePlayer extends Module {
    EntityOtherPlayerMP fake_player;

    @Override
    public void onEnable() {
        if (mc.world == null || mc.player == null)
            return;

        fake_player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("12cbdfad-33b7-4c07-aeac-01766e609482"), "zPrestige_"));
        fake_player.copyLocationAndAnglesFrom(mc.player);
        fake_player.inventory = mc.player.inventory;
        fake_player.setHealth(36);
        mc.world.addEntityToWorld(-100, fake_player);
    }

    @Override
    public void onTick() {
        if (fake_player != null && fake_player.getDistanceSq(mc.player) > (100 * 100))
            mc.world.removeEntityFromWorld(-100);
    }

    @Override
    public void onDisable() {
        if (fake_player != null)
            mc.world.removeEntityFromWorld(-100);
    }
}
