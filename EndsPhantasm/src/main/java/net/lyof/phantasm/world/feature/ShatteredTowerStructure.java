package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.registries.ForgeRegistries;

public class ShatteredTowerStructure extends Feature<CountConfiguration> {
    public static final int R = 7;

    private static final ResourceLocation DRAGLING = ResourceLocation.fromNamespaceAndPath("unusualend", "dragling");
    private static final ResourceLocation LOOT_TABLE = Phantasm.makeID("chests/shattered_tower");
    private static final ResourceLocation CHALLENGE = Phantasm.makeID("shattered_tower");
    public static final Feature<CountConfiguration> INSTANCE = new ShatteredTowerStructure(CountConfiguration.CODEC);

    public ShatteredTowerStructure(Codec<CountConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<CountConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        CountConfiguration config = context.config();
        origin = world.getChunk(origin).getPos().getMiddleBlockPosition(origin.getY());

        int miny = world.getMinBuildHeight();
        int maxy = config.count().sample(random) + origin.getY();
        maxy = maxy + 7 - maxy % 7;

        this.setBlock(world, origin.atY(maxy + 1), ModBlocks.CHALLENGE_RUNE.get().defaultBlockState());
        if (world.getBlockEntity(origin.atY(maxy + 1)) instanceof ChallengeRuneBlockEntity challengeRune) {
            challengeRune.renderBase = true;
            challengeRune.setChallenge(CHALLENGE);
        }

        for (int sy = maxy; sy >= miny; sy--) {
            for (int sx = -R; sx <= R; sx++) {
                for (int sz = -R; sz <= R; sz++) {
                    if (sx * sx + sz * sz < R * R) {
                        if (sx * sx + sz * sz < (R - 1) * (R - 1)) {
                            this.setBlock(world, origin.atY(sy).east(sx).north(sz),
                                    Blocks.AIR.defaultBlockState());
                        }

                        if (sx * sx + sz * sz >= (R - 1) * (R - 1)) {
                            Block block = Blocks.OBSIDIAN;
                            double crying = (sy - 70) / (maxy - 70.0);
                            if (Math.random() + 0.1 < crying * crying * crying)
                                block = Blocks.CRYING_OBSIDIAN;
                            else if (Math.random() < 0.35)
                                block = Math.random() < 0.4 ? ModBlocks.POLISHED_OBSIDIAN.get() : ModBlocks.POLISHED_OBSIDIAN_BRICKS.get();

                            this.setBlock(world, origin.atY(sy).east(sx).north(sz),
                                    block.defaultBlockState());
                        } else if (sy == maxy) {
                            this.setBlock(world, origin.atY(sy).east(sx).north(sz),
                                    ModBlocks.POLISHED_OBSIDIAN_BRICKS.get().defaultBlockState());
                        } else if (sy == miny) {
                            this.setBlock(world, origin.atY(sy).east(sx).north(sz),
                                    Blocks.END_PORTAL.defaultBlockState());
                        }
                    }
                }
            }
            if (world.getBlockState(origin.atY(sy)).isAir())
                this.setBlock(world, origin.atY(sy), Blocks.CHAIN.defaultBlockState());
            if (sy < maxy && sy > miny) this.putStairs(world, origin.atY(sy));
            if (sy % 7 == 0 && sy != maxy && sy != miny) this.putPlatform(world, origin.atY(sy), random.nextInt(7));
        }
        return true;
    }

    public void putStairs(WorldGenLevel world, BlockPos center) {
        int y = center.getY() - world.getMinBuildHeight();

        for (int sx = -R + 1; sx <= R - 1; sx++) {
            for (int sz = -R + 1; sz <= R - 1; sz++) {
                if (sx * sx + sz * sz >= (R - 2) * (R - 2) && sx * sx + sz * sz < (R - 1) * (R - 1)) {
                    if (sx == R - 3 && sz == R - 3 && y % 4 == 1
                            || sx == -R + 3 && sz == -R + 3 && y % 4 == 3
                            || sx == R - 3 && sz == -R + 3 && y % 4 == 0
                            || sx == -R + 3 && sz == R - 3 && y % 4 == 2) {

                        this.setBlock(world, center.east(sx).north(sz),
                                Blocks.PURPUR_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM));
                    }

                    if (sx > R - 3 && y % 4 == 0
                            || sx < -R + 3 && y % 4 == 2
                            || sz > R - 3 && y % 4 == 1
                            || sz < -R + 3 && y % 4 == 3)
                        this.setBlock(world, center.east(sx).north(sz),
                                Blocks.PURPUR_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
                }
            }
        }

        if (Math.random() < 0.2 || center.getY() == 1) {
            BlockPos door = center.mutable();
            if (y % 4 == 0) door = door.east(R - 1);
            else if (y % 4 == 2) door = door.west(R - 1);
            else if (y % 4 == 1) door = door.north(R - 1);
            else door = door.south(R - 1);

            door = door.above();
            this.setBlock(world, door, Blocks.AIR.defaultBlockState());
            this.setBlock(world, door.above(), Blocks.AIR.defaultBlockState());
        }
    }

    public void putPlatform(WorldGenLevel world, BlockPos center, int roomtype) {
        if (roomtype == 0) {
            for (int sx = -R + 3; sx <= R - 3; sx++) {
                for (int sz = -R + 3; sz <= R - 3; sz++) {
                    if (sx * sx + sz * sz < (R - 4) * (R - 4)) {
                        this.setBlock(world, center.east(sx).north(sz), Blocks.PURPUR_BLOCK.defaultBlockState());
                    }
                }
            }
        } else if (roomtype == 1) {
            for (int sx = -R + 3; sx <= R - 3; sx++) {
                for (int sz = -R + 3; sz <= R - 3; sz++) {
                    if (sx * sx + sz * sz < (R - 4) * (R - 4)) {
                        this.setBlock(world, center.east(sx).north(sz), Blocks.PURPUR_BLOCK.defaultBlockState());
                    }
                }
            }
            this.setBlock(world, center.above(), Blocks.CHEST.defaultBlockState());
            if (world.getBlockEntity(center.above()) instanceof ChestBlockEntity chest)
                chest.setLootTable(LOOT_TABLE, world.getRandom().nextLong());
        } else if (roomtype == 2) {
            this.setBlock(world, center, Blocks.SPAWNER.defaultBlockState());
            if (world.getBlockEntity(center) instanceof SpawnerBlockEntity spawner) {
                CompoundTag nbt = spawner.getSpawner().save(new CompoundTag());
                nbt.putShort("MinSpawnDelay", (short) 200);
                nbt.putShort("SpawnCount", (short) 1);
                nbt.putShort("MaxNearbyEntities", (short) 2);
                nbt.remove("SpawnData");
                spawner.getSpawner().load(null, center, nbt);

                EntityType<?> dragling = ForgeRegistries.ENTITY_TYPES.getValue(DRAGLING);
                if (dragling != null)
                    spawner.setEntityId(dragling == EntityType.PIG ? EntityType.VEX : dragling, world.getRandom());
            }
        } else if (roomtype == 3) {
            center = center.north(world.getRandom().nextIntBetweenInclusive(-R + 4, R - 4)).east(world.getRandom().nextIntBetweenInclusive(-R + 4, R - 4));
            BlockPos.spiralAround(center, 1, Direction.NORTH, Direction.EAST).forEach(pos ->
                    this.setBlock(world, pos, Blocks.OBSIDIAN.defaultBlockState())
            );
            this.setBlock(world, center.above(), Blocks.CHEST.defaultBlockState());
            if (world.getBlockEntity(center.above()) instanceof ChestBlockEntity chest)
                chest.setLootTable(BuiltInLootTables.END_CITY_TREASURE, world.getRandom().nextLong());
        }
    }
}