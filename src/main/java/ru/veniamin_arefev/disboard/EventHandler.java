package ru.veniamin_arefev.disboard;
// Created by Veniamin_arefev
// Date was 08.05.2019

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void preInitGUI(GuiScreenEvent.InitGuiEvent.Post event){
        if (event.getGui() instanceof GuiMultiplayer) {
            NonNullList<GuiButton> buttons= NonNullList.create();
            for (GuiButton button : event.getButtonList()){
                if (button.id == 8){
                    button.width += 20;
                    button.x -= 158;
                }
                if ((button.id == 2) || (button.id == 3) || (button.id == 4) || (button.id == 7)) {
                    buttons.add(button);
                } else if (button.id == 1){
                    button.width = 200;
                    button.displayString = I18n.format("multiplayerGUI.join");
                }
            }
            event.getButtonList().removeAll(buttons);
        }
    }
}
