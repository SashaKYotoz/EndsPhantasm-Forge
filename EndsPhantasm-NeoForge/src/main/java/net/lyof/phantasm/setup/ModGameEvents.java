package net.lyof.phantasm.setup;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.mixin.access.EndGatewayBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.EndFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Phantasm.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModGameEvents {
    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, ModItems.POME_SLICE.get(), ModEffects.CORROSION_POTION);
        builder.addMix(ModEffects.CORROSION_POTION, Items.REDSTONE, ModEffects.LONG_CORROSION_POTION);
        builder.addMix(ModEffects.CORROSION_POTION, Items.GLOWSTONE_DUST, ModEffects.STRONG_CORROSION_POTION);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Level entityLevel = event.getEntity().level();
        if (event.getTo() == Level.END && ConfigEntries.outerEndIntegration && entityLevel instanceof ServerLevel level) {
            BlockPos p = new BlockPos(1280, 60, 0);
            BlockPos pos1 = EndGatewayBlockEntityAccessor.getExitPos(level, p).above(2);
            if (level.getBlockState(pos1.below()).isAir()) {
                level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE)
                        .getOptional(EndFeatures.END_ISLAND).ifPresent(reference ->
                                reference.place(level, level.getChunkSource().getGenerator(),
                                        RandomSource.create(pos1.asLong()), pos1.below(2)));
            }
            event.getEntity().teleportTo(p.getX(), p.getY() + 5, p.getZ());
        }
    }
}