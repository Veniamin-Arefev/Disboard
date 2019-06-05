package ru.veniamin_arefev.disboard.items;
// Created by Veniamin_arefev
// Date was 02.12.2018

import net.minecraft.item.Item;
import ru.veniamin_arefev.disboard.Disboard;

public class CraftItem extends Item {

    public CraftItem(String name) {
        setRegistryName(Disboard.MOD_ID, name);
        setTranslationKey(name);
        setMaxStackSize(64);
    }

}
