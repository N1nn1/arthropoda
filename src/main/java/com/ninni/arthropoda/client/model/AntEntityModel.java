package com.ninni.arthropoda.client.model;

import com.google.common.collect.ImmutableList;
import com.ninni.arthropoda.entity.AntEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.AnimalModel;

@SuppressWarnings("FieldCanBeLocal, unused")
public class AntEntityModel extends AnimalModel<AntEntity> {
    private final ModelPart root;

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leftAntenna;
    private final ModelPart rightAntenna;
    private final ModelPart abdomen;
    private final ModelPart leftForeLeg;
    private final ModelPart rightForeLeg;
    private final ModelPart leftMidLeg;
    private final ModelPart rightMidLeg;
    private final ModelPart leftBackLeg;
    private final ModelPart rightBackLeg;

    public AntEntityModel(ModelPart root) {
        this.root = root;

        this.body         = root.getChild("body");

        this.head         = body.getChild("head");
        this.abdomen      = body.getChild("abdomen");
        this.leftForeLeg  = body.getChild("leftForeLeg");
        this.rightForeLeg = body.getChild("rightForeLeg");
        this.leftMidLeg   = body.getChild("leftMidLeg");
        this.rightMidLeg  = body.getChild("rightMidLeg");
        this.leftBackLeg  = body.getChild("leftBackLeg");
        this.rightBackLeg = body.getChild("rightBackLeg");

        this.leftAntenna  = head.getChild("leftAntenna");
        this.rightAntenna = head.getChild("rightAntenna");
    }

    public static TexturedModelData getTexturedModelData() {

        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        ModelPartData body = root.addChild(
            "body",
            ModelPartBuilder.create()
                            .uv(14, 13)
                            .mirrored(false)
                            .cuboid(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, 20.5F, 0.5F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData head = body.addChild(
            "head",
            ModelPartBuilder.create()
                            .uv(0, 9)
                            .mirrored(false)
                            .cuboid(-2.5F, -3.0F, -3.0F, 5.0F, 4.0F, 4.0F, new Dilation(0.0F))
                            .uv(15, 0)
                            .mirrored(false)
                            .cuboid(-1.5F, 0.0F, -4.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, -1.5F, -1.5F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData leftAntenna = head.addChild(
            "leftAntenna",
            ModelPartBuilder.create()
                            .uv(0, 14)
                            .mirrored(false)
                            .cuboid(0.0F, -4.0F, -3.5F, 0.0F, 4.0F, 4.0F, new Dilation(0.0F)),
            ModelTransform.of(1.0F, -3.0F, -1.5F, 0.0F, -0.3927F, 0.0F)
        );

        ModelPartData rightAntenna = head.addChild(
            "rightAntenna",
            ModelPartBuilder.create()
                            .uv(0, 14)
                            .mirrored(false)
                            .cuboid(0.0F, -4.0F, -3.5F, 0.0F, 4.0F, 4.0F, new Dilation(0.0F)),
            ModelTransform.of(-1.0F, -3.0F, -1.5F, 0.0F, 0.3927F, 0.0F)
        );

        ModelPartData abdomen = body.addChild(
            "abdomen",
            ModelPartBuilder.create()
                            .uv(0, 0)
                            .mirrored(false)
                            .cuboid(-2.5F, -2.0F, -1.0F, 5.0F, 4.0F, 5.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, -1.0F, 1.5F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData leftForeLeg = body.addChild(
            "leftForeLeg",
            ModelPartBuilder.create()
                            .uv(-1, 22)
                            .mirrored(false)
                            .cuboid(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F)),
            ModelTransform.of(1.5F, 1.5F, -1.5F, 0.0F, 0.3927F, 0.7854F)
        );

        ModelPartData rightForeLeg = body.addChild(
            "rightForeLeg",
            ModelPartBuilder.create()
                            .uv(-1, 22)
                            .mirrored(false)
                            .cuboid(-3.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F)),
            ModelTransform.of(-1.5F, 1.5F, -1.5F, 0.0F, -0.3927F, -0.7854F)
        );

        ModelPartData leftMidLeg = body.addChild(
            "leftMidLeg",
            ModelPartBuilder.create()
                            .uv(-1, 22)
                            .mirrored(false)
                            .cuboid(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F)),
            ModelTransform.of(1.5F, 1.5F, -0.5F, 0.0F, 0.0F, 0.7854F)
        );

        ModelPartData rightMidLeg = body.addChild(
            "rightMidLeg",
            ModelPartBuilder.create()
                            .uv(-1, 22)
                            .mirrored(false)
                            .cuboid(-3.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F)),
            ModelTransform.of(-1.5F, 1.5F, -0.5F, 0.0F, 0.0F, -0.7854F)
        );

        ModelPartData leftBackLeg = body.addChild(
            "leftBackLeg",
            ModelPartBuilder.create()
                            .uv(-1, 22)
                            .mirrored(false)
                            .cuboid(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F)),
            ModelTransform.of(1.5F, 1.5F, 0.5F, 0.0F, -0.3927F, 0.7854F)
        );

        ModelPartData rightBackLeg = body.addChild(
            "rightBackLeg",
            ModelPartBuilder.create()
                            .uv(-1, 22)
                            .mirrored(false)
                            .cuboid(-3.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F)),
            ModelTransform.of(-1.5F, 1.5F, 0.5F, 0.0F, 0.3927F, -0.7854F)
        );

        return TexturedModelData.of(data, 32, 32);
    }

    @Override
    public void setAngles(AntEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {}

    @Override
    protected Iterable<ModelPart> getHeadParts() { return ImmutableList.of(); }

    @Override
    protected Iterable<ModelPart> getBodyParts() { return ImmutableList.of(body); }
}
