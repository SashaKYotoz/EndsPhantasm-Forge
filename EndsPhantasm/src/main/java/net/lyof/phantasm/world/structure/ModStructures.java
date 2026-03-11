package net.lyof.phantasm.world.structure;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.structure.custom.EndRuinStructure;
import net.lyof.phantasm.world.structure.processor.RandomReplaceStructureProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE = DeferredRegister.create(Registries.STRUCTURE_TYPE, Phantasm.MOD_ID);
    public static final RegistryObject<StructureType<EndRuinStructure>> END_RUIN =
            STRUCTURE.register("end_ruin", () -> () -> EndRuinStructure.CODEC);

    public static final DeferredRegister<StructureProcessorType<?>> PROCESSOR = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, Phantasm.MOD_ID);
    public static final RegistryObject<StructureProcessorType<RandomReplaceStructureProcessor>> RANDOM_REPLACE =
            PROCESSOR.register("random_replace", () -> () -> RandomReplaceStructureProcessor.CODEC);

    public static void register(IEventBus bus) {
        STRUCTURE.register(bus);
        PROCESSOR.register(bus);
    }
}