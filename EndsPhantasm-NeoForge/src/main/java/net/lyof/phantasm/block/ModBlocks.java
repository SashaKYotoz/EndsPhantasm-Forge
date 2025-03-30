package net.lyof.phantasm.block;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.custom.*;
import net.lyof.phantasm.block.custom.DirectionalBlock;
import net.lyof.phantasm.block.custom.signs.ModHangingSignBlock;
import net.lyof.phantasm.block.custom.signs.ModStandingSignBlock;
import net.lyof.phantasm.block.custom.signs.ModWallHangingSignBlock;
import net.lyof.phantasm.block.custom.signs.ModWallSignBlock;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.tree.TreeGenerators;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Phantasm.MOD_ID);
    private static final BlockBehaviour.Properties CrystalMaterial =
            BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE).lightLevel((blockState) -> 4).emissiveRendering((a, b, c) -> true).noOcclusion();
    private static final BlockBehaviour.Properties CrystalGlassMaterial =
            BlockBehaviour.Properties.of().emissiveRendering((a, b, c) -> true).strength(0.75f).lightLevel((blockState) -> 4)
                    .noOcclusion().mapColor(MapColor.COLOR_LIGHT_BLUE).sound(SoundType.GLASS);

    private static final BlockBehaviour.Properties PolishedObsidianMaterial =
            BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).strength(7);

    private static final BlockBehaviour.Properties PreamWoodMaterial =
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(MapColor.COLOR_BROWN);
    private static final BlockBehaviour.Properties PreamPlankMaterial =
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(MapColor.TERRACOTTA_YELLOW);
    private static final BlockBehaviour.Properties PreamPassableMaterial =
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).mapColor(MapColor.TERRACOTTA_YELLOW);
    private static final BlockBehaviour.Properties PreamLeafMaterial =
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(MapColor.TERRACOTTA_PURPLE);

    private static final BlockBehaviour.Properties RawPurpurMaterial =
            BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE).mapColor(MapColor.TERRACOTTA_PURPLE);
    private static final BlockBehaviour.Properties OblivionMaterial = BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK).mapColor(MapColor.COLOR_BLACK);
    private static final BlockBehaviour.Properties acidicMassMaterial = BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK).mapColor(MapColor.WARPED_HYPHAE).strength(1.8f);
    private static final BlockBehaviour.Properties pomeMaterial = BlockBehaviour.Properties.ofFullCopy(Blocks.MELON).mapColor(MapColor.WARPED_HYPHAE).strength(1.5f);
    private static final BlockBehaviour.Properties ciriteMaterial = BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE).mapColor(MapColor.WARPED_HYPHAE).strength(2f, 0.2f).friction(1);
    public static List<DeferredBlock> BLOCK_CUTOUT = new ArrayList<>();

    public static final WoodType PREAM = new WoodType("pream", BlockSetType.OAK);
    public static final DeferredBlock<Block> FALLEN_STAR = registerBlock("fallen_star", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).mapColor(MapColor.COLOR_LIGHT_BLUE).lightLevel((blockState) -> 15)));
    public static final DeferredBlock<Block> POLISHED_OBSIDIAN = registerBlock("polished_obsidian", () -> new Block(PolishedObsidianMaterial));
    public static final DeferredBlock<Block> POLISHED_OBSIDIAN_BRICKS = registerBlock("polished_obsidian_bricks", () -> new Block(PolishedObsidianMaterial));
    public static final DeferredBlock<Block> POLISHED_OBSIDIAN_BRICKS_STAIRS = registerBlock("polished_obsidian_bricks_stairs", () -> new StairBlock(POLISHED_OBSIDIAN_BRICKS.get().defaultBlockState(), PolishedObsidianMaterial), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> POLISHED_OBSIDIAN_BRICKS_SLAB = registerBlock("polished_obsidian_bricks_slab", () -> new SlabBlock(PolishedObsidianMaterial), BLOCK_CUTOUT);
    // Crystal Blockset
    public static final DeferredBlock<Block> CRYSTAL_SHARD = registerBlock("crystal_shard", () -> new CrystalShardBlock(CrystalMaterial.lightLevel(((b) -> 7)).sound(SoundType.GLASS)), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> VOID_CRYSTAL_SHARD = registerBlock("void_crystal_shard", () -> new CrystalShardBlock(CrystalMaterial), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> CRYSTAL_BLOCK = registerBlock("crystal_block", () -> new Block(CrystalMaterial));
    public static final DeferredBlock<Block> CRYSTAL_TILES = registerBlock("crystal_tiles", () -> new Block(CrystalMaterial));
    public static final DeferredBlock<Block> CRYSTAL_TILES_STAIRS = registerBlock("crystal_tiles_stairs", () -> new StairBlock(CRYSTAL_TILES.get().defaultBlockState(), CrystalMaterial), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> CRYSTAL_TILES_SLAB = registerBlock("crystal_tiles_slab", () -> new SlabBlock(CrystalMaterial), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> CRYSTAL_PILLAR = registerBlock("crystal_pillar", () -> new RotatedPillarBlock(CrystalMaterial));
    public static final DeferredBlock<Block> CRYSTAL_GLASS = registerBlock("crystal_glass", () -> new Block(CrystalGlassMaterial.noOcclusion()), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> CRYSTAL_GLASS_PANE = registerBlock("crystal_glass_pane", () -> new StainedGlassPaneBlock(DyeColor.BLUE, CrystalGlassMaterial.noOcclusion()), BLOCK_CUTOUT);
    // Void Crystal Blockset
    public static final DeferredBlock<Block> VOID_CRYSTAL_BLOCK = registerBlock("void_crystal_block", () -> new Block(CrystalMaterial));
    public static final DeferredBlock<Block> VOID_CRYSTAL_TILES = registerBlock("void_crystal_tiles", () -> new Block(CrystalMaterial));
    public static final DeferredBlock<Block> VOID_CRYSTAL_TILES_STAIRS = registerBlock("void_crystal_tiles_stairs", () -> new StairBlock(VOID_CRYSTAL_TILES.get().defaultBlockState(), CrystalGlassMaterial), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> VOID_CRYSTAL_TILES_SLAB = registerBlock("void_crystal_tiles_slab", () -> new SlabBlock(CrystalMaterial), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> VOID_CRYSTAL_PILLAR = registerBlock("void_crystal_pillar", () -> new RotatedPillarBlock(CrystalGlassMaterial));
    public static final DeferredBlock<Block> VOID_CRYSTAL_GLASS = registerBlock("void_crystal_glass", () -> new Block(CrystalGlassMaterial.noOcclusion()), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> VOID_CRYSTAL_GLASS_PANE = registerBlock("void_crystal_glass_pane", () -> new StainedGlassPaneBlock(DyeColor.BLUE, CrystalGlassMaterial.noOcclusion()), BLOCK_CUTOUT);
    // Pream Blockset
    public static final DeferredBlock<Block> STRIPPED_PREAM_LOG = log("stripped_pream_log", MapColor.COLOR_LIGHT_BLUE, MapColor.COLOR_BLUE, PreamWoodMaterial);
    public static final DeferredBlock<Block> PREAM_LOG = log("pream_log", MapColor.COLOR_LIGHT_BLUE, MapColor.COLOR_BLUE, PreamWoodMaterial);
    public static final DeferredBlock<Block> STRIPPED_PREAM_WOOD = log("stripped_pream_wood", MapColor.COLOR_LIGHT_BLUE, MapColor.COLOR_BLUE, PreamWoodMaterial);
    public static final DeferredBlock<Block> PREAM_WOOD = log("pream_wood", MapColor.COLOR_LIGHT_BLUE, MapColor.COLOR_BLUE, PreamWoodMaterial);
    public static final DeferredBlock<Block> PREAM_LEAVES = registerBlock("pream_leaves", () -> new LeavesBlock(PreamLeafMaterial), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> HANGING_PREAM_LEAVES = registerBlock("hanging_pream_leaves", () -> new HangingFruitBlock(PreamLeafMaterial.noCollission(),
            ModTags.Blocks.HANGING_PREAM_LEAVES_GROWABLE_ON, Block.box(3, 3, 3, 13, 16, 13), "pream_berry"), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> PREAM_PLANKS = registerBlock("pream_planks", () -> new Block(PreamPlankMaterial));
    public static final DeferredBlock<Block> PREAM_STAIRS = registerBlock("pream_stairs", () -> new StairBlock(PREAM_PLANKS.get().defaultBlockState(), PreamPlankMaterial));
    public static final DeferredBlock<Block> PREAM_SLAB = registerBlock("pream_slab", () -> new SlabBlock(PreamPlankMaterial));
    public static final DeferredBlock<Block> PREAM_PRESSURE_PLATE = registerBlock("pream_pressure_plate", () -> new PressurePlateBlock(PREAM.setType(), PreamPassableMaterial));
    public static final DeferredBlock<Block> PREAM_BUTTON = registerBlock("pream_button", () -> new ButtonBlock(PREAM.setType(), 50, PreamPassableMaterial));
    public static final DeferredBlock<Block> PREAM_FENCE = registerBlock("pream_fence", () -> new FenceBlock(PreamPlankMaterial));
    public static final DeferredBlock<Block> PREAM_FENCE_GATE = registerBlock("pream_fence_gate", () -> new FenceGateBlock(PREAM, PreamPlankMaterial));
    public static final DeferredBlock<Block> PREAM_DOOR = registerBlock("pream_door", () -> new DoorBlock(PREAM.setType(), PreamPlankMaterial.noOcclusion()), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> PREAM_TRAPDOOR = registerBlock("pream_trapdoor", () -> new TrapDoorBlock(PREAM.setType(), PreamPlankMaterial.noOcclusion()), BLOCK_CUTOUT);
    public static final DeferredHolder<Block, ModStandingSignBlock> PREAM_SIGN = BLOCKS.register("pream_sign", () -> new ModStandingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN), WoodType.OAK));
    public static final DeferredHolder<Block, ModWallSignBlock> PREAM_WALL_SIGN = BLOCKS.register("pream_wall_sign", () -> new ModWallSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN), WoodType.OAK));
    public static final DeferredHolder<Block, ModHangingSignBlock> PREAM_HANGING_SIGN = BLOCKS.register("pream_hanging_sign", () -> new ModHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN), WoodType.OAK));
    public static final DeferredHolder<Block, ModWallHangingSignBlock> PREAM_WALL_HANGING_SIGN = BLOCKS.register("pream_wall_hanging_sign", () -> new ModWallHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN), WoodType.OAK));
    public static final DeferredBlock<Block> PREAM_SAPLING = registerBlock("pream_sapling", () -> new SaplingBlock(TreeGenerators.PREAM, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)), BLOCK_CUTOUT);
    //Oblivion
    public static final DeferredBlock<Block> OBLIVINE = registerBlock("oblivine", () -> new HangingFruitBlock(OblivionMaterial.instabreak().noCollission(), ModTags.Blocks.OBLIVINE_GROWABLE_ON, Block.box(2, 0, 2, 14, 16, 14), "oblifruit"), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> OBLIVION = registerBlock("oblivion", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).mapColor(MapColor.COLOR_BLACK)));
    public static final DeferredBlock<Block> CRYSTALILY = registerBlock("crystalily", () -> new HangingPlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)
            .mapColor(MapColor.COLOR_LIGHT_BLUE).instabreak().lightLevel((blockState) -> 7).emissiveRendering((bs, br, bp) -> true), ModTags.Blocks.OBLIVINE_GROWABLE_ON, Block.box(0, 8, 0, 16, 16, 16)), BLOCK_CUTOUT);
    // Vivid Nihilium
    public static final DeferredBlock<Block> VIVID_NIHILIUM = registerBlock("vivid_nihilium", () -> new NihiliumBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE).mapColor(MapColor.COLOR_CYAN).randomTicks()), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> VIVID_NIHILIS = registerBlock("vivid_nihilis", () -> new VividNihilisBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_ROOTS).mapColor(MapColor.COLOR_CYAN)) {
        @Override
        public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
            return reader.getBlockState(pos.below()).is(BlockTags.DIRT) || reader.getBlockState(pos.below()).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        }
    }, BLOCK_CUTOUT);
    public static final DeferredBlock<Block> STARFLOWER = registerBlock("starflower", () -> new FlowerBlock(MobEffects.NIGHT_VISION, 100, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_ROOTS).mapColor(MapColor.COLOR_LIGHT_BLUE).instabreak().noCollission().lightLevel((state) -> 5).emissiveRendering((a, b, c) -> true)) {
        @Override
        public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
            return reader.getBlockState(pos.below()).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        }
    }, BLOCK_CUTOUT);
    public static final DeferredHolder<Block, DoublePlantBlock> TALL_VIVID_NIHILIS = registerOnlyBlock("tall_vivid_nihilis", () -> new DoublePlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FERN).mapColor(MapColor.COLOR_CYAN)) {
        @Override
        public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
            return state.getValue(DoublePlantBlock.HALF) != DoubleBlockHalf.LOWER || reader.getBlockState(pos.below()).is(BlockTags.DIRT) || reader.getBlockState(pos.below()).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        }
    }, BLOCK_CUTOUT);

    // Acidic Nihilium
    public static final DeferredBlock<NihiliumBlock> ACIDIC_NIHILIUM = registerBlock("acidic_nihilium", () -> new NihiliumBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE).mapColor(MapColor.WARPED_NYLIUM).randomTicks()), BLOCK_CUTOUT);
    public static final DeferredBlock<VividNihilisBlock> ACIDIC_NIHILIS = registerBlock("acidic_nihilis", () -> new VividNihilisBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_ROOTS).mapColor(MapColor.WARPED_NYLIUM)) {
        @Override
        public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
            return reader.getBlockState(pos.below()).is(BlockTags.DIRT) || reader.getBlockState(pos.below()).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        }
    }, BLOCK_CUTOUT);
    public static final DeferredBlock<DoublePlantBlock> TALL_ACIDIC_NIHILIS = registerOnlyBlock("tall_acidic_nihilis", () -> new DoublePlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FERN).mapColor(MapColor.COLOR_CYAN)) {
        @Override
        public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
            return state.getValue(DoublePlantBlock.HALF) != DoubleBlockHalf.LOWER || reader.getBlockState(pos.below()).is(BlockTags.DIRT) || reader.getBlockState(pos.below()).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        }
    }, BLOCK_CUTOUT);
    public static final DeferredBlock<DragonMintBlock> DRAGON_MINT = registerBlock("dragon_mint", () -> new DragonMintBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_ROOTS).randomTicks()
            .mapColor(MapColor.CRIMSON_STEM).sound(SoundType.CHERRY_LEAVES).offsetType(BlockBehaviour.OffsetType.NONE).lightLevel(state -> state.getValue(HangingFruitBlock.HAS_FRUIT) ? 9 : 0)), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> ACIDIC_MASS = registerBlock("acidic_mass", () -> new Block(acidicMassMaterial) {
        @Override
        public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
            if (!entity.isSteppingCarefully()) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.4, 1, 0.4));
                if (entity instanceof LivingEntity living)
                    living.addEffect(new MobEffectInstance(ModEffects.CORROSION.getDelegate(), 100, 0));
            }
            super.stepOn(level, pos, state, entity);
        }
    });
    public static final DeferredBlock<Block> DRALGAE = registerBlock("dralgae", () -> new PillaringPlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_ROOTS)
            .mapColor(MapColor.CRIMSON_HYPHAE).offsetType(BlockBehaviour.OffsetType.NONE),
            ModTags.Blocks.DRALGAE_GROWABLE_ON,
            Block.box(5, 0, 5, 11, 16, 11)), BLOCK_CUTOUT);
    public static final DeferredBlock<Block> POME = registerBlock("pome", () -> new PomeBlock(pomeMaterial));
    // Raw Purpur
    public static final DeferredBlock<Block> RAW_PURPUR = registerBlock("raw_purpur", () -> new Block(RawPurpurMaterial));
    public static final DeferredBlock<Block> RAW_PURPUR_BRICKS = registerBlock("raw_purpur_bricks", () -> new Block(RawPurpurMaterial));
    public static final DeferredBlock<Block> RAW_PURPUR_BRICKS_STAIRS = registerBlock("raw_purpur_bricks_stairs", () -> new StairBlock(RAW_PURPUR_BRICKS.get().defaultBlockState(), RawPurpurMaterial));
    public static final DeferredBlock<Block> RAW_PURPUR_BRICKS_SLAB = registerBlock("raw_purpur_bricks_slab", () -> new SlabBlock(RawPurpurMaterial));
    public static final DeferredBlock<Block> RAW_PURPUR_TILES = registerBlock("raw_purpur_tiles", () -> new Block(RawPurpurMaterial));
    public static final DeferredBlock<Block> RAW_PURPUR_PILLAR = registerBlock("raw_purpur_pillar", () -> new RotatedPillarBlock(RawPurpurMaterial));
    public static final DeferredBlock<Block> PURPUR_LAMP = registerBlock("purpur_lamp", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_BLOCK).lightLevel((block) -> 15)));
    // Cirite
    public static final DeferredBlock<Block> CIRITE = registerBlock("cirite", () -> new Block(ciriteMaterial));
    public static final DeferredBlock<Block> CIRITE_IRON_ORE = registerBlock("cirite_iron_ore", () -> new Block(ciriteMaterial));
    public static final DeferredBlock<Block> CIRITE_BRICKS = registerBlock("cirite_bricks", () -> new Block(ciriteMaterial));
    public static final DeferredBlock<StairBlock> CIRITE_BRICKS_STAIRS = registerBlock("cirite_bricks_stairs", () -> new StairBlock(CIRITE_BRICKS.get().defaultBlockState(), ciriteMaterial));
    public static final DeferredBlock<SlabBlock> CIRITE_BRICKS_SLAB = registerBlock("cirite_bricks_slab", () -> new SlabBlock(ciriteMaterial));
    // Choral
    public static final DeferredBlock<Block> CHORAL_BLOCK = registerBlock("choral_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BRAIN_CORAL_BLOCK).mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final DeferredBlock<DirectionalBlock> CHORAL_FAN = registerBlock("choral_fan", () -> new DirectionalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRAIN_CORAL_FAN).mapColor(MapColor.TERRACOTTA_WHITE)), BLOCK_CUTOUT);
    public static final DeferredBlock<SubwooferBlock> SUBWOOFER_BLOCK = registerBlock("subwoofer_block", () -> new SubwooferBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NOTE_BLOCK).mapColor(MapColor.TERRACOTTA_WHITE)));

    private static DeferredBlock<Block> log(String name, MapColor color, MapColor color1, BlockBehaviour.Properties properties) {
        return registerBlock(name, () -> new ModRotatedPillarBlock(properties.mapColor((state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? color : color1)));
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block, List<DeferredBlock> addTo) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        addTo.add(toReturn);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerOnlyBlock(String name, Supplier<T> block, List<DeferredBlock> addTo) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        addTo.add(toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredHolder<Item, BlockItem> registerBlockItem(String name, DeferredBlock<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}