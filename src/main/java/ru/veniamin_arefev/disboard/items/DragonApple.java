package ru.veniamin_arefev.disboard.items;
// Created by Veniamin_arefev
// Date was 04.10.2018

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ru.veniamin_arefev.disboard.Disboard;

import java.util.Objects;

public class DragonApple extends ItemFood {

    public DragonApple(int amount, float saturation, boolean isWolfFood, String name) {
        super(amount, saturation, isWolfFood);
        setAlwaysEdible();
        setPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(11)), 30 * 20, 5), 1F);
        setRegistryName(Disboard.MOD_ID, name);
        setTranslationKey(name);
        setCreativeTab(CreativeTabs.FOOD);
        setMaxStackSize(64);
    }
}
