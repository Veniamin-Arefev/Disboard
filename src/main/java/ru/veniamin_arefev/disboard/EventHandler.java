package ru.veniamin_arefev.disboard;
// Created by Veniamin_arefev
// Date was 08.05.2019

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.veniamin_arefev.disboard.configs.Configs;
import ru.veniamin_arefev.disboard.packets.UUIDMessage;

import java.util.Objects;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void preInitGUI(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiMultiplayer) {
            NonNullList<GuiButton> buttons = NonNullList.create();
            for (GuiButton button : event.getButtonList()) {
                if (button.id == 8) {
                    button.width += 20;
                    button.x -= 158;
                }
                if ((button.id == 2) || (button.id == 3) || (button.id == 4) || (button.id == 7)) {
                    buttons.add(button);
                } else if (button.id == 1) {
                    button.width = 200;
                    button.displayString = I18n.format("multiplayerGUI.join");
                }
            }
            event.getButtonList().removeAll(buttons);
        }
    }

    @SubscribeEvent
    public static void PlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (Objects.requireNonNull(event.player.getServer()).isDedicatedServer()) {
            CommonProxy.myChannel.sendTo(new UUIDMessage(Configs.getUUID()), (EntityPlayerMP) event.player);
        }
    }
}
