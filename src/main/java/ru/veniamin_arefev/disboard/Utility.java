package ru.veniamin_arefev.disboard;
// Created by Veniamin_arefev
// Date was 08.05.2019

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public abstract class Utility {

    public static void anonsForAllExceptPlayer(MinecraftServer server, EntityPlayer playerEx, ITextComponent text) {
        server.getPlayerList().getPlayers().forEach(entityPlayerMP -> {
            if (!entityPlayerMP.isEntityEqual(playerEx)){
                entityPlayerMP.sendMessage(text);
        }
        });
    }

    public static TextComponentString getModCaption(){
        TextComponentString textComponents = new TextComponentString("[");
        textComponents.getStyle().setColor(TextFormatting.DARK_BLUE);

        ITextComponent temp = new TextComponentString(Disboard.MOD_NAME.toUpperCase());
        temp.getStyle().setColor(TextFormatting.GOLD);
        textComponents.appendSibling(temp);

        temp = new TextComponentString("] ");
        temp.getStyle().setColor(TextFormatting.DARK_BLUE);
        textComponents.appendSibling(temp);
        return textComponents;
    }
}
