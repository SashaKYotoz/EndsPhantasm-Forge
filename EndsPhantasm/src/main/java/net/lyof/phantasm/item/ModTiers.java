package net.lyof.phantasm.item;

import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModTiers implements Tier {

    CRYSTALLINE(2, 312, 8f, 1f, 17,()->
            Ingredient.of(ModBlocks.CRYSTAL_SHARD.get(), ModBlocks.VOID_CRYSTAL_SHARD.get())),
    STELLIUM(4, 1014, 8f, 5f, 13,()-> Ingredient.EMPTY);

    private final int durability;
    private final float speed;
    private final float damage;
    private final int mining_level;
    private final int enchantability;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModTiers(int mining_level, int durability, float speed, float damage, int enchantability, Supplier<Ingredient> repair) {
        this.durability = durability;
        this.speed = speed;
        this.damage = damage;
        this.mining_level = mining_level;
        this.enchantability = enchantability;
        this.repairIngredient = new LazyLoadedValue<>(repair);
    }

    @Override
    public int getUses() {
        return this.durability;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public int getLevel() {
        return this.mining_level;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
