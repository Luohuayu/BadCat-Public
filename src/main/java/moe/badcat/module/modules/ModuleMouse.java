package moe.badcat.module.modules;

import moe.badcat.hook.LayerDeadmau5HeadHook;
import moe.badcat.module.BaseEnableModule;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class ModuleMouse extends BaseEnableModule {
    public ModuleMouse() {
        super("原版类", "老鼠彩蛋");
    }

    @Override
    public void onUse() {
        super.onUse();

        try {
            Field layerRenderersField = RenderLivingBase.class.getDeclaredField("field_177097_h");
            layerRenderersField.setAccessible(true);

            for (RenderPlayer renderPlayer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
                List<LayerRenderer<?>> layerRenderers = (List<LayerRenderer<?>>) layerRenderersField.get(renderPlayer);
                layerRenderers.replaceAll(layerRenderer -> {
                    if (layerRenderer instanceof LayerDeadmau5Head) {
                        return isEnable() ? new LayerDeadmau5HeadHook(renderPlayer) : new LayerDeadmau5Head(renderPlayer);
                    }
                    return layerRenderer;
                });
            }
        } catch (Exception e) {
            if (ModuleDebug.enable) e.printStackTrace();
        }
    }
}
