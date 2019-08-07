package ru.veniamin_arefev.disboard;
// Created by Veniamin_arefev
// Date was 06.05.2019

import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.veniamin_arefev.disboard.configs.Configs;
import ru.veniamin_arefev.disboard.loot_functions.SetCustomName;
import ru.veniamin_arefev.disboard.packets.Message;
import ru.veniamin_arefev.disboard.packets.MessageHandler;

import static ru.veniamin_arefev.disboard.Disboard.configs;

public class CommonProxy {
    public static final SimpleNetworkWrapper myChannel = NetworkRegistry.INSTANCE.newSimpleChannel(Disboard.MOD_ID);
    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("I am invoked");
        System.out.println("Now side is " + event.getSide().toString());
        if (event.getSide().isClient()) {
            myChannel.registerMessage(MessageHandler.class, Message.class, 0, Side.CLIENT);
        } else if (event.getSide().isServer()) {
            myChannel.registerMessage(MessageHandler.class, Message.class, 1, Side.SERVER);
        }
        LootFunctionManager.registerFunction(new SetCustomName.Serializer());
        ItemsRegister.register();
        configs = new Configs(event.getModConfigurationDirectory(), event.getSide());
        configs.preInit();
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    public void init(FMLInitializationEvent event) {
        configs.init();
        PermissionAPI.registerNode("disboard.disboard.base", DefaultPermissionLevel.OP, "Base command permission");
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    public void postInit(FMLPostInitializationEvent event) {

    }
}
