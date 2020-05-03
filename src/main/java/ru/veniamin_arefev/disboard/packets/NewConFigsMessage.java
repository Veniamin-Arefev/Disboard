package ru.veniamin_arefev.disboard.packets;
// Created by Veniamin_arefev
// Date was 16.08.2019

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.veniamin_arefev.disboard.Disboard;
import ru.veniamin_arefev.disboard.configs.Configs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class NewConFigsMessage implements IMessage { //from server to client
    private ArrayList<Boolean> neededConfigs;

    public NewConFigsMessage() {
    }

    public NewConFigsMessage(ArrayList<Boolean> neededConfigs) {
        this.neededConfigs = neededConfigs;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        for (int i = 0; i < Configs.filesList.size(); i++) {
            try {
                if (byteBuf.readBoolean()) { // 1 - means that is should be rewritten
                    int length = byteBuf.readInt();
                    byteBuf.readBytes(new FileOutputStream(Configs.filesList.get(i), false), length);
                }
            } catch (Exception e) {
                Disboard.logger.error("Failed to rewrite server configs on client side", e.fillInStackTrace());
            }
        }
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        for (int i = 0; i < neededConfigs.size(); i++) {
            try {
                if (neededConfigs.get(i)) { // 1 - means that is should be send to client
                    byteBuf.writeBoolean(true);
                    int length = (int) Configs.filesList.get(i).length();
                    Disboard.logger.debug("We need total of " + length + " for " + Configs.filesList.get(i));
                    byteBuf.writeInt(length);
                    byteBuf.writeBytes(new FileInputStream(Configs.filesList.get(i)), length);
                } else {
                    byteBuf.writeBoolean(false);
                }
            } catch (Exception e) {
                Disboard.logger.error("Failed to write server configs in packet", e.fillInStackTrace());
            }
        }
    }

    public static class NewConFigsMessageHandler implements IMessageHandler<NewConFigsMessage, IMessage> {
        @Override
        public IMessage onMessage(NewConFigsMessage newConFigsMessage, MessageContext messageContext) {
            //all config files are already rewritten
            Configs.reloadLootTables();
            return null;
        }
    }
}
