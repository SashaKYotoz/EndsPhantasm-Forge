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
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.AddPackFindersEvent;
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

import java.nio.file.Path;

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

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModTrunkTypes.TRUNK_TYPE_REGISTRY.register(modEventBus);
        ModFoliageTypes.FOLIAGE_PLACER_REGISTRY.register(modEventBus);

        if (Phantasm.isFarmersDelightLoaded())
            FarmersDelightCompat.setup();

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
        modEventBus.addListener(this::registerPacks);
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
        ConfiguredData.registerClient();

        for (RegistryObject<Block> block : ModBlocks.BLOCK_CUTOUT)
            ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
        for (RegistryObject<Block> block : ModBlocks.BLOCK_TRANSLUCENT)
            ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.translucent());
    }

    private void registerPacks(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            if (isFarmersDelightLoaded()) {
                Path path = ModList.get().getModFileById("phantasm").getFile().findResource("resourcepacks/compat_farmersdelight");
                Pack builtinDataPack = Pack.readMetaAndCreate(
                        "phantasm:fd_compat",
                        Component.literal("Phantasm FD compat"),
                        false,
                        (a) -> new PathPackResources(a, path, true),
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.TOP,
                        PackSource.create((arg) -> Component.translatable("pack.nameAndSource", arg,
                                Component.translatable("pack.source.builtin")).withStyle(ChatFormatting.GRAY), false)
                );
                event.addRepositorySource((packConsumer) -> packConsumer.accept(builtinDataPack));
            }
            if (ModList.get().isLoaded("jeed")) {
                Path path = ModList.get().getModFileById("phantasm").getFile().findResource("resourcepacks/compat_jeed");
                Pack builtinDataPack = Pack.readMetaAndCreate(
                        "phantasm:jeed_compat",
                        Component.literal("Phantasm JEED compat"),
                        false,
                        (a) -> new PathPackResources(a, path, true),
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.TOP,
                        PackSource.create((arg) -> Component.translatable("pack.nameAndSource", arg,
                                Component.translatable("pack.source.builtin")).withStyle(ChatFormatting.GRAY), false)
                );
                event.addRepositorySource((packConsumer) -> packConsumer.accept(builtinDataPack));
            }
            Path path = ModList.get().getModFileById("phantasm").getFile().findResource("resourcepacks/phantasm_connected_glass");
            Pack builtinDataPack = Pack.readMetaAndCreate(
                    "phantasm:phantasm_connected_glass",
                    Component.literal("Phantasm Connected Glass"),
                    false,
                    (a) -> new PathPackResources(a, path, true),
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    PackSource.create((arg) -> Component.translatable("pack.nameAndSource", arg,
                            Component.translatable("pack.source.builtin")).withStyle(ChatFormatting.GRAY), false)
            );
            event.addRepositorySource((packConsumer) -> packConsumer.accept(builtinDataPack));
        }
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