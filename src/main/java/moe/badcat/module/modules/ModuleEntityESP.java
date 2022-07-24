package moe.badcat.module.modules;

import moe.badcat.hook.ESPRendererHook;
import moe.badcat.module.BaseEnableModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

public class ModuleEntityESP extends BaseEnableModule {
    private static ESPRendererHook rendererHook = null;
    private static EntityRenderer originRender = null;

    public ModuleEntityESP(String displayType, String displayName) {
        super(displayType, displayName);
    }

    @Override
    public void onUse() {
        super.onUse();

        if (rendererHook == null) {
            originRender = Minecraft.getMinecraft().entityRenderer;
            Minecraft.getMinecraft().entityRenderer = rendererHook = new ESPRendererHook(Minecraft.getMinecraft(), Minecraft.getMinecraft().getResourceManager());
        }
    }

    protected void setPlayerESP(boolean enable) {
        rendererHook.player = enable;
    }

    protected void setPixelmonESP(boolean enable) {
        rendererHook.pixelmon = enable;
    }

    public static void disable(boolean force) {
        if (rendererHook == null) return;
        if (force || (!rendererHook.player && !rendererHook.pixelmon)) {
            if (originRender != null) {
                Minecraft.getMinecraft().entityRenderer = originRender;
            }
            rendererHook.disable();
            rendererHook = null;
        }
    }
}