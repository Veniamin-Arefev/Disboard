package ru.veniamin_arefev.disboard.JEI;
// Created by Veniamin_arefev
// Date was 05.10.2018


import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootTable;
import ru.veniamin_arefev.disboard.configs.Configs;
import ru.veniamin_arefev.disboard.items.Boxes.*;

import java.util.List;
import java.util.Set;

import static ru.veniamin_arefev.disboard.JEI.BoxEntry.parseLootTable;

public class BoxRecipeWrapper implements IRecipeWrapper, ITooltipCallback<ItemStack> {
    private ItemStack input;
    private NonNullList<ItemStack>outputs;
    private Set<BoxEntry.DropItem> dropItemSet;

    public BoxRecipeWrapper(ItemStack input) {
        this.input = input;
        outputs = NonNullList.create();
        Item item = input.getItem();
        if (item instanceof Box) {
            fillOutputs(Configs.lootTables.get(((Box) item).ID));
        }
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, input);
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    private void fillOutputs(LootTable lootTable){
        dropItemSet = parseLootTable(lootTable);
        dropItemSet.iterator().forEachRemaining(dropItem -> outputs.add(dropItem.getItemStack()));
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

//    @Override
//    public List<String> getTooltipStrings(int mouseX, int mouseY) { return null; }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
        dropItemSet.iterator().forEachRemaining(dropItem -> {
            if (dropItem.getItemStack().isItemEqual(ingredient)){
                tooltip.add((float)(MathHelper.floor(dropItem.getDropChance() *1000))/10+" %");
            }
        });
    }
}
