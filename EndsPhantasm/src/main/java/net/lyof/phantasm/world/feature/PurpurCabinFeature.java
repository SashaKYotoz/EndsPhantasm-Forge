package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.utils.BlockHelper;
import net.lyof.phantasm.world.feature.config.PurpurCabinFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class PurpurCabinFeature extends Feature<PurpurCabinFeatureConfig> {
    public static final Feature<PurpurCabinFeatureConfig> INSTANCE = new PurpurCabinFeature(PurpurCabinFeatureConfig.CODEC);

    public PurpurCabinFeature(Codec<PurpurCabinFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PurpurCabinFeatureConfig> context) {
        RandomSource random = context.random();
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();

        int width = context.config().width().sample(random) / 2;
        int length = context.config().length().sample(random) / 2;

        if (world.getBlockState(origin.atY(38)).is(ModBlocks.RAW_PURPUR.get())) origin = origin.atY(38);
        else if (world.getBlockState(origin.atY(30)).is(ModBlocks.RAW_PURPUR.get())) origin = origin.atY(30);
        else if (world.getBlockState(origin.atY(22)).is(ModBlocks.RAW_PURPUR.get())) origin = origin.atY(22);
        else return false;

        origin.offset(-(origin.getX() % 16), 0, -(origin.getZ() % 16));

        for (int sx = -width; sx <= width; sx ++) {
            for (int sz = -length; sz <= length; sz ++) {
                BlockHelper.placeColumn(world, origin.east(sx).north(sz), 2, Blocks.AIR.defaultBlockState());
            }
        }

        decorate(world, origin, width, length);
        return true;
    }

    static void decorate(WorldGenLevel level, BlockPos origin, int width, int length) {
        BiPredicate<BlockGetter, BlockPos> condition = (a, b) -> a.getBlockState(b.below()).is(ModBlocks.RAW_PURPUR.get())
                || a.getBlockState(b.below(2)).is(ModBlocks.RAW_PURPUR.get());

        BlockHelper.placeColumn(level, origin.east(-width).north(-length), 2, ModBlocks.RAW_PURPUR_PILLAR.get().defaultBlockState(),
                condition);
        BlockHelper.placeColumn(level, origin.east(width).north(-length), 2, ModBlocks.RAW_PURPUR_PILLAR.get().defaultBlockState(),
                condition);
        BlockHelper.placeColumn(level, origin.east(-width).north(length), 2, ModBlocks.RAW_PURPUR_PILLAR.get().defaultBlockState(),
                condition);
        BlockHelper.placeColumn(level, origin.east(width).north(length), 2, ModBlocks.RAW_PURPUR_PILLAR.get().defaultBlockState(),
                condition);


        List<BlockPos> positions = new ArrayList<>();
        for (int i = 1-width; i < width; i++) {
            positions.add(origin.north(length).east(i));
            positions.add(origin.north(-length).east(i));
        }
        for (int i = 1-length; i < length; i++) {
            positions.add(origin.east(width).north(i));
            positions.add(origin.east(-width).north(i));
        }

        List<BlockState> states = new ArrayList<>(List.of(
                Blocks.CRAFTING_TABLE.defaultBlockState(),
                Blocks.STONECUTTER.defaultBlockState(),
                Blocks.FURNACE.defaultBlockState(),
                Blocks.FURNACE.defaultBlockState().rotate(Rotation.CLOCKWISE_90),
                Blocks.FURNACE.defaultBlockState().rotate(Rotation.CLOCKWISE_180),
                Blocks.FURNACE.defaultBlockState().rotate(Rotation.COUNTERCLOCKWISE_90),
                Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, Direction.UP),
                Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, Direction.UP)
        ));

        RandomSource rnd = RandomSource.create();
        BlockPos pos;
        for (int j = 0; j < rnd.nextIntBetweenInclusive(3, 6); j++) {
            pos = positions.remove(rnd.nextIntBetweenInclusive(0, positions.size()));
            if (level.getBlockState(pos.below()).is(ModBlocks.RAW_PURPUR.get())) {
                level.setBlock(pos, states.remove(rnd.nextInt(0, states.size())), 3);
                if (level.getBlockEntity(pos) instanceof BarrelBlockEntity barrel)
                    setLootBarrel(barrel);
            }
        }


        for (Direction direction : List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)) {
            for (int i = 0; i < rnd.nextIntBetweenInclusive(12, 20); i++)
                BlockHelper.placeColumn(level, BlockPos.of(origin.offset(i,direction)), 2, Blocks.AIR.defaultBlockState(),
                        (a, b) -> a.getBlockState(b).is(ModBlocks.RAW_PURPUR.get()));
        }
    }

    static int closestY(int base) {
        if (base >= 37) return 38;
        if (base >= 29) return 30;
        return 22;
    }

    static void setLootBarrel(BarrelBlockEntity barrel) {
        // TODO: custom loot table
        barrel.setLootTable(new ResourceLocation("minecraft", "chests/end_city_treasure"), RandomSupport.generateUniqueSeed());
    }
}
