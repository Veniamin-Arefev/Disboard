package ru.veniamin_arefev.disboard.loot_functions;
// Created by Veniamin_arefev
// Date was 23.07.2019

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

public class SetCustomName extends LootFunction {

    public final String customName;

    public SetCustomName(LootCondition[] conditionsIn, String customName) {
        super(conditionsIn);
        this.customName = customName;
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        stack.setStackDisplayName("§r" + customName);
        return stack;
    }

    public static class Serializer extends net.minecraft.world.storage.loot.functions.LootFunction.Serializer<SetCustomName> {
        public Serializer() {
            super(new ResourceLocation("set_custom_name"), SetCustomName.class);
        }

        public void serialize(JsonObject object, SetCustomName functionClazz, JsonSerializationContext serializationContext) {
            object.add("name", serializationContext.serialize(functionClazz.customName));
        }

        public SetCustomName deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            return new SetCustomName(conditionsIn, JsonUtils.deserializeClass(object, "name", deserializationContext, String.class));
        }
    }
}
