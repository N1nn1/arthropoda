package com.ninni.arthropoda.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class AnthillBlockEntity extends BlockEntity {
    public AnthillBlockEntity(BlockPos pos, BlockState state) { super(ArthropodaBlockEntities.ANTHILL, pos, state); }
}
