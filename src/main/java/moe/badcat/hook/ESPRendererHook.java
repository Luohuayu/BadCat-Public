package moe.badcat.hook;

import moe.badcat.BadCat;
import moe.badcat.module.modules.ModuleDebug;
import moe.badcat.utils.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ESPRendererHook extends EntityRenderer {
    private int ESPBox;
    private Method renderHandMethod;

    public boolean player = false;
    public boolean pixelmon = false;

    public ESPRendererHook(Minecraft mcIn, IResourceManager resourceManagerIn) {
        super(mcIn, resourceManagerIn);
        initPlayerBoxRender();
        ReflectionUtils.setPrivateField(EntityRenderer.class, this, "renderHand", "field_175074_C", Boolean.FALSE);
        try {
            renderHandMethod = EntityRenderer.class.getDeclaredMethod(BadCat.getInstance().isDeobfEnv() ? "renderHand" : "func_78476_b", float.class, int.class);
            renderHandMethod.setAccessible(true);
        } catch (Exception e) {
            if (ModuleDebug.enable) e.printStackTrace();
        }
    }

    private void initPlayerBoxRender() {
        this.ESPBox = GL11.glGenLists(1);
        GL11.glNewList(ESPBox, GL11.GL_COMPILE);
        GL11.glBegin(GL11.GL_LINES);
        AxisAlignedBB bb = new AxisAlignedBB(-0.5D, 0.0D, -0.5D, 0.5D, 1D, 0.5D);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glEnd();
        GL11.glEndList();
    }

    public void disable() {
        GL11.glDeleteLists(this.ESPBox, 1);
    }

    private void renderPlayerBox(float partialTicks) {
        EntityPlayer selfPlayer = Minecraft.getMinecraft().player;
        World world = Minecraft.getMinecraft().world;
        if (world != null && selfPlayer != null) {
            // GL settings
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(2);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            GL11.glPushMatrix();
            GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ);

            if (this.player) {
                for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
                    if (player == selfPlayer) continue;
                    if (player.isDead || player.getHealth() <= 0.0F) continue;
                    GL11.glPushMatrix();
                    GL11.glTranslated(player.prevPosX + (player.posX - player.prevPosX) * partialTicks, player.prevPosY + (player.posY - player.prevPosY) * partialTicks, player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);
                    GL11.glScaled(player.width + 0.1F, player.height + 0.1F, player.width + 0.1F);
                    GL11.glColor4f(0.0F, 0.8F, 0.0F, 0.75F);
                    GL11.glCallList(ESPBox);
                    GL11.glPopMatrix();
                }
            }

            if (this.pixelmon) {
                for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
                    String entityName = entity.getClass().getSimpleName();
                    if (!Objects.equals(entityName, "EntityPixelmon") && !Objects.equals(entityName, "EntityBreeding") && !Objects.equals(entityName, "EntityStatue")) continue;
                    GL11.glPushMatrix();
                    GL11.glTranslated(entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks, entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks, entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks);
                    GL11.glScaled(entity.width + 0.1F, entity.height + 0.1F, entity.width + 0.1F);
                    GL11.glColor4f(0.5F, 0.5F, 0.8F, 0.75F);
                    GL11.glCallList(ESPBox);
                    GL11.glPopMatrix();
                }
            }

            GL11.glPopMatrix();

            // GL resets
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }
    }

    @Override
    public void renderWorld(float partialTicks, long finishTimeNano) {
        super.renderWorld(partialTicks, finishTimeNano);
        renderPlayerBox(partialTicks);
        if (renderHandMethod != null) {
            GlStateManager.clear(256);
            try {
                renderHandMethod.invoke(this, partialTicks, 2);
            } catch (Exception e) {
                if (ModuleDebug.enable) e.printStackTrace();
            }
        }
    }
}
