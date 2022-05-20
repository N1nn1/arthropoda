package com.ninni.arthropoda.entity;

import com.ninni.arthropoda.block.ArthropodaBlocks;
import com.ninni.arthropoda.sound.ArthropodaSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AttackWithOwnerGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.TrackOwnerAttackerGoal;
import net.minecraft.entity.ai.goal.UniversalAngerGoal;
import net.minecraft.entity.ai.goal.UntamedActiveTargetGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AntEntity extends TameableEntity implements Angerable {
    private static final TrackedData<Integer> ANGER_TIME = DataTracker.registerData(AntEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private UUID angryAt;
    @Nullable
    BlockPos anthillPos;
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    private static final TrackedData<Integer> ABDOMEN_COLOR = DataTracker.registerData(AntEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected AntEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, 1);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(MathHelper.nextInt(random, 1, 4));
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(MathHelper.nextInt(random, 1, 2));
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(MathHelper.nextDouble(random, 0.2, 0.275));
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(2, new AttackGoal(1.25F, true));
        this.goalSelector.add(3, new PounceAtTargetGoal(this, 0.4F));
        this.goalSelector.add(4, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, true));
        this.goalSelector.add(4, new TemptGoal(this, 1.25, Ingredient.ofItems(Items.SUGAR), false));
        this.goalSelector.add(5, new FindAnthillGoal(this));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 6));

        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, (new RevengeGoal(this)).setGroupRevenge());
        this.targetSelector.add(4, new OtherAntsTargetGoal(1.25F, false));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(6, new UntamedActiveTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
        this.targetSelector.add(8, new UniversalAngerGoal<>(this, true));
    }
    public static DefaultAttributeContainer.Builder createAntAttributes() {
        return createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 10)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3);
    }

    @Override
    public EntityGroup getGroup() { return EntityGroup.ARTHROPOD; }

    @Nullable
    public BlockPos getAnthillPos() { return this.anthillPos; }
    public void setAnthillPos(BlockPos pos) { this.anthillPos = pos; }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ANGER_TIME, 0);
        this.dataTracker.startTracking(ABDOMEN_COLOR, DyeColor.RED.getId());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeAngerToNbt(nbt);
        nbt.putByte("AbdomenColor", (byte) this.getAbdomenColor().getId());
        if (this.getAnthillPos() != null) {
            nbt.put("AnthillPos", NbtHelper.fromBlockPos(this.getAnthillPos()));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readAngerFromNbt(this.world, nbt);
        this.setAbdomenColor(DyeColor.byId(nbt.getInt("AbdomenColor")));
        this.setAnthillPos(null);
        if (nbt.contains("AnthillPos")) {
            this.setAnthillPos(NbtHelper.toBlockPos(nbt.getCompound("AnthillPos")));
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        if (this.world.isClient) {
            boolean bl = this.isOwner(player) || this.isTamed() || item == Items.SUGAR && !this.isTamed();
            return bl ? ActionResult.CONSUME : ActionResult.PASS;
        } else {
            if (this.isTamed()) {
                ActionResult actionResult = super.interactMob(player, hand);

                if (!(item instanceof DyeItem)) {
                    if ((!actionResult.isAccepted() || this.isBaby()) && this.isOwner(player)) {
                        this.setSitting(!this.isSitting());
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget(null);
                        return ActionResult.SUCCESS;
                    }
                    return actionResult;
                }


                DyeColor dyeColor = ((DyeItem)item).getColor();
                if (dyeColor != this.getAbdomenColor()) {
                    this.setAbdomenColor(dyeColor);
                    if (!player.getAbilities().creativeMode) { itemStack.decrement(1); }

                    return ActionResult.SUCCESS;
                }

                return actionResult;

            } else if (item == Items.SUGAR) {
                if (!this.isSilent()) this.world.playSoundFromEntity(null, this, ArthropodaSoundEvents.ENTITY_ANT_EAT, this.getSoundCategory(), 1.0F, 1.5F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                if (!player.getAbilities().creativeMode) itemStack.decrement(1);

                if (this.random.nextInt(3) == 0) {
                    this.setOwner(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.world.sendEntityStatus(this, (byte)7);
                } else this.world.sendEntityStatus(this, (byte)6);

                return ActionResult.SUCCESS;
            }

            return super.interactMob(player, hand);
        }
    }

    public DyeColor getAbdomenColor() { return DyeColor.byId(this.dataTracker.get(ABDOMEN_COLOR)); }
    public void setAbdomenColor(DyeColor color){ this.dataTracker.set(ABDOMEN_COLOR, color.getId()); }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) { return false; }
    @Override
    public boolean hurtByWater() { return true; }

    @Override
    public int getAngerTime() { return this.dataTracker.get(ANGER_TIME); }
    @Override
    public void setAngerTime(int angerTime) { this.dataTracker.set(ANGER_TIME, angerTime); }
    @Override
    public void chooseRandomAngerTime() { this.setAngerTime(ANGER_TIME_RANGE.get(this.random)); }
    @Override
    @Nullable
    public UUID getAngryAt() { return this.angryAt; }
    @Override
    public void setAngryAt(@Nullable UUID angryAt) { this.angryAt = angryAt; }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return ArthropodaSoundEvents.ENTITY_ANT_AMBIENT; }
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return ArthropodaSoundEvents.ENTITY_ANT_HURT; }
    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return ArthropodaSoundEvents.ENTITY_ANT_DEATH; }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.getMaterial().isLiquid()) {
            BlockState blockState = this.world.getBlockState(pos.up());
            BlockSoundGroup blockSoundGroup = blockState.isIn(BlockTags.INSIDE_STEP_SOUND_BLOCKS) ? blockState.getSoundGroup() : state.getSoundGroup();
            this.playSound(ArthropodaSoundEvents.ENTITY_ANT_WALK, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
        }
    }

    @Override
    protected float getSoundVolume() { return 2.0F; }
    @Override
    public float getSoundPitch() { return super.getSoundPitch() + 0.5F; }

    private class AttackGoal extends MeleeAttackGoal {

        public AttackGoal(double speed, boolean pauseWhenIdle) { super(AntEntity.this, speed, pauseWhenIdle); }

        @Override
        protected void attack(LivingEntity target, double squaredDistance) {
            if (squaredDistance <= this.getSquaredMaxAttackDistance(target) && this.isCooledDown()) {
                this.resetCooldown();
                this.mob.tryAttack(target);
                AntEntity.this.playSound(ArthropodaSoundEvents.ENTITY_ANT_ATTACK, 1.0F, 1.0F);
            }
        }

        @Override
        public boolean canStart() { return !AntEntity.this.isSitting() && !AntEntity.this.isSleeping() && !AntEntity.this.isInSneakingPose() && !AntEntity.this.isNavigating() && super.canStart(); }
    }

    private class OtherAntsTargetGoal extends ActiveTargetGoal<AntEntity> {

        @SuppressWarnings("unused")
        public OtherAntsTargetGoal(double speed, boolean pauseWhenIdle) { super(AntEntity.this, AntEntity.class, false); }

        @Override
        public void start() { if (this.mob instanceof AntEntity ant && this.targetEntity instanceof AntEntity antTarget && antTarget.getAbdomenColor() != ant.getAbdomenColor() && ant.isTamed() && antTarget.isTamed()) super.start(); }
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) { return null; }

    @SuppressWarnings("unused")
    public static boolean canSpawn(EntityType <AntEntity> entity, ServerWorldAccess world, SpawnReason reason, BlockPos pos, Random random){
        BlockState state = world.getBlockState(pos.down());
        return state.isOf(Blocks.GRASS_BLOCK) && world.getBaseLightLevel(pos, 0) > 8;
    }

    private static class FindAnthillGoal extends Goal {
        private final AntEntity ant;
        private int findingTicks;
        private BlockPos anthillPos;

        public FindAnthillGoal(AntEntity ant) {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
            this.ant = ant;
        }

        @Override
        public boolean canStart() {
            this.anthillPos = this.getNearestAnthills();
            return this.ant.getAnthillPos() == null && this.anthillPos != null;
        }

        @Override
        public boolean shouldContinue() { return this.anthillPos != null; }

        @Override
        public void start() { this.findingTicks = 1200; }

        @Override
        public void tick() {
            if (this.findingTicks > 0) {
                this.findingTicks--;
                this.ant.getNavigation().startMovingTo(this.anthillPos.getX(), this.anthillPos.getY(), this.anthillPos.getZ(), 1.0F);
                if (this.ant.getBlockPos().isWithinDistance(this.anthillPos, 5.0D)) this.ant.setAnthillPos(this.anthillPos);
            }
        }

        public BlockPos getNearestAnthills() {
            List<BlockPos> list = Lists.newArrayList();
            int radius = 4;
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = new BlockPos(this.ant.getBlockPos().getX() + x, this.ant.getBlockPos().getY(), this.ant.getBlockPos().getZ() + z);
                    if (this.ant.world.getBlockState(pos).isOf(ArthropodaBlocks.ANTHILL)) list.add(pos);
                }
            }
            if (list.isEmpty()) return null;

            return list.get(0);
        }

    }
}
