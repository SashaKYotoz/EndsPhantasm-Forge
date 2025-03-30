package net.lyof.phantasm.world.feature.tree.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.world.feature.tree.ModFoliageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class PreamFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<PreamFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            foliagePlacerParts(instance).apply(instance, PreamFoliagePlacer::new));

    public PreamFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliageTypes.PREAM_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader reader, FoliageSetter setter, RandomSource random, TreeConfiguration config, int trunkHeight, FoliageAttachment attachment, int foliageHeight, int radius, int offset) {
        BlockPos origin = attachment.pos().below();
        if (!reader.isStateAtPosition(origin.above(), state -> state.is(ModBlocks.PREAM_LOG.get())))
            radius = random.nextIntBetweenInclusive(3, 5);
        else radius = random.nextIntBetweenInclusive(2, 5);

        this.placeLeavesRow(reader, setter, random, config, origin, radius-1, -1, attachment.doubleTrunk());
        this.placeLeavesRow(reader, setter, random, config, origin, radius, 0, attachment.doubleTrunk());
        this.placeLeavesRow(reader, setter, random, config, origin, radius-1, 1, attachment.doubleTrunk());
    }

    @Override
    public int foliageHeight(RandomSource random, int i, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return dx*dx + dz*dz > radius*radius;
    }
}
