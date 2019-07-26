package ru.veniamin_arefev.disboard.JEI;
// Created by Veniamin_arefev
// Date was 05.10.2018


import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootTable;
import ru.veniamin_arefev.disboard.configs.Configs;
import ru.veniamin_arefev.disboard.items.Boxes.Box;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ru.veniamin_arefev.disboard.JEI.BoxEntry.parseLootTable;
import static ru.veniamin_arefev.disboard.JEI.BoxRecipeCategory.iconNext;
import static ru.veniamin_arefev.disboard.JEI.BoxRecipeCategory.iconPrevious;

public class BoxRecipeWrapper implements IRecipeWrapper, ITooltipCallback<ItemStack> {
    private ItemStack input;
    private NonNullList<ItemStack> outputs;
    private Set<BoxEntry.DropItem> dropItemSet;
    private int currentIndexOfFirstItem = 0;
    public IGuiItemStackGroup isg;

    public BoxRecipeWrapper(ItemStack input) {
        this.input = input;
        init();
    }

    public void init() {
        outputs = NonNullList.create();
        Item item = input.getItem();
        assert item instanceof Box;
        fillOutputs(Configs.lootTables.get(((Box) item).ID));
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, input);
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    private void fillOutputs(LootTable lootTable) {
        dropItemSet = parseLootTable(lootTable);
        dropItemSet.iterator().forEachRemaining(dropItem -> outputs.add(dropItem.getItemStack()));
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (outputs.size() > 40) {
            iconNext.draw(minecraft, 130, 5);
            iconPrevious.draw(minecraft, 130, 22);
        }
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (outputs.size() <= 40) {
            return false;
        } else {
            if (mouseX > 130 && mouseX < 146 && mouseY > 5 && mouseY < 21) { //Next
                if (outputs.size() > currentIndexOfFirstItem + 40) {
                    currentIndexOfFirstItem += 10;
                    for (int i = 1; i <= 40; i++) {
                        if (i <= outputs.size() - currentIndexOfFirstItem) {
                            isg.set(i, outputs.get(i + currentIndexOfFirstItem - 1));
                        } else {
                            isg.set(i, ItemStack.EMPTY);
                        }
                    }
                }
                return true;
            }
            if (mouseX > 130 && mouseX < 146 && mouseY > 22 && mouseY < 38) { //Previous
                if (currentIndexOfFirstItem >= 10) {
                    currentIndexOfFirstItem -= 10;
                    for (int i = 1; i <= 40; i++)
                        isg.set(i, outputs.get(i + currentIndexOfFirstItem - 1));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
        tooltip.add((float) (MathHelper.floor(((BoxEntry.DropItem) dropItemSet.toArray()[slotIndex - 1 + currentIndexOfFirstItem]).getDropChance() * 1000)) / 10 + " %");
//        dropItemSet.iterator().forEachRemaining(dropItem -> {
//            if (dropItem.getItemStack().isItemEqual(ingredient)) {
//                tooltip.add((float) (MathHelper.floor(dropItem.getDropChance() * 1000)) / 10 + " %");
//            }
//        });
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (outputs.size() > 40) {
            if (mouseX > 130 && mouseX < 146 && mouseY > 5 && mouseY < 21) {
                return Collections.singletonList(I18n.format("jei.box_drop_gui.next"));
            }
            if (mouseX > 130 && mouseX < 146 && mouseY > 22 && mouseY < 38) {
                return Collections.singletonList(I18n.format("jei.box_drop_gui.previous"));
            }
        }
        return Collections.emptyList();
    }
}
