package ru.veniamin_arefev.disboard.items.Boxes;
// Created by Veniamin_arefev
// Date was 23.09.2018

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import ru.veniamin_arefev.disboard.Disboard;
import ru.veniamin_arefev.disboard.Utility;
import ru.veniamin_arefev.disboard.configs.Configs;

import java.util.List;

public abstract class Box extends Item {
    private String name;
    public int ID;
    private TextFormatting nameFormatting;
    private TextFormatting lootFormatting;

    public Box(String name, int ID, TextFormatting nameFormatting, TextFormatting lootFormatting) {
        this.name = name;
        this.ID = ID;
        this.nameFormatting = nameFormatting;
        this.lootFormatting = lootFormatting;
        setRegistryName(Disboard.MOD_ID, name);
        setTranslationKey(name);
        setCreativeTab(Disboard.boxTab);
        setMaxStackSize(64);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand  handIn) {
        if (!worldIn.isRemote) {
            LootContext ctx = new LootContext.Builder((WorldServer) worldIn).withLuck(playerIn.getLuck()).build();
            List<ItemStack> stacks = Configs.lootTables.get(ID).generateLootForPools(worldIn.rand, ctx);
            if (!stacks.isEmpty()) {
                playerIn.getHeldItem(handIn).setCount(playerIn.getHeldItem(handIn).getCount() - 1);
                playerIn.sendMessage(getMessageForOpener(stacks.get(0)));
                Utility.anonsFromAllExceptPlayer(worldIn.getMinecraftServer(),playerIn,getMessageForOthers(playerIn.getName(),stacks.get(0)));
                playerIn.dropItem(stacks.get(0), false);
            } else {
                ITextComponent message = new TextComponentTranslation("boxes.box_drop.error");
                playerIn.sendMessage(message);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
    public TextComponentString getMessageForOpener(ItemStack lootStack) {
        TextComponentString textComponents = Utility.getModCaption();
        ITextComponent temp = new TextComponentTranslation("boxes.box_drop.opener.opened");
        temp.getStyle().setColor(TextFormatting.GREEN);
        textComponents.appendSibling(temp);

        temp = new TextComponentTranslation("boxes."+name+".get_name");
        temp.getStyle().setColor(nameFormatting);
        textComponents.appendSibling(temp);

        temp = new TextComponentTranslation("boxes.box_drop.and_got");
        temp.getStyle().setColor(TextFormatting.YELLOW);
        textComponents.appendSibling(temp);

        temp = lootStack.getTextComponent();
        temp.getStyle().setColor(lootFormatting).setUnderlined(true);
        textComponents.appendSibling(temp);

        return textComponents;
    }

    public TextComponentString getMessageForOthers(String playerName,ItemStack lootStack) {
        TextComponentString textComponents = Utility.getModCaption();
        ITextComponent temp = new TextComponentString(playerName + " ");
        temp.getStyle().setColor(TextFormatting.WHITE);
        textComponents.appendSibling(temp);

        temp = new TextComponentTranslation("boxes.box_drop.opener.opened");
        temp.getStyle().setColor(TextFormatting.GREEN);
        textComponents.appendSibling(temp);

        temp = new TextComponentTranslation("boxes."+name+".get_name");
        temp.getStyle().setColor(nameFormatting);
        textComponents.appendSibling(temp);

        temp = new TextComponentTranslation("boxes.box_drop.and_got");
        temp.getStyle().setColor(TextFormatting.YELLOW);
        textComponents.appendSibling(temp);

        temp = lootStack.getTextComponent();
        temp.getStyle().setColor(lootFormatting).setUnderlined(true);
        textComponents.appendSibling(temp);

        return textComponents;
    }
}
