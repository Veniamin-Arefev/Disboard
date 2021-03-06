package ru.veniamin_arefev.disboard.JEI;
// Created by Veniamin_arefev
// Date was 18.10.2018


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import ru.veniamin_arefev.disboard.Disboard;
import ru.veniamin_arefev.disboard.loot_functions.SetCustomName;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BoxEntry {

    public BoxEntry() {
    }

    public static Set<DropItem> parseLootTable(LootTable table) {
        Set<DropItem> drops = new TreeSet<>((dr1, dr2) -> { //cant return 0, because TreeSet cant hold duplicate entries
            if (dr1.getDropChance() < dr2.getDropChance()) {
                return 1;
            } else {
                return -1;
            }
        });
        if (table == null) {
            Disboard.logger.error("FUCKING LOOT TABLE DOESNT DO THEIR WORK");
        }
        try {
            final float totalWeight1 = getEntries(getPools(table).get(0)).stream().mapToInt(entry -> entry.getEffectiveWeight(0)).sum();
            getPools(table).forEach(lootPool -> getEntries(lootPool)
                    .forEach(lootEntry -> {
                        if (lootEntry instanceof LootEntryItem) {
                            float chance = lootEntry.getEffectiveWeight(0) / totalWeight1;
                            int count = 1;
                            int meta = 0;
                            NBTTagCompound tag = null;
                            String name = null;
                            TextFormatting color = TextFormatting.WHITE;
                            LootFunction[] functions = ((LootEntryItem) lootEntry).functions;
                            for (LootFunction function : functions) {
                                if (function instanceof SetCustomName) {
                                    try {
                                        name = ((SetCustomName) function).customName;
                                    } catch (Exception e) {
                                        Disboard.logger.error(e.fillInStackTrace());
                                    }
                                }
                                if (function instanceof SetCount) {
                                    try {
                                        count = MathHelper.floor(((SetCount) function).countRange.getMin());
                                    } catch (Exception e) {
                                        Disboard.logger.error(e.fillInStackTrace());
                                    }
                                }
                                if (function instanceof SetMetadata) {
                                    try {
                                        meta = MathHelper.floor(((SetMetadata) function).metaRange.getMin());
                                    } catch (Exception e) {
                                        Disboard.logger.error(e.fillInStackTrace());
                                    }
                                }
                                if (function instanceof SetNBT) {
                                    try {
                                        tag = ((SetNBT) function).tag;
                                    } catch (Exception e) {
                                        Disboard.logger.error(e.fillInStackTrace());
                                    }
                                }
                            }
                            try {
                                ItemStack itemStack = new ItemStack(((LootEntryItem) lootEntry).item, count, meta);
                                if (tag != null) {
                                    if (itemStack.getTagCompound() != null) {
                                        tag.merge(itemStack.getTagCompound());
                                    }
                                    itemStack.setTagCompound(tag);
                                }
                                if (name != null) {
                                    itemStack.setStackDisplayName(name);
                                    itemStack.getTextComponent().getStyle().setItalic(false);
                                }
                                itemStack.getTextComponent().getStyle().setColor(color);
                                drops.add(new DropItem(itemStack, chance));

                            } catch (Exception e) {
                                String patchedItemName = lootEntry.getEntryName().substring(0, lootEntry.getEntryName().indexOf('#'));
                                ItemStack itemStack = new ItemStack(Item.getByNameOrId(Disboard.MOD_ID + ":error_item"), count).setStackDisplayName("id = " + patchedItemName + " : meta is" + meta);
                                drops.add(new DropItem(itemStack, chance));
                                Disboard.logger.error("Invalid item in loot table - " + lootEntry.getEntryName());
                                Disboard.logger.error(e.fillInStackTrace());
                            }
                        }
                    }));
        } catch (Exception e) {
            Disboard.logger.error("Failed to load loot table. Is it empty - " + getPools(table).isEmpty());
            Disboard.logger.error(e.fillInStackTrace());
        }
        return drops;
    }

    private static List<LootPool> getPools(LootTable table) {
        return ReflectionHelper.getPrivateValue(LootTable.class, table, "pools", "field_186466_c");
    }

    private static List<LootEntry> getEntries(LootPool pool) {
        return ReflectionHelper.getPrivateValue(LootPool.class, pool, "lootEntries", "field_186453_a");
    }

    public static class DropItem {
        private ItemStack itemStack;
        private float dropChance;

        public DropItem(ItemStack itemStack, float dropChance) {
            this.itemStack = itemStack;
            this.dropChance = dropChance;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public float getDropChance() {
            return dropChance;
        }
    }
}
