package net.lyof.phantasm.setup.compat;

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
        ModItems.EGGS_NIHILO_PROPERTIES = new FoodProperties.Builder().nutrition(5).saturationMod(0.8f)
                .effect(new MobEffectInstance(ModEffects.NOURISHMENT.get(), 1200, 0), 1)
                .build();
    }

    public static void register() {
        ModItems.CRYSTALLINE_KNIFE = ModItems.ITEMS.register("crystalline_sword", () -> new KnifeItem(ModTiers.CRYSTALLINE, 1.5f, -2f, new Item.Properties()));

        ModBlocks.PREAM_CABINET = ModBlocks.BLOCKS.register("pream_cabinet",
                () -> new CabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).mapColor(MapColor.TERRACOTTA_YELLOW)));
    }
}
