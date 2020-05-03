package ru.veniamin_arefev.disboard.configs;
// Created by Veniamin_arefev
// Date was 08.05.2019

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.ForgeHooks;
import org.apache.logging.log4j.Level;
import ru.veniamin_arefev.disboard.Disboard;
import ru.veniamin_arefev.disboard.JEI.BoxRecipeWrapper;
import ru.veniamin_arefev.disboard.JEI.JEIPluginDisboard;
import ru.veniamin_arefev.disboard.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class Configs {
    public static final ArrayList<String> lootTablesNames = Lists.newArrayList(
            "common_box.json", //start with 0
            "rare_box.json",                    //1
            "epic_box.json",                    //2
            "legendary_box.json",               //3
            "crystal_box.json",                 //4
            "tools_box.json",                   //5
            "black_box.json"                    //6
    );
    private static final Gson GSON_INSTANCE = (new GsonBuilder()).registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer()).registerTypeAdapter(LootPool.class, new LootPool.Serializer()).registerTypeAdapter(LootTable.class, new LootTable.Serializer()).registerTypeHierarchyAdapter(LootEntry.class, new LootEntry.Serializer()).registerTypeHierarchyAdapter(LootFunction.class, new net.minecraft.world.storage.loot.functions.LootFunctionManager.Serializer()).registerTypeHierarchyAdapter(LootCondition.class, new net.minecraft.world.storage.loot.conditions.LootConditionManager.Serializer()).registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer()).create();
    public static ArrayList<File> filesList = Lists.newArrayList();
    public static ArrayList<LootTable> lootTables = Lists.newArrayList();
    public static String lastUUID;
    private static File configDir;
    private static Properties properties;

    public Configs(File configDir) {
        Configs.configDir = new File(configDir, "disboard");
    }

    private static void writeDefaultFileList() {
        filesList.clear();
        for (String name : lootTablesNames) {
            filesList.add(new File(configDir, name));
        }
    }

    public static void writeDedicatedFileList() {
        filesList.clear();
        File dedicatedDir = new File(configDir, lastUUID);
        if (!dedicatedDir.exists()) {
            boolean created = false;
            try {
                created = dedicatedDir.mkdir();
            } catch (SecurityException e) {
                Disboard.logger.error("Cant create server config directory");
                Disboard.logger.error(e);
            } finally {
                if (!created) {
                    Disboard.logger.error("Failed to create server config directory");
                }
            }
        }
        for (String name : lootTablesNames) {
            filesList.add(new File(dedicatedDir, name));
        }
    }

    public static boolean reloadLootTables() {
        lootTables.clear();
        try {
            LootTableManager manager = new LootTableManager(File.createTempFile("disboard", "temp"));
            int i = 0;
            for (File file : filesList) {
                String data = Utility.readJson(file.getAbsolutePath());
                lootTables.add(ForgeHooks.loadLootTable(GSON_INSTANCE, new ResourceLocation(file.getName()), data, true, manager));
            }
            if (Disboard.isJEILoaded) {
                if (!JEIPluginDisboard.recipes.isEmpty()) {
                    JEIPluginDisboard.recipes.forEach(BoxRecipeWrapper::init);
                }
            }
            return true;
        } catch (IOException e) {
            Disboard.logger.error("Error occupied during reloading loot tables", e.fillInStackTrace());
            return false;
        }
    }

    public static String getUUID() {
        return properties.getProperty("UUID");
    }

    private void dealWithProperties(File configDir) {
        File propFile = new File(configDir, "disboard.properties");
        properties = new Properties();

        if (propFile.exists()) {
            try {
                properties.load(new FileInputStream(propFile));
            } catch (IOException e) {
                Disboard.logger.error("Cant load properties file");
                Disboard.logger.error(e);
            }
        } else {
            properties.setProperty("UUID", UUID.randomUUID().toString());
            try {
                propFile.createNewFile();
                properties.store(new FileOutputStream(propFile), "DON'T TOUCH THIS");
            } catch (IOException e) {
                Disboard.logger.error("Cant save properties file");
                Disboard.logger.error(e.fillInStackTrace());
            }
        }
    }

    public void preInit() {
        if (!configDir.exists()) {
            boolean created = false;
            try {
                created = configDir.mkdir();
            } catch (SecurityException e) {
                Disboard.logger.error("Cant create config directory");
                Disboard.logger.error(e);
            } finally {
                if (!created) {
                    Disboard.logger.error("Failed to create config directory");
                }
            }
        }
        dealWithProperties(configDir);
        lootTablesCopy();
        writeDefaultFileList();
    }

    public void init() {
        reloadLootTables();
    }

    public void initOnServerStart() {
        Disboard.logger.debug("singleInit");
        writeDefaultFileList();
        reloadLootTables();
    }

    private void lootTablesCopy() {
        try {
            String s = Disboard.class.getResource("").toString();
            s = s.substring(0, s.lastIndexOf("ru")) + "assets/disboard/loot_tables/boxes";
            URI uri = URI.create(s);
            Path configPath = Paths.get(configDir.toURI());
            Path defaultsPath;
            if (uri.getScheme().equals("jar")) {
                FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                defaultsPath = fileSystem.getPath("/assets", "disboard", "loot_tables", "boxes");
            } else {
                defaultsPath = Paths.get(uri);
            }

            Stream<Path> walk = Files.walk(defaultsPath, 1);
            List<String> fileNames = NonNullList.create();
            for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
                String name = it.next().getFileName().toString();
                if (name.contains(".json")) {
                    fileNames.add(name);
                }
            }

            for (String fileName : fileNames) {
                try {
                    Files.copy(this.getClass().getClassLoader().getResourceAsStream("assets/disboard/loot_tables/boxes/" + fileName), configPath.resolve(fileName));
                } catch (FileAlreadyExistsException e) {
                    Disboard.logger.log(Level.DEBUG, fileName + " already exists");
                }
            }

        } catch (IOException e) {
            if (!(e instanceof FileAlreadyExistsException)) {
                Disboard.logger.error("Cant create default loot tables", e.fillInStackTrace());
            }
        }
    }
}
