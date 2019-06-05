package ru.veniamin_arefev.disboard.commands;
// Created by Veniamin_arefev
// Date was 08.05.2019

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.veniamin_arefev.disboard.configs.Configs;

import java.util.ArrayList;
import java.util.List;

public class LootCommand extends CommandBase {
    private final ArrayList<String> aliases;

    public LootCommand(){
        aliases = new ArrayList<>();
//        aliases.add("dbloottable");
//        aliases.add("dblt");
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }
    //base command
    @Override
    public String getName() {
        return "loot_tables";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "loot_tables <action>";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (strings.length == 0) {
            iCommandSender.sendMessage(new TextComponentString("Base command that do nothing)"));
            return;
        }
        String action = strings[0];
        if (action.equals("reload")){
            Configs.reloadLootTables();
            iCommandSender.sendMessage(new TextComponentString("Successfully reloaded loot tables"));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}
