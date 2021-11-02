package dev.prestige.base.utils;

import dev.prestige.base.PrestigeBase;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BlockUtil {

    public static void placeBlock(BlockPos blockPos, boolean packet) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            if (!(PrestigeBase.mc.world.getBlockState(blockPos.offset(enumFacing)).equals(Blocks.AIR))) {
                for (Entity entity : PrestigeBase.mc.world.loadedEntityList)
                    if (new AxisAlignedBB(blockPos).intersects(entity.getEntityBoundingBox()))
                        return;

                PrestigeBase.mc.player.connection.sendPacket(new CPacketEntityAction(PrestigeBase.mc.player, CPacketEntityAction.Action.START_SNEAKING));

                if (packet)
                    PrestigeBase.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos.offset(enumFacing), enumFacing.getOpposite(), EnumHand.MAIN_HAND, 0, 0, 0));
                else PrestigeBase.mc.playerController.processRightClickBlock(PrestigeBase.mc.player, PrestigeBase.mc.world, blockPos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(blockPos), EnumHand.MAIN_HAND);

                PrestigeBase.mc.player.connection.sendPacket(new CPacketEntityAction(PrestigeBase.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                return;
            }
        }
    }
}
