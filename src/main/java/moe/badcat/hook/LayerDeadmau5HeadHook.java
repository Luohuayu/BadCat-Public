package moe.badcat.hook;

import moe.badcat.module.modules.ModuleDebug;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class LayerDeadmau5HeadHook extends LayerDeadmau5Head {
    private final RenderPlayer playerRenderer;

    public LayerDeadmau5HeadHook(RenderPlayer playerRendererIn) {
        super(playerRendererIn);
        this.playerRenderer = playerRendererIn;
        try {
            Minecraft.getMinecraft().getTextureManager().loadTexture(
                    AbstractClientPlayer.getLocationSkin("deadmau5"),
                    new DynamicTexture(
                            ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAAEAAAAAgCAYAAACinX6EAAAACXBIWXMAAAsTAAALEwEAmpwYAAAA7klEQVRoge2Uyw3CMBBExyS0AOWkhaUDWqASWqADtgWa4YI4cQMl5hQEBzv+RAxC+06Rs9GMnpx13nvE8HpqAKyddOfoYAARWQK4F3y6UtVLSWYObcLMAOBakdEDwHG7+zjcHPav58C7W0VmMoupASedB/CoyIhfsTA1mclMCgAAJ91QGqCquQLGrL40Mwc3tQPmQERKQhYF8rJJ2QFz0CD8K4zn7u2sjczPylduwC+TtAP+GRPALsDGBLALsDEB7AJsTAC7ABsTwC7AxgSwC7AxAewCbEwAuwAbE8AuwMYEsAuwMQHsAmxMALsAmyckMS816HikIQAAAABJRU5ErkJggg==")))
                    )
            );
        } catch (Exception e) {
            if (ModuleDebug.enable) e.printStackTrace();
        }
    }

    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entitylivingbaseIn.isInvisible()) {
            this.playerRenderer.bindTexture(AbstractClientPlayer.getLocationSkin("deadmau5"));

            for (int i = 0; i < 2; ++i) {
                float f = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
                float f1 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
                GlStateManager.pushMatrix();
                GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.375F * (float)(i * 2 - 1), 0.0F, 0.0F);
                GlStateManager.translate(0.0F, -0.375F, 0.0F);
                GlStateManager.rotate(-f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
                float f2 = 1.3333334F;
                GlStateManager.scale(1.3333334F, 1.3333334F, 1.3333334F);
                this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
                GlStateManager.popMatrix();
            }
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
