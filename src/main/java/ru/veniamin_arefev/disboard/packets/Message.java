package ru.veniamin_arefev.disboard.packets;
// Created by Veniamin_arefev
// Date was 03.08.2019

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.Charset;

public class Message implements IMessage {

    //default constructor. without it all crush
    public Message() {
    }

    public Message(int type, String string) {
        this.type = type;
        this.string = string;
    }

    public Message(int type, int integer) {
        this.type = type;
        this.integer = integer;
    }

    public int type;
    public String string;
    public int integer;
    // 0 - just String
    // 1 - just int
    // 2 -
    // 3 -


    @Override
    public void toBytes(ByteBuf buf) { // Writing
        buf.writeInt(type);
        if (type == 0) {
            buf.writeInt(string.length());
            buf.writeBytes(string.getBytes());
        }
        if (type == 1) {
            buf.writeInt(integer);
        }


    }

    @Override
    public void fromBytes(ByteBuf buf) { // Reading
        type = buf.readInt();
        if (type == 0) {
            int lenght = buf.readInt();
            string = buf.readCharSequence(lenght, Charset.defaultCharset()).toString();
            return;
        }
        if (type == 1) {
            integer = buf.readInt();
            return;
        }
    }
}
