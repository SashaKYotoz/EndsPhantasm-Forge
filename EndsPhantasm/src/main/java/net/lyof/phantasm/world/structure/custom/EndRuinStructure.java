package net.lyof.phantasm.world.structure.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lyof.phantasm.world.structure.ModStructures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinStructure;

import java.util.List;

public class EndRuinStructure extends OceanRuinStructure {
    public static final Codec<EndRuinStructure> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(settingsCodec(instance),
                    ResourceLocation.CODEC.listOf().fieldOf("pieces").forGetter(structure -> structure.pieces)
            ).apply(instance, EndRuinStructure::new));


    public final List<ResourceLocation> pieces;

    public EndRuinStructure(StructureSettings settings, List<ResourceLocation> pieces) {
        super(settings, OceanRuinStructure.Type.WARM, 0.8f, 1);
        this.pieces = pieces;
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.END_RUIN.get();
    }
}