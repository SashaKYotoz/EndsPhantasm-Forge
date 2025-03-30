package net.lyof.phantasm.setup;

import net.lyof.phantasm.Phantasm;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> XP_BOOSTED = tag("gets_xp_speed_boost");
        public static final TagKey<Item> PREAM_LOGS = tag("pream_logs");
        public static final TagKey<Item> CRYSTAL_FLOWERS = tag("crystal_flowers");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(Phantasm.makeID( name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> PREAM_BLOCKS = tag("pream_blocks");
        public static final TagKey<Block> HANGING_PREAM_LEAVES_GROWABLE_ON = tag("hanging_pream_leaves_growable_on");
        public static final TagKey<Block> END_PLANTS = tag("end_plants");
        public static final TagKey<Block> END_PLANTS_GROWABLE_ON = tag("end_plants_growable_on");

        public static final TagKey<Block> OBLIVINE_GROWABLE_ON = tag("oblivine_growable_on");
        public static final TagKey<Block> DRALGAE_GROWABLE_ON = tag("dralgae_growable_on");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(Phantasm.makeID( name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> DREAMING_DEN = create("is_dreaming_den");
        public static final TagKey<Biome> ACIDBURNT_ABYSSES = create("is_acidburnt_abysses");

        private static TagKey<Biome> create(String s) {
            return TagKey.create(Registries.BIOME, Phantasm.makeID(s));
        }
    }
}
