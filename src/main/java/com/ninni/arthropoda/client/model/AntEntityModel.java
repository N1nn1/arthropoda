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
import net.minecraft.util.math.MathHelper;

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
    public void setAngles(AntEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        limbDistance = MathHelper.clamp(limbDistance, -0.45F, 0.45F);

        float speed = 1.0F;
        float degree = 2.0F;

        head.pitch = headPitch * ((float) Math.PI / 180f);
        head.yaw = headYaw * ((float) Math.PI / 180f);

        leftAntenna.roll = MathHelper.sin(animationProgress * speed * 0.1F) * degree * 0.4F * 0.25F;
        leftAntenna.pitch = MathHelper.cos(animationProgress * speed * 0.05F) * degree * 0.75F * 0.25F;

        rightAntenna.roll = MathHelper.sin(animationProgress * speed * 0.1F + (float)Math.PI) * degree * 0.4F * 0.25F;
        rightAntenna.pitch = MathHelper.cos(animationProgress * speed * 0.05F + (float)Math.PI) * degree * 0.75F * 0.25F;

        abdomen.roll = MathHelper.sin(limbAngle * speed * 0.6F) * degree * 0.2F * limbDistance;

        leftForeLeg.roll = MathHelper.cos(limbAngle * speed * 0.6F) * degree * 1F * limbDistance + 0.7854F;
        leftForeLeg.yaw = MathHelper.sin(limbAngle * speed * 0.6F) * degree * -1F * limbDistance + 0.3927F;
        rightForeLeg.roll = MathHelper.cos(limbAngle * speed * 0.6F + (float)Math.PI/2) * degree * 1F * limbDistance - 0.7854F;
        rightForeLeg.yaw = MathHelper.sin(limbAngle * speed * 0.6F + (float)Math.PI/2) * degree * -1F * limbDistance - 0.3927F;

        leftMidLeg.roll = MathHelper.sin(limbAngle * speed * 0.6F) * degree * 1F * limbDistance + 0.7854F;
        leftMidLeg.yaw = MathHelper.cos(limbAngle * speed * 0.6F) * degree * 1F * limbDistance;
        rightMidLeg.roll = MathHelper.sin(limbAngle * speed * 0.6F + (float)Math.PI/2) * degree * 1F * limbDistance - 0.7854F;
        rightMidLeg.yaw = MathHelper.cos(limbAngle * speed * 0.6F + (float)Math.PI/2) * degree * 1F * limbDistance;

        leftBackLeg.roll = MathHelper.cos(limbAngle * speed * 0.6F + (float)Math.PI) * degree * 1F * limbDistance + 0.7854F;
        leftBackLeg.yaw = MathHelper.sin(limbAngle * speed * 0.6F + (float)Math.PI) * degree * -1F * limbDistance - 0.3927F;
        rightBackLeg.roll = MathHelper.cos(limbAngle * speed * 0.6F + (float)Math.PI/1.5F) * degree * 1F * limbDistance - 0.7854F;
        rightBackLeg.yaw = MathHelper.sin(limbAngle * speed * 0.6F + (float)Math.PI/1.5F) * degree * -1F * limbDistance + 0.3927F;

        if (entity.isInSittingPose()) {
            body.pivotY = 22.5F;

            leftForeLeg.roll =0;
            leftMidLeg.roll = 0;
            leftBackLeg.roll =0;
            rightForeLeg.roll =0;
            rightMidLeg.roll = 0;
            rightBackLeg.roll =0;

        } else {
            body.pivotY = 20.5F;
            leftForeLeg.roll = 0.7854F;
            leftMidLeg.roll = 0.7854F;
            leftBackLeg.roll = 0.7854F;
            rightForeLeg.roll = -0.7854F;
            rightMidLeg.roll = -0.7854F;
            rightBackLeg.roll = -0.7854F;
        }
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() { return ImmutableList.of(); }

    @Override
    protected Iterable<ModelPart> getBodyParts() { return ImmutableList.of(body); }
}
