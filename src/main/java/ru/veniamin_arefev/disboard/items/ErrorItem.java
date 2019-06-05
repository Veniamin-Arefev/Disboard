package ru.veniamin_arefev.disboard.items;
// Created by Veniamin_arefev
// Date was 27.10.2018


import net.minecraft.item.Item;
import ru.veniamin_arefev.disboard.Disboard;

public class ErrorItem extends Item {

    public ErrorItem(String name) {
        setRegistryName(Disboard.MOD_ID, name);
        setTranslationKey(name);
        setMaxStackSize(64);
    }

}
