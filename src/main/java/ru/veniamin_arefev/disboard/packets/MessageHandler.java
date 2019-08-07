package ru.veniamin_arefev.disboard.packets;
// Created by Veniamin_arefev
// Date was 03.08.2019

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHandler implements IMessageHandler<Message, IMessage> {

    @Override
    public IMessage onMessage(Message message, MessageContext ctx) {
        System.out.println(ctx.side);
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
        if (message.type == 1) {
            serverPlayer.getServerWorld().addScheduledTask(() -> {
                serverPlayer.sendMessage(new TextComponentString("You have " + message.integer + " sticks"));
            });
        }
        return null;
    }
}
