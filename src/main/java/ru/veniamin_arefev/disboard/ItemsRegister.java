package ru.veniamin_arefev.disboard;
// Created by Veniamin_arefev
// Date was 23.09.2018

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.veniamin_arefev.disboard.items.*;
import ru.veniamin_arefev.disboard.items.Boxes.*;

public class ItemsRegister {
    private static ItemModelMesher mesher;

    private static Item common_box = new Common_box("common_box");
    private static Item rare_box = new Rare_box("rare_box");
    private static Item epic_box = new Epic_box("epic_box");
    private static Item legendary_box = new Legendary_box("legendary_box");
    private static Item tools_box = new Tools_box("tools_box");
    private static Item crystal_box = new Crystal_box("crystal_box");
    private static Item black_box = new Black_box("black_box");

    private static ItemFood dragon_apple = new DragonApple(20,100F,false,"dragon_apple");
    private static Item set_tool = new SetTool("set_tool");
    private static Item region_tool = new RegionTool("region_tool");
    private static Item error_item = new ErrorItem("error_item");

    private static Item craft0 = new CraftItem("cast");
    private static Item craft1 = new CraftItem("dia");
    private static Item craft2 = new CraftItem("ingot");
    private static Item craft3 = new CraftItem("pearl");


    public static void register()
    {
        setRegister(common_box);
        setRegister(rare_box);
        setRegister(epic_box);
        setRegister(legendary_box);
        setRegister(tools_box);
        setRegister(crystal_box);
        setRegister(black_box);

        setRegister(dragon_apple);
        setRegister(set_tool);
        setRegister(region_tool);
        setRegister(error_item);

        setRegister(craft0);
        setRegister(craft1);
        setRegister(craft2);
        setRegister(craft3);
    }

    private static void setRegister(Item item)
    {
        ForgeRegistries.ITEMS.register(item);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender()
    {
        mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        setRender(common_box);
        setRender(rare_box);
        setRender(epic_box);
        setRender(legendary_box);
        setRender(tools_box);
        setRender(crystal_box);
        setRender(black_box);

        setRender(dragon_apple);
        setRender(set_tool);
        setRender(region_tool);
        setRender(error_item);

        setRender(craft0);
        setRender(craft1);
        setRender(craft2);
        setRender(craft3);

        mesher = null;
    }

    @SideOnly(Side.CLIENT)
    private static void setRender(Item item)
    {
        mesher.register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));

    }
}

