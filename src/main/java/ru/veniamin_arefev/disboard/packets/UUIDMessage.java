package ru.veniamin_arefev.disboard.packets;
// Created by Veniamin_arefev
// Date was 16.08.2019

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.veniamin_arefev.disboard.Disboard;
import ru.veniamin_arefev.disboard.configs.Configs;

import java.nio.charset.StandardCharsets;

public class UUIDMessage implements IMessage { //from server to client
    private String UUID;

    public UUIDMessage() {
    }

    public UUIDMessage(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        UUID = byteBuf.readCharSequence(length, StandardCharsets.UTF_8).toString();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(UUID.length());
        byteBuf.writeCharSequence(UUID, StandardCharsets.UTF_8);
    }

    public static class UUIDMessageHandler implements IMessageHandler<UUIDMessage, IMessage> {
        @Override
        public IMessage onMessage(UUIDMessage uuidMessage, MessageContext messageContext) {
            Disboard.logger.debug("Dedicated server UUID is " + uuidMessage.UUID);
            Configs.lastUUID = uuidMessage.UUID;
            Configs.writeDedicatedFileList();
            ClientHashedMessage response = new ClientHashedMessage("Hashed files");
            return response;
        }
    }
}
