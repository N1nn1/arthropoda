package com.ninni.arthropoda.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.ninni.arthropoda.Arthropoda.*;

public class ArthropodaBlocks {

    public static final Block ANTHILL = register("anthill", new AnthillBlock(AbstractBlock.Settings.of(Material.SOLID_ORGANIC).strength(0.6F).sounds(BlockSoundGroup.ROOTED_DIRT)));

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(MOD_ID, id), block);
    }
}
