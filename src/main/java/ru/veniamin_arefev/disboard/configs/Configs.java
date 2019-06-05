package ru.veniamin_arefev.disboard.configs;
// Created by Veniamin_arefev
// Date was 08.05.2019

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.ForgeHooks;
import ru.veniamin_arefev.disboard.Disboard;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

public class Configs {
    private File configDir;
    private static final Gson GSON_INSTANCE = (new GsonBuilder()).registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer()).registerTypeAdapter(LootPool.class, new net.minecraft.world.storage.loot.LootPool.Serializer()).registerTypeAdapter(LootTable.class, new net.minecraft.world.storage.loot.LootTable.Serializer()).registerTypeHierarchyAdapter(LootEntry.class, new net.minecraft.world.storage.loot.LootEntry.Serializer()).registerTypeHierarchyAdapter(LootFunction.class, new net.minecraft.world.storage.loot.functions.LootFunctionManager.Serializer()).registerTypeHierarchyAdapter(LootCondition.class, new net.minecraft.world.storage.loot.conditions.LootConditionManager.Serializer()).registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new net.minecraft.world.storage.loot.LootContext.EntityTarget.Serializer()).create();
    public static final ArrayList<String> lootTablesNames = Lists.newArrayList(
            "common_box.json", //start with 0
            "rare_box.json",                    //1
            "epic_box.json",                    //2
            "legendary_box.json",               //3
            "crystal_box.json",                 //4
            "tools_box.json",                   //5
            "black_box.json"                    //6
    );
    private static ArrayList<File> filesList = Lists.newArrayList();
    public static ArrayList<LootTable> lootTables = Lists.newArrayList();



    public Configs(File configDir) {
        this.configDir = new File(configDir,"disboard");

    }

    public void preInit(){
        boolean created = false;
        try {
            created = configDir.mkdir();
        } catch (SecurityException e) {
            Disboard.logger.error("Cant create config directory");
            Disboard.logger.error(e);
        } finally {
            if (!created && !configDir.exists()){
                Disboard.logger.error("Cant create config directory");
            }
        }
        lootTablesCopy();
        writeFileList();
    }

    private void lootTablesCopy(){
        try {
            Path configPath = Paths.get(configDir.toURI());
            Path defaultsPath = Paths.get(Disboard.class.getClassLoader().getResource("assets/disboard/loot_tables/boxes").toURI());
            Files.walkFileTree(defaultsPath, new CopyFileVisitor(defaultsPath,configPath, StandardCopyOption.COPY_ATTRIBUTES));
        } catch (Exception e) {
            if (!(e instanceof FileAlreadyExistsException)) {
                Disboard.logger.error("Cant create default loot tables",e.fillInStackTrace());
            }
        }
    }

    private void writeFileList(){
        filesList.clear();
        for (String name : lootTablesNames) {
            filesList.add(new File(configDir,name));
        }
    }

    public void init(){
        reloadLootTables();
    }

    public static void reloadLootTables(){
        lootTables.clear();
        try {
            LootTableManager manager = new LootTableManager(File.createTempFile("disboard", "temp"));
            for (File file : filesList) {
                String data = com.google.common.io.Files.toString(file, StandardCharsets.UTF_8);
                lootTables.add(ForgeHooks.loadLootTable(GSON_INSTANCE, new ResourceLocation(file.getName()), data, true, manager));
            }
        } catch (IOException e) {
            Disboard.logger.error("Error occupied during reloading loot tables",e.fillInStackTrace());
        }
    }
}
