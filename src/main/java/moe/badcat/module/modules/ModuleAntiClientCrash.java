package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import moe.badcat.module.ITickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class ModuleAntiClientCrash extends BaseEnableModule implements ITickable {

    public ModuleAntiClientCrash() {
        super("其他", "客户端崩溃拦截");
    }

    @Override
    public boolean initModule() {
        onUse();
        return true;
    }

    @Override
    public void tick() {
        if (isEnable()) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player != null) {
                if (!isFinite(player.motionX)) player.motionX = 0;
                if (!isFinite(player.motionY)) player.motionY = 0;
                if (!isFinite(player.motionZ)) player.motionZ = 0;
                if (player.motionX > 100 || player.motionX < -100) player.motionX = 0;
                if (player.motionY > 100 || player.motionY < -100) player.motionY = 0;
                if (player.motionZ > 100 || player.motionZ < -100) player.motionZ = 0;
            }
        }
    }

    private static boolean isFinite(double d) {
        return Math.abs(d) <= Double.MAX_VALUE;
    }
}
