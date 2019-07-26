package ru.veniamin_arefev.disboard;
// Created by Veniamin_arefev
// Date was 06.05.2019

import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.veniamin_arefev.disboard.configs.Configs;
import ru.veniamin_arefev.disboard.loot_functions.SetCustomName;

import static ru.veniamin_arefev.disboard.Disboard.configs;

public class CommonProxy {
    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    public void preInit(FMLPreInitializationEvent event) {
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
        PermissionAPI.registerNode("disboard.disboard.base", DefaultPermissionLevel.OP, "Base command permission");
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    public void postInit(FMLPostInitializationEvent event) {

    }
}
