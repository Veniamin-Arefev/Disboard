package ru.veniamin_arefev.disboard.JEI;
// Created by Veniamin_arefev
// Date was 05.10.2018


import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import ru.veniamin_arefev.disboard.Disboard;

import java.util.Objects;

import static ru.veniamin_arefev.disboard.configs.Configs.lootTablesNames;

@JEIPlugin
public class JEIPluginDisboard implements IModPlugin {
    public static NonNullList<BoxRecipeWrapper> recipes = null;

    @Override
    public void register(IModRegistry registry) {
        Disboard.isJEILoaded = true;
        NonNullList<ItemStack> boxes = NonNullList.create();
        for (String name : lootTablesNames) {
            boxes.add(new ItemStack(Objects.requireNonNull(Item.getByNameOrId(Disboard.MOD_ID + ":" + name.substring(0, name.indexOf(".")))), 1));
        }
        recipes = NonNullList.create();
        boxes.forEach(itemStack -> recipes.add(new BoxRecipeWrapper(itemStack)));
        registry.addRecipes(recipes, BoxRecipeCategory.UID);
        registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(Objects.requireNonNull(Item.getByNameOrId("disboard:error_item"))));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new BoxRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    }
}
