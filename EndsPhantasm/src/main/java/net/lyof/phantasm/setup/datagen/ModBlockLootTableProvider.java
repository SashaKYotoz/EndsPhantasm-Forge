package net.lyof.phantasm.setup.datagen;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.custom.HangingFruitBlock;
import net.lyof.phantasm.block.custom.HangingPlantBlock;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }


    @Override
    public void generate() {
        ModBlocks.BLOCKS.getEntries().stream().filter(
                blockRegistryObject -> !blockRegistryObject.get().defaultBlockState().is(ModBlocks.PREAM_LEAVES.get()) &&
                        !blockRegistryObject.get().defaultBlockState().is(ModBlocks.HANGING_PREAM_LEAVES.get()) &&
                        !blockRegistryObject.get().defaultBlockState().is(ModBlocks.VIVID_NIHILIUM.get()) &&
                        !blockRegistryObject.get().defaultBlockState().is(ModBlocks.TALL_VIVID_NIHILIS.get()) &&
                        !blockRegistryObject.get().defaultBlockState().is(ModBlocks.VIVID_NIHILIS.get())
        ).forEach(blockRegistryObject -> dropSelf(blockRegistryObject.get()));

        add(ModBlocks.PREAM_LEAVES.get(), createLeavesDrops(ModBlocks.PREAM_LEAVES.get(), ModBlocks.PREAM_SAPLING.get(), 0.1f));
        add(ModBlocks.HANGING_PREAM_LEAVES.get(), block -> createSilkTouchOrShearsDispatchTable(block, this.applyExplosionCondition(block, LootItem.lootTableItem(ModItems.PREAM_BERRY.get()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HangingFruitBlock.HAS_FRUIT, true))))));

        otherWhenSilkTouch(Blocks.END_STONE, ModBlocks.VIVID_NIHILIUM.get());

        add(ModBlocks.VIVID_NIHILIS.get(), block -> createShearsOnlyDrop(ModBlocks.VIVID_NIHILIS.get()));
        add(ModBlocks.TALL_VIVID_NIHILIS.get(), block -> createShearsOnlyDrop(ModBlocks.TALL_VIVID_NIHILIS.get()));
    }
}
