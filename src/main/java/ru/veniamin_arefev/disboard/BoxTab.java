package ru.veniamin_arefev.disboard;
// Created by Veniamin_arefev
// Date was 23.09.2018


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BoxTab extends CreativeTabs {

    public BoxTab(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.DIAMOND,1);
    }

}
