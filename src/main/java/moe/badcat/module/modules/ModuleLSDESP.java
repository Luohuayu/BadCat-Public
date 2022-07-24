package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ModuleLSDESP extends BaseEnableModule {
    public ModuleLSDESP() {
        super("原版类", "魔法邮票");
    }

    @Override
    public void onUse() {
        super.onUse();

        if (isEnable()) {
            Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/wobble.json"));
        } else {
            Minecraft.getMinecraft().entityRenderer.stopUseShader();
        }
    }
}
