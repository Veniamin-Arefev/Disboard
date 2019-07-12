package ru.veniamin_arefev.disboard.commands;
// Created by Veniamin_arefev
// Date was 08.05.2019

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.veniamin_arefev.disboard.Disboard;
import ru.veniamin_arefev.disboard.Utility;
import ru.veniamin_arefev.disboard.configs.Configs;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisboardCommand extends CommandBase {
    private final ArrayList<String> aliases;

    public DisboardCommand(){
        aliases = new ArrayList<>();
        aliases.add("db");
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public String getName() {
        return Disboard.MOD_ID;
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "loot_tables <action>";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] args) {
        if (args.length == 0) {
            iCommandSender.sendMessage(new TextComponentString("Creator of this mod is Veniamin_arefev"));
        }
        if (args.length > 0) {
            if (args[0].equals("info" )){
                iCommandSender.sendMessage(new TextComponentString("This is small mod for for Disboard servers"));
                return;
            }
            if (args[0].equals("lootTables") &&args[1].equals("reload")) {
                if (Configs.reloadLootTables()){
                    TextComponentString textComponents = new TextComponentString("Successfully reloaded loot tables");
                    textComponents.getStyle().setColor(TextFormatting.GREEN);
                    iCommandSender.sendMessage(Utility.getModCaption().appendSibling(textComponents));
                } else {
                    TextComponentString textComponents = new TextComponentString("Failed to reload loot tables, see log files for more info");
                    textComponents.getStyle().setColor(TextFormatting.RED);
                    iCommandSender.sendMessage(Utility.getModCaption().appendSibling(textComponents));
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "info", "lootTables");
        } else if (args.length == 2 && "lootTables".equals(args[0])) {
            return getListOfStringsMatchingLastWord(args, "reload");
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (sender.getCommandSenderEntity() instanceof EntityPlayer){
            return PermissionAPI.hasPermission((EntityPlayer) sender.getCommandSenderEntity(), "disboard.disboard.base");
        } else {
            return false;
        }
    }
}
