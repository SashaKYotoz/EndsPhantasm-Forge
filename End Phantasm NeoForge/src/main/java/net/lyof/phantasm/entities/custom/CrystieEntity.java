package net.lyof.phantasm.entities.custom;

import it.unimi.dsi.fastutil.ints.IntList;
import net.lyof.phantasm.entities.ModEntities;
import net.lyof.phantasm.entities.goals.CrystieAirMovementGoal;
import net.lyof.phantasm.entities.goals.DiveBombGoal;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
        Fireworks fireworks = new Fireworks(-2,
                List.of(new FireworkExplosion(
                        FireworkExplosion.Shape.BURST,
                        IntList.of(2651799),
                        IntList.of(11250603),
                        true,
                        true
                )));
        stack.set(DataComponents.FIREWORKS, fireworks);
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

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 2);
    }
}