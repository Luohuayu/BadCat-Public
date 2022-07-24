package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import moe.badcat.utils.ReflectionUtils;
import moe.badcat.hook.XRayRenderHook;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;

public class ModuleXRay extends BaseEnableModule {
    private BlockModelRenderer originRender = null;
    private Map<Block, Integer> originBlockLight = new LinkedHashMap<>();

    public ModuleXRay() {
        super("原版类", "X-Ray");
    }

    @Override
    public void onUse() {
        super.onUse();

        BlockRendererDispatcher blockRendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        if (blockRendererDispatcher.getClass().getName().equals("codechicken.lib.render.block.CCBlockRendererDispatcher")) {
            try {
                Field parentDispatcher = blockRendererDispatcher.getClass().getDeclaredField("parentDispatcher");
                parentDispatcher.setAccessible(true);
                blockRendererDispatcher = (BlockRendererDispatcher)parentDispatcher.get(blockRendererDispatcher);
            } catch (Exception ignored) {}
        }

        if (isEnable()) {
            for (Block block : Block.REGISTRY) {
                if (block instanceof BlockAir) continue;
                originBlockLight.put(block, (int)ReflectionUtils.getPrivateField(Block.class, block, "lightValue", "field_149784_t"));
                block.setLightLevel(100.F);
            }
            originRender = blockRendererDispatcher.getBlockModelRenderer();
            ReflectionUtils.setPrivateField(BlockRendererDispatcher.class, blockRendererDispatcher, "blockModelRenderer", "field_175027_c", new XRayRenderHook(Minecraft.getMinecraft().getBlockColors()));
        } else {
            originBlockLight.forEach((block, light) -> {
                ReflectionUtils.setPrivateField(Block.class, block, "lightValue", "field_149784_t", light);
            });
            originBlockLight.clear();
            if (blockRendererDispatcher.getBlockModelRenderer() instanceof XRayRenderHook) {
                ReflectionUtils.setPrivateField(BlockRendererDispatcher.class, blockRendererDispatcher, "blockModelRenderer", "field_175027_c", originRender);
            }
        }

        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }
}
