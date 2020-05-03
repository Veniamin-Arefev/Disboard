package ru.veniamin_arefev.disboard;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import ru.veniamin_arefev.disboard.commands.DisboardCommand;
import ru.veniamin_arefev.disboard.configs.Configs;

@Mod(
        modid = Disboard.MOD_ID,
        name = Disboard.MOD_NAME,
        version = Disboard.VERSION
)
public class Disboard {

    public static final String MOD_ID = "disboard";
    public static final String MOD_NAME = "Disboard";
    public static final String VERSION = "1.010-RELEASE";
    public static final CreativeTabs boxTab = new BoxTab("Boxes_tab");
    public static Logger logger;
    public static Configs configs;
    @Mod.Instance(MOD_ID)
    public static Disboard INSTANCE;
    @SidedProxy(clientSide = "ru.veniamin_arefev.disboard.ClientProxy", serverSide = "ru.veniamin_arefev.disboard.CommonProxy")
    public static CommonProxy proxy;

    public static boolean isJEILoaded = false;

    @Mod.EventHandler
    public static void serverLoading(FMLServerStartingEvent event) { //server starting only
        event.registerServerCommand(new DisboardCommand());
        configs.initOnServerStart();
    }

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
