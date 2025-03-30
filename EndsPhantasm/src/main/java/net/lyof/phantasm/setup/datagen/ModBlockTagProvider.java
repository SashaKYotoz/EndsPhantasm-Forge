package net.lyof.phantasm.setup.datagen;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Phantasm.MOD_ID,existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Blocks.END_PLANTS_GROWABLE_ON).add(
                Blocks.END_STONE,
                Blocks.END_STONE_BRICKS,
                Blocks.END_STONE_BRICK_STAIRS,
                Blocks.END_STONE_BRICK_SLAB
        );
    }
}
