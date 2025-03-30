package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class ObsidianTowerStructure extends Feature<CountConfiguration> {
    public static final Feature<CountConfiguration> INSTANCE = new ObsidianTowerStructure(CountConfiguration.CODEC);

    public ObsidianTowerStructure(Codec<CountConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<CountConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        CountConfiguration config = context.config();
        origin = world.getChunk(origin).getPos().getMiddleBlockPosition(origin.getY());

        Phantasm.log("Hi " + origin);

        int maxy = config.count().sample(random) + origin.getY();
        maxy = maxy + 7 - maxy % 7;

        for (int sy = maxy; sy >= 0; sy--) {
            for (int sx = -8; sx < 9; sx++) {
                for (int sz = -8; sz < 9; sz++) {
                    if (sx*sx + sz*sz < 64 && (sx*sx + sz*sz >= 49 || sy == maxy || sy == 0)) {
                        Block block = Blocks.OBSIDIAN;
                        double crying = (sy - 60) / (maxy - 60.0);
                        if (Math.random() + 0.1 < crying * crying && crying > 0)
                            block = Blocks.CRYING_OBSIDIAN;
                        else if (Math.random() < 0.2)
                            block = Math.random() < 0.5 ? ModBlocks.POLISHED_OBSIDIAN.get() : ModBlocks.POLISHED_OBSIDIAN_BRICKS.get();

                        world.setBlock(origin.atY(sy).east(sx).north(sz),
                                block.defaultBlockState(), 3);
                    }
                    else if (sx*sx + sz*sz < 49) {
                        world.setBlock(origin.atY(sy).east(sx).north(sz),
                                Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
            if (world.getBlockState(origin.atY(sy)).isAir()) world.setBlock(origin.atY(sy), Blocks.CHAIN.defaultBlockState(), 3);
            if (sy < maxy && sy > 0) putStairs(world, origin.atY(sy));
            if (sy % 7 == 0 && sy != maxy && sy != 0) putPlatform(world, origin.atY(sy), random.nextInt(7));
            //if (sy % 5 == 0 && sy != 0) generateRoom(world, origin.withY(sy - 4));
        }

        return true;
    }

    public static void putStairs(WorldGenLevel world, BlockPos center) {
        int y = center.getY();

        for (int sx = -7; sx < 8; sx++) {
            for (int sz = -7; sz < 8; sz++) {
                if (sx*sx + sz*sz >= 36 && sx*sx + sz*sz < 49) {
                    if (sx >= 5 && y % 4 == 0
                            || sx <= -5 && y % 4 == 2
                            || sz >= 5 && y % 4 == 1
                            || sz <= -5 && y % 4 == 3)
                        world.setBlock(center.east(sx).north(sz),
                                Blocks.PURPUR_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP),
                                3);
                }
            }
        }

        if (Math.random() < 0.2 || center.getY() == 1) {
            BlockPos door = center.mutable();
            if (y % 4 == 0) door = door.east(7);
            else if (y % 4 == 2) door = door.west(7);
            else if (y % 4 == 1) door = door.north(7);
            else door = door.south(7);

            door = door.above();
            world.setBlock(door, Blocks.AIR.defaultBlockState(), 3);
            world.setBlock(door.above(), Blocks.AIR.defaultBlockState(), 3);
        }
    }

    public static void putPlatform(WorldGenLevel world, BlockPos center, int roomtype) {
        if (roomtype == 0) {
            for (int sx = -5; sx < 6; sx++) {
                for (int sz = -5; sz < 6; sz++) {
                    if (sx * sx + sz * sz < 16) {
                        world.setBlock(center.east(sx).north(sz), Blocks.PURPUR_BLOCK.defaultBlockState(),
                                3);
                    }
                }
            }
        }
        else if (roomtype == 1) {
            for (int sx = -5; sx < 6; sx++) {
                for (int sz = -5; sz < 6; sz++) {
                    if (sx * sx + sz * sz < 16) {
                        world.setBlock(center.east(sx).north(sz), Blocks.PURPUR_BLOCK.defaultBlockState(),
                                3);
                    }
                }
            }
            world.setBlock(center.above(), Blocks.CHEST.defaultBlockState(), 3);
            if (world.getBlockEntity(center.above()) instanceof ChestBlockEntity chest)
                chest.setLootTable(BuiltInLootTables.STRONGHOLD_CORRIDOR, world.getRandom().nextLong());
        }
        else if (roomtype == 2) {
            world.setBlock(center, Blocks.SPAWNER.defaultBlockState(), 3);
            if (world.getBlockEntity(center) instanceof SpawnerBlockEntity spawner) {
                CompoundTag nbt = spawner.getSpawner().save(new CompoundTag());
                nbt.putShort("MinSpawnDelay", (short) 200);
                nbt.putShort("SpawnCount", (short) 1);
                nbt.putShort("MaxNearbyEntities", (short) 2);
                nbt.remove("SpawnData");
                Phantasm.log(nbt);
                spawner.getSpawner().load(null, center, nbt);
                spawner.setEntityId(EntityType.VEX, world.getRandom());
            }
        }
        else if (roomtype == 3) {
            center = center.north(world.getRandom().nextIntBetweenInclusive(-3, 3)).east(world.getRandom().nextIntBetweenInclusive(-3, 3));
            BlockPos.spiralAround(center, 2, Direction.NORTH, Direction.EAST).forEach(pos ->
                    world.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3)
            );
            world.setBlock(center.above(), Blocks.CHEST.defaultBlockState(), 3);
            if (world.getBlockEntity(center.above()) instanceof ChestBlockEntity chest)
                chest.setLootTable(BuiltInLootTables.END_CITY_TREASURE, world.getRandom().nextLong());
        }
    }

    public static void generateRoom(WorldGenLevel world, BlockPos center) {
        if (Math.random() < 0.5 || center.getY() == 1) {
            BlockPos door = center.mutable();
            if (Math.random() <= 0.25) door = door.east(7);
            else if (Math.random() <= 0.25) door = door.west(7);
            else if (Math.random() <= 0.25) door = door.north(7);
            else door = door.south(7);

            world.setBlock(door, Blocks.AIR.defaultBlockState(), 3);
            world.setBlock(door.above(), Blocks.AIR.defaultBlockState(), 3);
        }

        if (Math.random() < 0.3) {
            world.setBlock(center, Blocks.SPAWNER.defaultBlockState(), 3);
            if (world.getBlockEntity(center) instanceof SpawnerBlockEntity spawner)
                spawner.setEntityId(EntityType.ZOMBIE, world.getRandom());
            world.setBlock(center.above(), Blocks.CHEST.defaultBlockState(), 3);
            if (world.getBlockEntity(center.above()) instanceof ChestBlockEntity chest)
                chest.setLootTable(BuiltInLootTables.STRONGHOLD_CORRIDOR, world.getRandom().nextLong());
        }
    }
}
