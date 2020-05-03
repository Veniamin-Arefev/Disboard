package ru.veniamin_arefev.disboard.packets;
// Created by Veniamin_arefev
// Date was 16.08.2019

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.veniamin_arefev.disboard.Disboard;

public class AskClientForUpdateDataMessage implements IMessage { //from server to client

    public AskClientForUpdateDataMessage() {
    }


    @Override
    public void fromBytes(ByteBuf byteBuf) {
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
    }

    public static class AskClientForUpdateDataMessageHandler implements IMessageHandler<AskClientForUpdateDataMessage, IMessage> {
        @Override
        public IMessage onMessage(AskClientForUpdateDataMessage uuidMessage, MessageContext messageContext) {
            Disboard.logger.debug("Send hashed files to dedicated server");
            ClientHashedMessage response = new ClientHashedMessage("Hashed files");
            return response;
        }
    }
}
