package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import java.lang.reflect.Array;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ModulePixelmonRidingCrash extends BaseEnableModule {
    private SimpleNetworkWrapper networkWrapper;
    private IMessage movementPacket;

    public ModulePixelmonRidingCrash() {
        super("发包崩服", "宝可梦骑乘");
    }

    @Override
    public boolean initModule() {
        try {
            Class<?> class_Pixelmon = Class.forName("com.pixelmonmod.pixelmon.Pixelmon");

            try {
                String[] version = ((String) class_Pixelmon.getField("VERSION").get(null)).split("\\.");
                if (version.length == 3 && Integer.parseInt(version[0] + version[1] + version[2]) >= 710) return false;
            } catch (Exception ignored) {}

            networkWrapper = (SimpleNetworkWrapper) class_Pixelmon.getDeclaredField("network").get(null);
            if (networkWrapper == null) return false;

            Class<?> class_Movement = Class.forName("com.pixelmonmod.pixelmon.comm.packetHandlers.Movement");
            Class<?> class_EnumMovement = Class.forName("com.pixelmonmod.pixelmon.enums.EnumMovement");
            Class<?> class_EnumMovementType = Class.forName("com.pixelmonmod.pixelmon.comm.packetHandlers.EnumMovementType");
            Object enumJump = class_EnumMovement.getField("Jump").get(null);
            Object enumRiding = class_EnumMovementType.getField("Riding").get(null);
            Object[] movementArray = (Object[]) Array.newInstance(class_EnumMovement, 8000);
            Arrays.fill(movementArray, enumJump);
            movementPacket = (IMessage) class_Movement.getConstructor(movementArray.getClass(), class_EnumMovementType)
                    .newInstance(new Object[]{ movementArray, enumRiding });

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
                    networkWrapper.sendToServer(movementPacket);
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
