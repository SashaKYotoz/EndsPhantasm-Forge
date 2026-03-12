package net.lyof.phantasm.setup.compat;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.item.ModTiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class FarmersDelightCompat {
    public static void setup() {
        ModItems.EGGS_NIHILO_PROPERTIES = getEggNihiloProperties();
        if (Phantasm.isFarmersDelightLoaded())
            register();
    }

    public static FoodProperties getEggNihiloProperties() {
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(5)
                .saturationMod(0.8f);
        if (ModEffects.NOURISHMENT.isPresent())
            builder.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 1200, 0), 1.0f);

        return builder.build();
    }

    public static void register() {
        ModItems.CRYSTALLINE_KNIFE = ModItems.ITEMS.register("crystalline_knife", () -> new KnifeItem(ModTiers.CRYSTALLINE, 1.5f, -2f, new Item.Properties()));

        ModBlocks.PREAM_CABINET = ModBlocks.registerBlock("pream_cabinet",
                () -> new CabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).mapColor(MapColor.TERRACOTTA_YELLOW)));
    }
}