package com.ninni.arthropoda.entity;

import com.ninni.arthropoda.sound.ArthropodaSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AttackWithOwnerGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
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
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class AntEntity extends TameableEntity implements Angerable {
    private static final TrackedData<Integer> ANGER_TIME = DataTracker.registerData(AntEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private UUID angryAt;
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    private static final TrackedData<Integer> ABDOMEN_COLOR = DataTracker.registerData(AntEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected AntEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, 1);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(3, new AttackGoal(1.25F, true));
        this.goalSelector.add(2, new PounceAtTargetGoal(this, 0.4F));
        this.goalSelector.add(4, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, true));
        this.goalSelector.add(4, new TemptGoal(this, 1.25, Ingredient.ofItems(Items.SUGAR), false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6));

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
            .add(EntityAttributes.GENERIC_MAX_HEALTH, getRandomHealth())
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, getRandomMovementSpeed())
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, getRandomAttackDamage());
    }
    protected static float getRandomHealth() { return 2.0F; }
    protected static double getRandomMovementSpeed() { return 0.2; }
    protected static double getRandomAttackDamage() { return 1; }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

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
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readAngerFromNbt(this.world, nbt);
        this.setAbdomenColor(DyeColor.byId(nbt.getInt("AbdomenColor")));
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        if (this.world.isClient) {
            boolean bl = this.isOwner(player) || this.isTamed() || item == Items.SUGAR && !this.isTamed();
            return bl ? ActionResult.CONSUME : ActionResult.PASS;
        } else {
            if (this.isTamed()) {
                if (this.isHealingItem(itemStack) && this.getHealth() < this.getMaxHealth() ) {
                    if (!this.isSilent()) {
                        this.world.playSoundFromEntity(null, this, SoundEvents.ENTITY_GENERIC_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                    }
                    if (!player.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }

                    this.heal((float)item.getFoodComponent().getHunger());
                    return ActionResult.SUCCESS;
                }
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
                    if (!player.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }

                    return ActionResult.SUCCESS;
                }

                return actionResult;


            } else if (item == Items.SUGAR) {
                if (!this.isSilent()) {
                    this.world.playSoundFromEntity(null, this, ArthropodaSoundEvents.ENTITY_ANT_EAT, this.getSoundCategory(), 1.0F, 1.5F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                }
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }

                if (this.random.nextInt(3) == 0) {
                    this.setOwner(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setSitting(true);
                    this.world.sendEntityStatus(this, (byte)7);
                } else {
                    this.world.sendEntityStatus(this, (byte)6);
                }

                return ActionResult.SUCCESS;
            }

            return super.interactMob(player, hand);
        }
    }

    public DyeColor getAbdomenColor() {
        return DyeColor.byId(this.dataTracker.get(ABDOMEN_COLOR));
    }

    public void setAbdomenColor(DyeColor color){
        this.dataTracker.set(ABDOMEN_COLOR, color.getId());
    }

    public boolean isHealingItem (ItemStack stack){
        Item item = stack.getItem();
        return item.isFood() && !Objects.requireNonNull(item.getFoodComponent()).isMeat();
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) { return false; }
    @Override
    public boolean hurtByWater() { return true; }

    @Override
    public int getAngerTime() {
        return this.dataTracker.get(ANGER_TIME);
    }
    @Override
    public void setAngerTime(int angerTime) {
        this.dataTracker.set(ANGER_TIME, angerTime);
    }
    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }
    @Override
    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }
    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

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
            double d = this.getSquaredMaxAttackDistance(target);
            if (squaredDistance <= d && this.isCooledDown()) {
                this.resetCooldown();
                this.mob.tryAttack(target);
                AntEntity.this.playSound(ArthropodaSoundEvents.ENTITY_ANT_ATTACK, 1.0F, 1.0F);
            }
        }

        @Override
        public boolean canStart() {
            return !AntEntity.this.isSitting() && !AntEntity.this.isSleeping() && !AntEntity.this.isInSneakingPose() && !AntEntity.this.isNavigating() && super.canStart();
        }
    }

    private class OtherAntsTargetGoal extends ActiveTargetGoal<AntEntity> {

        public OtherAntsTargetGoal(double speed, boolean pauseWhenIdle) { super(AntEntity.this, AntEntity.class, false); }

        @Override
        public void start() {
            if (this.mob instanceof AntEntity ant && this.targetEntity instanceof AntEntity antTarget && antTarget.getAbdomenColor() != ant.getAbdomenColor()) super.start();
        }
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) { return null; }

    @SuppressWarnings("unused")
    public static boolean canSpawn(EntityType <AntEntity> entity, ServerWorldAccess world, SpawnReason reason, BlockPos pos, Random random){
        BlockState state = world.getBlockState(pos.down());
        return state.isOf(Blocks.GRASS_BLOCK) && world.getBaseLightLevel(pos, 0) > 8;
    }
}
