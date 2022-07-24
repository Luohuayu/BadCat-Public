package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import moe.badcat.module.ITickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;

public class ModuleFly extends BaseEnableModule implements ITickable {
    public ModuleFly() {
        super("原版类", "飞行");
    }

    @Override
    public void onUse() {
        super.onUse();

        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (isEnable()) {
            player.capabilities.allowFlying = true;
        } else {
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
        }
    }

    public void tick() {
        if (isEnable()) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player != null && player.capabilities != null) {
                player.capabilities.allowFlying = true;
                if (player.fallDistance > 1.0f) {
                    player.connection.sendPacket(new CPacketPlayer(true));
                    player.fallDistance = 0;
                }
            }
        }
    }
}
