package net.lyof.phantasm.entities.custom;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.entities.ModEntities;
import net.lyof.phantasm.entities.goals.CrystieAirMovementGoal;
import net.lyof.phantasm.entities.goals.DiveBombGoal;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class CrystieEntity extends Animal {
    public boolean isAngry = false;
    public static ItemStack FIREWORK = getFirework();

    public CrystieEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 100, true);
        this.setNoGravity(true);
    }

    @Override
    public boolean causeFallDamage(float l, float d, DamageSource source) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return ModEntities.CRYSTIE.get().create(level);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CrystieAirMovementGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomFlyingGoal(this, 2));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, true) {
            @Override
            public boolean canUse() {
                return super.canUse() && CrystieEntity.this.isAngry;
            }
        });
        this.goalSelector.addGoal(3, new TemptGoal(this, 1, Ingredient.of(ModTags.Items.CRYSTAL_FLOWERS), false));
        this.goalSelector.addGoal(4, new DiveBombGoal(this));
        //this.goalSelector.add(4, new AvoidGroundGoal(this));
    }

    public static ItemStack getFirework() {
        ItemStack stack = Items.FIREWORK_ROCKET.getDefaultInstance();
        CompoundTag fireworkTag = new CompoundTag();
        CompoundTag fireworksTag = new CompoundTag();
        int[] colors = new int[]{2651799};
        ListTag explosionsTag = new ListTag();
        CompoundTag explosionTag = new CompoundTag();
        explosionTag.putInt("Type", 1);
        explosionTag.putIntArray("Colors", colors);
        explosionTag.putIntArray("FadeColors", new int[]{11250603});
        explosionTag.putBoolean("Flicker", true);
        explosionTag.putBoolean("Trail", true);
        explosionsTag.add(explosionTag);
        fireworksTag.put("Explosions", explosionsTag);
        fireworksTag.putInt("Flight", -2);
        fireworkTag.put("Fireworks", fireworksTag);
        stack.setTag(fireworkTag);
        return stack;
    }

    public void explode() {
        FireworkRocketEntity firework = new FireworkRocketEntity(this.level(), FIREWORK, this);
        this.level().addFreshEntity(firework);

        if (!this.isDeadOrDying()) this.discard();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player)
            this.isAngry = true;
        return super.hurt(source, amount);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor accessor, MobSpawnType type) {
        return accessor.getBlockState(this.getOnPos().below()).isAir();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.AXOLOTL_ATTACK;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.AMETHYST_BLOCK_BREAK;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        this.explode();
    }

    public static boolean checkCrystieSpawnRules(EntityType<? extends Animal> type, LevelAccessor accessor, MobSpawnType mobSpawnType, BlockPos pos, RandomSource source) {
        return accessor.getBlockState(pos.below()).isAir() && pos.getY() > 72;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ModTags.Items.CRYSTAL_FLOWERS);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult result = super.mobInteract(player, hand);

        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(ModBlocks.VOID_CRYSTAL_GLASS.get().asItem())) {
            this.discard();
            stack.shrink(1);
            player.addItem(Items.END_CRYSTAL.getDefaultInstance());
            result = InteractionResult.SUCCESS;
        }
        return result;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 2);
    }
}