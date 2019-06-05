package ru.veniamin_arefev.disboard;
// Created by Veniamin_arefev
// Date was 06.05.2019

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static ru.veniamin_arefev.disboard.Disboard.configs;

public class CommonProxy {
    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    public void preInit(FMLPreInitializationEvent event) {
//        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        ItemsRegister.register();
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
