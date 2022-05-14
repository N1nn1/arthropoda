package com.ninni.arthropoda.item;

import com.ninni.arthropoda.block.ArthropodaBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.ninni.arthropoda.Arthropoda.*;

@SuppressWarnings("unused")
public class ArthropodaItems {
    public static final Item ANTHILL = register("anthill", new BlockItem(ArthropodaBlocks.ANTHILL, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(MOD_ID, id), item);
    }
}


