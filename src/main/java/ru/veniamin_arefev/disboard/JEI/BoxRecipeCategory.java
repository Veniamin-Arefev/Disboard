package ru.veniamin_arefev.disboard.JEI;
// Created by Veniamin_arefev
// Date was 05.10.2018


import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import ru.veniamin_arefev.disboard.Disboard;

import javax.annotation.Nullable;

public class BoxRecipeCategory implements IRecipeCategory {
    public static final String UID = Disboard.MOD_ID + ".loot";
    public static IDrawable iconNext;
    public static IDrawable iconPrevious;
    private final IDrawable background;
    private final IDrawable icon;
    private final int guiWidth = 183;
    private final int guiHeight = 124;

    public BoxRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(new ResourceLocation(Disboard.MOD_ID,
                "gui/box_drops_gui.png"), 0, 0, guiWidth, guiHeight);
        icon = guiHelper.createDrawable(new ResourceLocation(Disboard.MOD_ID,
                "gui/box_drops_gui.png"), 184, 32, 16, 16);
        iconNext = guiHelper.createDrawable(new ResourceLocation(Disboard.MOD_ID,
                "gui/box_drops_gui.png"), 184, 0, 16, 16);
        iconPrevious = guiHelper.createDrawable(new ResourceLocation(Disboard.MOD_ID,
                "gui/box_drops_gui.png"), 184, 16, 16, 16);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return I18n.format("jei.box_drop_gui.title");
    }

    @Override
    public String getModName() {
        return Disboard.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.fontRenderer.drawSplitString(I18n.format("jei.box_drop_gui.rmb"), 12, 15, 70, 0xffffffff);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        ((BoxRecipeWrapper) wrapper).isg = layout.getItemStacks();
        isg.init(0, true, 83, 3);
        isg.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0).get(0));

        int x = 2;
        int y = 49;

        for (int i = 1; i <= 40; i++) {
            isg.init(i, false, x, y);
            x += 18;
            if (x >= guiWidth - 16) {
                x = 2;
                y += 18;
            }
        }
        for (int i = 1; i < Math.min(40, ingredients.getOutputs(VanillaTypes.ITEM).size()) + 1; i++)
            isg.set(i, ingredients.getOutputs(VanillaTypes.ITEM).get(i - 1));
        isg.addTooltipCallback((BoxRecipeWrapper) wrapper);
    }
}
