package com.ninni.arthropoda.block;

import com.mojang.datafixers.types.Type;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import static com.ninni.arthropoda.Arthropoda.*;

public class ArthropodaBlockEntities {

    public static final BlockEntityType<AnthillBlockEntity> ANTHILL = register("anthill", FabricBlockEntityTypeBuilder.create(AnthillBlockEntity::new, ArthropodaBlocks.ANTHILL));


    private static <T extends BlockEntity> BlockEntityType<T> register(String id, FabricBlockEntityTypeBuilder<T> builder) {
        Identifier identifier = new Identifier(MOD_ID, id);

        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, identifier.toString());
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier, builder.build(type));
    }
}
