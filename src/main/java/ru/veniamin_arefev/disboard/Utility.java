package ru.veniamin_arefev.disboard;
// Created by Veniamin_arefev
// Date was 08.05.2019

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public abstract class Utility {

    public static void anonsForAllExceptPlayer(MinecraftServer server, EntityPlayer playerEx, ITextComponent text) {
        server.getPlayerList().getPlayers().forEach(entityPlayerMP -> {
            if (!entityPlayerMP.isEntityEqual(playerEx)) {
                entityPlayerMP.sendMessage(text);
            }
        });
    }

    public static TextComponentString getModCaption() {
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

    public static String checksum(File file) {
        try {
            // file hashing
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            DigestInputStream dis = new DigestInputStream(new FileInputStream(file), md);
            while (dis.read() != -1) { //empty loop to clear the data
                md = dis.getMessageDigest();
            }

            // bytes to hex
            StringBuilder result = new StringBuilder();
            for (byte b : md.digest()) {
                result.append(String.format("%02x", b));
            }

            return result.toString();
        } catch (Exception e) {
            Disboard.logger.error("Failed to hash configs");
            return null;
        }
    }
}
