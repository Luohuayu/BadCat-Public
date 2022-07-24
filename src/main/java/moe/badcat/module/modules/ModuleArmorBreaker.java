package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import moe.badcat.module.ITickable;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModuleArmorBreaker extends BaseEnableModule implements ITickable {
    public ModuleArmorBreaker() {
        super("原版类", "护甲破坏");
    }

    @Override
    public void tick() {
        if (isEnable()) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            World world = Minecraft.getMinecraft().world;
            PlayerControllerMP playerController = Minecraft.getMinecraft().playerController;

            if (player != null && world != null) {
                if (player.getHeldItemMainhand().isEmpty() || player.getHeldItemOffhand().isEmpty()) return;
                if (!(player.getHeldItemMainhand().getItem() instanceof ItemSword) || !(player.getHeldItemOffhand().getItem() instanceof ItemSword)) return;

                List<EntityPlayer> targetEntities = world.loadedEntityList
                        .parallelStream().filter(entity -> entity instanceof EntityPlayer)
                        .map(entity -> (EntityPlayer)entity)
                        .filter(entity -> !entity.isDead && entity.getHealth() > 0)
                        .filter(entity -> player.getDistance(entity) <= 6)
                        .filter(entity -> entity != player)
                        .collect(Collectors.toList());

                for (EntityPlayer targetEntity : targetEntities) {
                    player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    playerController.attackEntity(player, targetEntity);
                    player.swingArm(player.getActiveHand() == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                }
            }
        }
    }
}
