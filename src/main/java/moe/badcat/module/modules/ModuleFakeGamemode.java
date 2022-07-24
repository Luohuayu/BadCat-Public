package moe.badcat.module.modules;

import moe.badcat.BadCat;
import moe.badcat.module.BaseModule;
import moe.badcat.module.ITickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.world.GameType;

public class ModuleFakeGamemode extends BaseModule implements ITickable {
    private Type type = Type.None;
    private boolean clickGuiClosed = false;

    public ModuleFakeGamemode() {
        super("其他", "伪造游戏模式");
    }

    @Override
    public void onUse() {
        if (type == Type.None) type = Type.Survival;
        else if (type == Type.Survival) type = Type.Creative;
        else if (type == Type.Creative) type = Type.Adventure;
        else if (type == Type.Adventure) type = Type.Spectator;
        else if (type == Type.Spectator) type = Type.None;

        if (type == Type.None) {
            PlayerControllerMP playerController = Minecraft.getMinecraft().playerController;
            if (playerController != null && playerController.getCurrentGameType() != GameType.SURVIVAL) {
                playerController.setGameType(GameType.SURVIVAL);
            }
        }

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
                PlayerControllerMP playerController = Minecraft.getMinecraft().playerController;
                if (playerController == null) return;

                switch (type) {
                    case Survival:
                    {
                        if (playerController.getCurrentGameType() != GameType.SURVIVAL) {
                            playerController.setGameType(GameType.SURVIVAL);
                        }
                        break;
                    }
                    case Creative:
                    {
                        if (playerController.getCurrentGameType() != GameType.CREATIVE) {
                            playerController.setGameType(GameType.CREATIVE);
                        }
                        break;
                    }
                    case Adventure:
                    {
                        if (playerController.getCurrentGameType() != GameType.ADVENTURE) {
                            playerController.setGameType(GameType.ADVENTURE);
                        }
                        break;
                    }
                    case Spectator:
                    {
                        if (playerController.getCurrentGameType() != GameType.SPECTATOR) {
                            playerController.setGameType(GameType.SPECTATOR);
                        }
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }

            }
        }
    }

    enum Type {
        None("关"),
        Survival("生存"),
        Creative("创造"),
        Adventure("冒险"),
        Spectator("旁观");

        String displayName;
        Type(String displayName) {
            this.displayName = displayName;
        }
    }
}
