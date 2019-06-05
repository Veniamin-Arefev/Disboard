package ru.veniamin_arefev.disboard.items;
// Created by Veniamin_arefev
// Date was 04.10.2018

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import ru.veniamin_arefev.disboard.Disboard;

public class SetTool extends Item {

    public SetTool(String name) {
        setRegistryName(Disboard.MOD_ID, name);
        setTranslationKey(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
    }
}
