package ru.veniamin_arefev.disboard.packets;
// Created by Veniamin_arefev
// Date was 16.08.2019

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.veniamin_arefev.disboard.Disboard;
import ru.veniamin_arefev.disboard.Utility;
import ru.veniamin_arefev.disboard.configs.Configs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ClientHashedMessage implements IMessage { //from client to server
    private static ArrayList<String> hashList = Lists.newArrayList();

    public ClientHashedMessage() {
    }

    public ClientHashedMessage(String nothing) {
        hashList.clear();
        hashFiles();
    }

    private void hashFiles() {
        Configs.filesList.forEach(file -> {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (IOException e) {
                Disboard.logger.error("Failed to create empty config file");

            }
            hashList.add(Utility.checksum(file));
        });
        Disboard.logger.debug("Created hashes:");
        hashList.forEach(hash -> Disboard.logger.debug(hash));
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        hashList.clear();
        for (int i = 0; i < Configs.filesList.size(); i++) {
            int length = byteBuf.readInt();
            hashList.add(byteBuf.readCharSequence(length, StandardCharsets.UTF_8).toString());
        }
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        hashList.forEach(hash -> {
            byteBuf.writeInt(hash.length());
            byteBuf.writeCharSequence(hash, StandardCharsets.UTF_8);
        });
    }

    public static class ClientHashedMessageHandler implements IMessageHandler<ClientHashedMessage, IMessage> {
        @Override
        public IMessage onMessage(ClientHashedMessage clientHashedMessage, MessageContext messageContext) {
            Disboard.logger.debug("Received hashes:");
            hashList.forEach(hash -> Disboard.logger.debug(hash));
            ArrayList<Boolean> neededConfigs = Lists.newArrayList();
            Disboard.logger.debug("Want to update this:");
            for (int i = 0; i < Configs.filesList.size(); i++) {
                boolean needed = !Utility.checksum(Configs.filesList.get(i)).equals(hashList.get(i));
                Disboard.logger.debug("Need update: " + needed + " for " + Configs.filesList.get(i).getName());
                neededConfigs.add(needed);
            }
            NewConFigsMessage response = new NewConFigsMessage(neededConfigs);
            return response;
        }
    }
}
