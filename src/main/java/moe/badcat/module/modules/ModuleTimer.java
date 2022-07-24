package moe.badcat.module.modules;

import moe.badcat.module.BaseModule;
import moe.badcat.module.ITickable;
import moe.badcat.utils.ReflectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ModuleTimer extends BaseModule implements ITickable {
    private EntityPlayer player;
    private int tickDouble = 1;

    public ModuleTimer() {
        super("原版类", "时间加速");
    }

    @Override
    public void onUse() {
        if (tickDouble++ >= 10) {
            tickDouble = 1;
        }
        setTickLength(50.0F / tickDouble);
    }

    @Override
    public void tick() {
        if (Minecraft.getMinecraft().player == null) {
            tickDouble = 1;
            setTickLength(50.0F);
        }
    }

    private void setTickLength(float tickLength) {
        ReflectionUtils.setPrivateField(net.minecraft.util.Timer.class, ReflectionUtils.getPrivateField(Minecraft.class, Minecraft.getMinecraft(), "timer", "field_71428_T"), "tickLength", "field_194149_e", tickLength);
    }

    @Override
    public String getDisplayText() {
        return String.format("%s [%s]", this.displayName, tickDouble == 1 ? "关" : String.format("%d 倍", tickDouble));
    }
}
