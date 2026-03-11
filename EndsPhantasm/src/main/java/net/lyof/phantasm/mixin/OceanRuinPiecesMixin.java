package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.structure.custom.EndRuinStructure;
import net.lyof.phantasm.world.structure.processor.RandomReplaceStructureProcessor;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinPieces;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OceanRuinPieces.class)
public abstract class OceanRuinPiecesMixin {
    @Inject(method = "addPiece",
            at = @At("HEAD"), cancellable = true)
    private static void addVariantPieces(StructureTemplateManager manager, BlockPos pos, Rotation rotation, StructurePieceAccessor holder, RandomSource random, OceanRuinStructure structure, boolean large, float integrity, CallbackInfo ci) {

        if (structure instanceof EndRuinStructure ruin) {
            //for (int i = 0; i < random.nextBetween(2, 5); i++)
            holder.addPiece(new OceanRuinPieces.OceanRuinPiece(manager, (ResourceLocation) Util.getRandom(ruin.pieces.toArray(), random), pos, rotation, -0.95f, structure.biomeTemp, large));

            ci.cancel();
        }
    }

    @Mixin(OceanRuinPieces.OceanRuinPiece.class)
    public abstract static class PieceMixin extends TemplateStructurePiece {
        public PieceMixin(StructurePieceType type, int length, StructureTemplateManager structureTemplateManager, ResourceLocation id, String template, StructurePlaceSettings placementData, BlockPos pos) {
            super(type, length, structureTemplateManager, id, template, placementData, pos);
        }

        @WrapWithCondition(method = "postProcess", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/TemplateStructurePiece;postProcess(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/core/BlockPos;)V"))
        private boolean cancelVoidGeneration(TemplateStructurePiece instance, WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource source, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
            return this.templatePosition.getY() > 4;
        }

        // Yes this is a stupid way of checking if it's the right structure. Blame mojang for ignoring the ResourceLocation
        @WrapMethod(method = "makeSettings")
        private static StructurePlaceSettings alterPlacementData(Rotation rotation, float integrity, OceanRuinStructure.Type temperature, Operation<StructurePlaceSettings> original) {
            if (integrity < 0)
                return original.call(rotation, -integrity, temperature)
                        .addProcessor(new RandomReplaceStructureProcessor(0.15f, ModTags.Blocks.END_CRYSTAL_PLACEABLE_ON, ModBlocks.ACIDIC_MASS.get()));
            return original.call(rotation, integrity, temperature);
        }
    }
}