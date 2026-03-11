package net.lyof.phantasm;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.entities.ModBlockEntities;
import net.lyof.phantasm.client.particles.ModParticles;
import net.lyof.phantasm.config.ConfiguredData;
import net.lyof.phantasm.config.ModConfig;
import net.lyof.phantasm.effect.BetterBrewingRecipe;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.item.ModTabs;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.compat.FarmersDelightCompat;
import net.lyof.phantasm.sound.ModSounds;
import net.lyof.phantasm.world.ModFeatures;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.lyof.phantasm.world.feature.tree.ModFoliageTypes;
import net.lyof.phantasm.world.feature.tree.ModTrunkTypes;
import net.lyof.phantasm.world.structure.ModStructures;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("removal")
@Mod(Phantasm.MOD_ID)
public class Phantasm {
    public static final String MOD_ID = "phantasm";
    public static final Logger LOGGER = LoggerFactory.getLogger("End's Phantasm");

    public Phantasm() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ForgeEvents.class);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModConfig.register();
        ConfiguredData.register();

        if (Phantasm.isFarmersDelightLoaded())
            FarmersDelightCompat.setup();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModTrunkTypes.TRUNK_TYPE_REGISTRY.register(modEventBus);
        ModFoliageTypes.FOLIAGE_PLACER_REGISTRY.register(modEventBus);
        ModTabs.CREATIVE_TAB.register(modEventBus);
        ModParticles.PARTICLE_TYPES.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModStructures.register(modEventBus);
        ModEntities.register(modEventBus);
        EndDataCompat.register();
        ModEffects.register(modEventBus);
        ModSounds.register();
        modEventBus.addListener(ModPackets::registerPackets);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }

    public static ResourceLocation makeID(String id) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, ModItems.POMB_SLICE.get(), ModEffects.CORROSION_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(ModEffects.CORROSION_POTION.get(), Items.REDSTONE, ModEffects.LONG_CORROSION_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(ModEffects.CORROSION_POTION.get(), Items.GLOWSTONE_DUST, ModEffects.STRONG_CORROSION_POTION.get()));
    }

    private void clientSetup(FMLClientSetupEvent event) {
        for (RegistryObject<Block> block : ModBlocks.BLOCK_CUTOUT)
            ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
        for (RegistryObject<Block> block : ModBlocks.BLOCK_TRANSLUCENT)
            ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.translucent());
    }

    public static boolean isFarmersDelightLoaded() {
        return ModList.get().isLoaded("farmersdelight");
    }

    public static boolean isVinURLLoaded() {
        return ModList.get().isLoaded("vinurl");
    }

    @Deprecated
    public static <T> T log(T message) {
        return log(message, 0);
    }

    public static <T> T log(T message, int level) {
        if (level == 3 && FMLLoader.isProduction())
            level = 1;

        if (level == 0)
            LOGGER.info("[Phantasm] {}", message);
        else if (level == 1)
            LOGGER.warn("[Phantasm] {}", message);
        else if (level == 2)
            LOGGER.error("[Phantasm] {}", message);
        else if (level == 3)
            LOGGER.debug("[Phantasm] {}", message);
        return message;
    }
}