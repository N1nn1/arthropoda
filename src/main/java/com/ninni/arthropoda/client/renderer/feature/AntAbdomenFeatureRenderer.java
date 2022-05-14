package com.ninni.arthropoda.client.renderer.feature;

import com.ninni.arthropoda.client.model.AntEntityModel;
import com.ninni.arthropoda.entity.AntEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static com.ninni.arthropoda.Arthropoda.*;

@Environment(EnvType.CLIENT)
public class AntAbdomenFeatureRenderer extends FeatureRenderer<AntEntity, AntEntityModel> {
    private static final Identifier ABDOMEN = new Identifier(MOD_ID, "textures/entity/ant/ant_abdomen_overlay.png");

    public AntAbdomenFeatureRenderer(FeatureRendererContext<AntEntity, AntEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AntEntity ant, float f, float g, float h, float j, float k, float l) {
        if (ant.isTamed() && !ant.isInvisible()) {
            float[] fs = ant.getAbdomenColor().getColorComponents();
            renderModel(this.getContextModel(), ABDOMEN, matrixStack, vertexConsumerProvider, i, ant, fs[0], fs[1], fs[2]);
        }
    }
}
