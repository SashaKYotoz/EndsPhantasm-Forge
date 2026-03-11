package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EndCityPieces.EndCityPiece.class)
public abstract class EndCityPiecesSectionGeneratorMixin extends TemplateStructurePiece {
    @Unique
    private static final ResourceLocation CHALLENGE_ID = Phantasm.makeID("elytra");
    @Unique private static final ResourceLocation STRUCTURE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "end_city/ship");
    @Shadow
    protected abstract ResourceLocation makeTemplateLocation();

    public EndCityPiecesSectionGeneratorMixin(StructurePieceType type, int length, StructureTemplateManager structureTemplateManager, ResourceLocation id, String template, StructurePlaceSettings placementData, BlockPos pos) {
        super(type, length, structureTemplateManager, id, template, placementData, pos);
    }
    @Inject(method = "handleDataMarker", at = @At("HEAD"), cancellable = true)
    public void placeElytraChallenge(String metadata, BlockPos pos, ServerLevelAccessor world, RandomSource random, BoundingBox boundingBox, CallbackInfo ci) {
        if (ConfigEntries.elytraChallenge && metadata.startsWith("Elytra") && this.makeTemplateLocation().equals(STRUCTURE_ID)) {
            if (ConfigEntries.elytraChallengeOffset.size() < 3) ConfigEntries.elytraChallengeOffset = List.of(0d, 2d, 8d);

            Direction axis = this.placeSettings.getRotation().rotate(Direction.SOUTH);
            pos = pos.relative(axis.getClockWise(), ConfigEntries.elytraChallengeOffset.get(0).intValue())
                    .below(ConfigEntries.elytraChallengeOffset.get(1).intValue())
                    .relative(axis, ConfigEntries.elytraChallengeOffset.get(2).intValue());
            world.setBlock(pos, ModBlocks.CHALLENGE_RUNE.get().defaultBlockState(), Block.UPDATE_ALL);

            if (world.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity rune) {
                rune.setChallenge(CHALLENGE_ID);
            }

            ci.cancel();
        }
    }
}