package net.lyof.phantasm.world.structure.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lyof.phantasm.world.structure.ModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class RandomReplaceStructureProcessor extends StructureProcessor {
    public static final Codec<RandomReplaceStructureProcessor> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.floatRange(0, 1).fieldOf("chance").forGetter(processor -> processor.chance),
                    TagKey.codec(Registries.BLOCK).fieldOf("affected").forGetter(processor -> processor.affectedBlocks),
                    ForgeRegistries.BLOCKS.getCodec().fieldOf("result").forGetter(processor -> processor.result)
            ).apply(instance, RandomReplaceStructureProcessor::new));

    protected final float chance;
    protected final TagKey<Block> affectedBlocks;
    protected final Block result;

    public RandomReplaceStructureProcessor(float chance, TagKey<Block> affectedBlocks, Block result) {
        this.chance = chance;
        this.affectedBlocks = affectedBlocks;
        this.result = result;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings data) {
        current = super.processBlock(world, pos, pivot, original, current, data);
        if (current != null && current.state().is(this.affectedBlocks) && data.getRandom(current.pos()).nextFloat() < this.chance)
            return new StructureTemplate.StructureBlockInfo(current.pos(), this.result.defaultBlockState(), current.nbt());
        return current;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.RANDOM_REPLACE.get();
    }
}