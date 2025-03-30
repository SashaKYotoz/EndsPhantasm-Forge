package net.lyof.phantasm.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class BlockHelper {
    public static boolean hasNeighbor(BlockGetter world, BlockPos pos, Predicate<BlockState> check) {
        return check.test(world.getBlockState(pos.above()))
                || check.test(world.getBlockState(pos.below()))
                || check.test(world.getBlockState(pos.north()))
                || check.test(world.getBlockState(pos.south()))
                || check.test(world.getBlockState(pos.east()))
                || check.test(world.getBlockState(pos.west()));
    }

    public static void placeColumn(LevelAccessor world, BlockPos start, int size, BlockState block) {
        placeColumn(world, start, size, block, (a, b) -> true);
    }

    public static void placeColumn(LevelAccessor world, BlockPos start, int size, BlockState block,
                                   BiPredicate<BlockGetter, BlockPos> condition) {
        for (int sy = 0; sy < size; sy++) {
            if (condition.test(world, start.above(sy)))
                world.setBlock(start.above(sy), block, 0);
        }
    }

//    public static List<Direction> getHorizontalDirections(RandomSource random, int n, Direction blacklist) {
//        List<Direction> result = new ArrayList<>();
//        Direction dir;
//        for (int i = 0; i < n; i++) {
//            do {
//                dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
//            } while (dir. < 0 || result.contains(dir) || dir == blacklist);
//
//            result.add(dir);
//        }
//        return result;
//    }
}
