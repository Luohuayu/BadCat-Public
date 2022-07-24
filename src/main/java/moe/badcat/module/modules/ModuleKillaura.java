package moe.badcat.module.modules;

import moe.badcat.BadCat;
import moe.badcat.module.BaseEnableModule;
import moe.badcat.module.ITickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

import java.util.Comparator;
import java.util.stream.Stream;

public class ModuleKillaura extends BaseEnableModule implements ITickable {
    private Type type = Type.None;
    private boolean clickGuiClosed = false;

    public ModuleKillaura() {
        super("原版类", "自动攻击");
    }

    @Override
    public void onUse() {
        if (type == Type.None) type = Type.Player;
        else if (type == Type.Player) type = Type.Living;
        else if (type == Type.Living) type = Type.Mob;
        else if (type == Type.Mob) type = Type.Animal;
        else if (type == Type.Animal) type = Type.All;
        else type = Type.None;

        clickGuiClosed = false;
    }

    @Override
    public String getDisplayText() {
        return String.format("%s [%s]", this.displayName, type.displayName);
    }

    @Override
    public void tick() {
        if (type != Type.None) {
            if (clickGuiClosed || (clickGuiClosed = BadCat.getInstance().isClickGuiClose())) {
                EntityPlayerSP player = Minecraft.getMinecraft().player;

                if (player == null || player.getCooledAttackStrength(0) < 1) return;

                Stream<EntityLivingBase> stream = Minecraft.getMinecraft().world.loadedEntityList
                        .parallelStream().filter(entity -> entity instanceof EntityLivingBase)
                        .map(entity -> (EntityLivingBase)entity)
                        .filter(entity -> !entity.isDead && entity.getHealth() > 0)
                        .filter(entity -> !entity.isInvisible())
                        .filter(entity -> player.getDistance(entity) <= 6)
                        .filter(entity -> entity != player)
                        .filter(entity ->
                                (type == Type.Player && entity instanceof EntityPlayer) ||
                                (type == Type.Living && entity instanceof EntityLiving) ||
                                (type == Type.Mob && entity instanceof EntityMob) ||
                                (type == Type.Animal && entity instanceof EntityAnimal) ||
                                (type == Type.All));
                Entity target = stream.min(Comparator.comparingDouble(player::getDistance)).orElse(null);

                if (target != null) {
                    Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().player, target);
                    player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }

    enum Type {
        None("关"),
        Player("玩家"),
        Living("生物"),
        Mob("怪物"),
        Animal("动物"),
        All("所有");

        String displayName;
        Type(String displayName) {
            this.displayName = displayName;
        }
    }
}
