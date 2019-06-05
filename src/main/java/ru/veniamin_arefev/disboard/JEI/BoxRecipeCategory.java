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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import ru.veniamin_arefev.disboard.Disboard;

import javax.annotation.Nullable;

public class BoxRecipeCategory implements IRecipeCategory {
    public static final String UID = Disboard.MOD_ID+"loot";
    private final IDrawable background;
    private final IDrawable icon;

    public BoxRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(new ResourceLocation(Disboard.MOD_ID,
                "gui/box_drops_gui.png"), 0, 0, 164, 127);
        icon = guiHelper.createDrawable(new ResourceLocation(Disboard.MOD_ID,
                "gui/box_drops_icon.png"), 0, 0, 16, 16);
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
        return new TextComponentTranslation("boxes.box_drop_gui.title").getFormattedText();
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
/*        // Любые надписи, которые будут на каждом рецепте.
        // Все координаты идут относительно самого рецепта. Все width и height рассчитывать не нужно.
        minecraft.fontRenderer.drawString("Block drops:", 5, 13, 0xffffffff, true);
*/
    }

    @Override
    public void setRecipe( IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 74, 2);
        isg.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0).get(0));

        int x = 1;
        int y = 46;

        for(int i = 1; i < Math.min(50, ingredients.getOutputs(VanillaTypes.ITEM).size())+1; i++) {
            isg.init(i, false, x, y);
            x+= 16;
            if(x >= 1+16*10) {
                x = 1;
                y+= 16;
            }
        }
        for(int i = 1; i < Math.min(50, ingredients.getOutputs(VanillaTypes.ITEM).size())+1; i++)
            isg.set(i, ingredients.getOutputs(VanillaTypes.ITEM).get(i - 1));
        isg.addTooltipCallback((BoxRecipeWrapper) wrapper);
    }
}
