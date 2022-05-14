package com.ninni.arthropoda;

import com.google.common.collect.ImmutableMap;
import com.ninni.arthropoda.client.init.ArthropodaEntityModelLayers;
import com.ninni.arthropoda.client.model.AntEntityModel;
import com.ninni.arthropoda.client.renderer.AntEntityRenderer;
import com.ninni.arthropoda.entity.ArthropodaEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class ArthropodaClient implements ClientModInitializer {

    @Override
    @SuppressWarnings({ "deprecation" })
    public void onInitializeClient() {
        EntityRendererRegistry erri = EntityRendererRegistry.INSTANCE;
        erri.register(ArthropodaEntities.ANT, AntEntityRenderer::new);
        new ImmutableMap.Builder<EntityModelLayer, EntityModelLayerRegistry.TexturedModelDataProvider>()
            .put(ArthropodaEntityModelLayers.ANT, AntEntityModel::getTexturedModelData)
            .build().forEach(EntityModelLayerRegistry::registerModelLayer);
    }
}

