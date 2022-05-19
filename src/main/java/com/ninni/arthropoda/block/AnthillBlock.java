package com.ninni.arthropoda.block;

import com.ninni.arthropoda.entity.AntEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AnthillBlock extends BlockWithEntity {
    protected AnthillBlock(Settings settings) { super(settings); }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (!world.isClient && blockEntity instanceof AnthillBlockEntity && EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            this.angerNearbyAnts(world, pos);
        }
    }

    @Override
    public void onEntityLand(BlockView world, Entity entity) {
        if (entity.bypassesLandingEffects()) { super.onEntityLand(world, entity); } else { this.angerNearbyAnts((World) world, entity.getBlockPos()); }
    }

    private void angerNearbyAnts(World world, BlockPos pos) {
        List<AntEntity> list = world.getNonSpectatingEntities(AntEntity.class, new Box(pos).expand(8.0, 6.0, 8.0));
        if (!list.isEmpty()) {
            List<PlayerEntity> list2 = world.getNonSpectatingEntities(PlayerEntity.class, new Box(pos).expand(8.0, 6.0, 8.0));
            for (AntEntity ant : list) {
                if (ant.getTarget() != null) continue;
                ant.setTarget(list2.get(world.random.nextInt(list2.size())));
            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) { return new AnthillBlockEntity(pos, state); }
}
