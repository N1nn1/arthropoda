package com.ninni.arthropoda.client.renderer;


import com.ninni.arthropoda.client.init.ArthropodaEntityModelLayers;
import com.ninni.arthropoda.client.model.AntEntityModel;
import com.ninni.arthropoda.client.renderer.feature.AntAbdomenFeatureRenderer;
import com.ninni.arthropoda.entity.AntEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import static com.ninni.arthropoda.Arthropoda.*;

@Environment(EnvType.CLIENT)
public class AntEntityRenderer<T extends LivingEntity> extends MobEntityRenderer<AntEntity, AntEntityModel> {
    public static final Identifier TEXTURE    = new Identifier(MOD_ID, "textures/entity/ant/ant.png");

    public AntEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new AntEntityModel(ctx.getPart(ArthropodaEntityModelLayers.ANT)), 0.3F);
        this.addFeature(new AntAbdomenFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(AntEntity entity) { return TEXTURE; }
}

