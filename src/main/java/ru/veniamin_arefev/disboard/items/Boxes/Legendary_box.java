package ru.veniamin_arefev.disboard.items.Boxes;
// Created by Veniamin_arefev
// Date was 29.09.2018

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class Legendary_box extends Box {
    public Legendary_box(String name) {
        super(name, 3, TextFormatting.DARK_RED, TextFormatting.GOLD);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.getHeldItem(handIn).getItem() instanceof Legendary_box) {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        } else {
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
    }
}
