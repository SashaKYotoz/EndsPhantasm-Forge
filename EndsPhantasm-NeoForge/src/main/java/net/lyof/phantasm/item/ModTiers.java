package net.lyof.phantasm.item;

import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.SimpleTier;

import java.util.function.Supplier;

public class ModTiers {

    public static final Tier CRYSTALLINE = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 312, 8f, 1f, 17, () ->
            Ingredient.of(ModBlocks.CRYSTAL_SHARD.get(), ModBlocks.VOID_CRYSTAL_SHARD.get()));
    public static final Tier STELLIUM = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 1014, 8f, 5f, 13, () -> Ingredient.EMPTY);
}
