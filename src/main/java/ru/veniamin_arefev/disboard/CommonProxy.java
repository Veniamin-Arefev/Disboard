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
import ru.veniamin_arefev.disboard.configs.Configs;
import ru.veniamin_arefev.disboard.loot_functions.SetCustomName;
import ru.veniamin_arefev.disboard.packets.AskClientForUpdateDataMessage;
import ru.veniamin_arefev.disboard.packets.ClientHashedMessage;
import ru.veniamin_arefev.disboard.packets.NewConFigsMessage;
import ru.veniamin_arefev.disboard.packets.UUIDMessage;

import static ru.veniamin_arefev.disboard.Disboard.configs;

public class CommonProxy {
    public static final SimpleNetworkWrapper myChannel = NetworkRegistry.INSTANCE.newSimpleChannel(Disboard.MOD_ID);
    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    private static int id = 120;

    public void preInit(FMLPreInitializationEvent event) {
        myChannel.registerMessage(UUIDMessage.UUIDMessageHandler.class, UUIDMessage.class, id++, Side.CLIENT);
        myChannel.registerMessage(AskClientForUpdateDataMessage.AskClientForUpdateDataMessageHandler.class,
                AskClientForUpdateDataMessage.class, id++, Side.CLIENT);
        myChannel.registerMessage(ClientHashedMessage.ClientHashedMessageHandler.class, ClientHashedMessage.class,
                id++, Side.SERVER);
        myChannel.registerMessage(NewConFigsMessage.NewConFigsMessageHandler.class, NewConFigsMessage.class,
                id++, Side.CLIENT);
        LootFunctionManager.registerFunction(new SetCustomName.Serializer());
        ItemsRegister.register();
        configs = new Configs(event.getModConfigurationDirectory());
        configs.preInit();
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    public void init(FMLInitializationEvent event) {
        configs.init();
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    public void postInit(FMLPostInitializationEvent event) {

    }
}
