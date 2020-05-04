package ru.veniamin_arefev.disboard.items.Boxes;
// Created by Veniamin_arefev
// Date was 23.09.2018

import net.minecraft.client.resources.I18n;
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
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import ru.veniamin_arefev.disboard.Disboard;
import ru.veniamin_arefev.disboard.Utility;
import ru.veniamin_arefev.disboard.configs.Configs;

import java.util.List;

public abstract class Box extends Item {
    public int ID;
    private String name;
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            LootContext ctx = new LootContext.Builder((WorldServer) worldIn).withLuck(playerIn.getLuck()).build();
            List<ItemStack> stacks = Configs.lootTables.get(ID).generateLootForPools(worldIn.rand, ctx);
            if (!stacks.isEmpty()) {
                playerIn.getHeldItem(handIn).setCount(playerIn.getHeldItem(handIn).getCount() - 1);
                playerIn.sendMessage(getMessageForOpener(stacks.get(0)));
                Utility.anonsForAllExceptPlayer(worldIn.getMinecraftServer(), playerIn, getMessageForOthers(playerIn.getDisplayName(), stacks.get(0)));
                playerIn.dropItem(stacks.get(0), false);
            } else {
                ITextComponent message = new TextComponentTranslation("boxes.box_drop.error");
                playerIn.sendMessage(message);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    private TextComponentString getMessageForOpener(ItemStack lootStack) {
        TextComponentString textComponents = Utility.getModCaption();
        ITextComponent temp = new TextComponentTranslation("boxes.box_drop.opener.opened");
        temp.getStyle().setColor(TextFormatting.GREEN);
        textComponents.appendSibling(temp);

        textComponents.appendSibling(getMessageEnding(lootStack));

        return textComponents;
    }

    private TextComponentString getMessageForOthers(ITextComponent playerName, ItemStack lootStack) {
        TextComponentString textComponents = Utility.getModCaption();
        textComponents.appendSibling(playerName);

        ITextComponent temp = new TextComponentTranslation("boxes.box_drop.other.opened");
        temp.getStyle().setColor(TextFormatting.GREEN);
        textComponents.appendSibling(temp);

        textComponents.appendSibling(getMessageEnding(lootStack));

        return textComponents;
    }

    private TextComponentString getMessageEnding(ItemStack lootStack) {
        TextComponentString textComponents = new TextComponentString(" ");
        ITextComponent temp = new TextComponentTranslation("item." + name + ".name");
        temp.getStyle().setColor(nameFormatting);
        textComponents.appendSibling(temp);

        textComponents.appendSibling(new TextComponentString(" "));

        temp = new TextComponentTranslation("boxes.box_drop.and_got");
        temp.getStyle().setColor(TextFormatting.GREEN);
        textComponents.appendSibling(temp);

        textComponents.appendSibling(new TextComponentString(" "));

        if (I18n.format(lootStack.getTranslationKey() + ".name").equals(lootStack.getDisplayName())) {
            //its not custom name
            temp = new TextComponentString("[");
            temp.getStyle().setColor(lootFormatting);
            textComponents.appendSibling(temp);
            temp = new TextComponentTranslation(lootStack.getTranslationKey() + ".name");
            HoverEvent hoverEvent = lootStack.getTextComponent().getStyle().getHoverEvent();
            temp.getSiblings().forEach(iTextComponent -> iTextComponent.getStyle().setHoverEvent(hoverEvent));
            temp.getStyle().setColor(lootFormatting).setHoverEvent(hoverEvent);
            textComponents.appendSibling(temp);
            temp = new TextComponentString("]");
            temp.getStyle().setColor(lootFormatting);
            textComponents.appendSibling(temp);
        } else {
            textComponents.appendSibling(lootStack.getTextComponent());
        }

        return textComponents;
    }
}
