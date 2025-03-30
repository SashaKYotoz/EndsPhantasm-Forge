package net.lyof.phantasm.world.feature.tree.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lyof.phantasm.world.feature.tree.ModTrunkTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class PreamTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<PreamTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            trunkPlacerParts(instance).apply(instance, PreamTrunkPlacer::new));

    public PreamTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkTypes.PREAM_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> consumer, RandomSource random, int height, BlockPos startPos, TreeConfiguration config) {
        TrunkPlacer.setDirtAt(reader, consumer, random, startPos.below(), config);
        height = this.getTreeHeight(random) + 1;

        int f = height >= this.getTreeHeight(random) ? random.nextIntBetweenInclusive(3, height) : -1;
        List<FoliagePlacer.FoliageAttachment> foliages = new ArrayList<>();

        for (int i = 0; i < height; ++i) {
            this.placeLog(reader, consumer, random, startPos.above(i), config);
        }

        if (f >= 0)
            foliages.add(new FoliagePlacer.FoliageAttachment(startPos.above(f), 0, false));
        foliages.add(new FoliagePlacer.FoliageAttachment(startPos.above(height), 0, false));
        return foliages;
    }
}
