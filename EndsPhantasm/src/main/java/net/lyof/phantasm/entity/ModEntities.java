package net.lyof.phantasm.entity;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.custom.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Phantasm.MOD_ID);
    public static final RegistryObject<EntityDataSerializer<ResourceLocation>> TRACKED_LOCATION = SERIALIZER.register("tracked_location", () -> EntityDataSerializer.simple(FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::readResourceLocation));

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Phantasm.MOD_ID);
    public static final RegistryObject<EntityType<CrystieEntity>> CRYSTIE = register("crystie",
            EntityType.Builder.of(CrystieEntity::new, MobCategory.MONSTER).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<BehemothEntity>> BEHEMOTH = register("behemoth",
            EntityType.Builder.of(BehemothEntity::new, MobCategory.MONSTER).sized(1f, 2f));
    public static final RegistryObject<EntityType<PolyppieEntity>> POLYPPIE = register("polyppie",
            EntityType.Builder.of(PolyppieEntity::new, MobCategory.MISC).sized(0.7f, 0.6f));
    public static final RegistryObject<EntityType<SourSludgeEntity>> SOUR_SLUDGE = register("sour_sludge",
            EntityType.Builder.of(SourSludgeEntity::new, MobCategory.MISC).sized(2.04f, 2.04f));

    public static final RegistryObject<EntityType<ChoralArrowEntity>> CHORAL_ARROW = register("choral_arrow",
            EntityType.Builder.of(ChoralArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
        return ENTITIES.register(registryname, () -> entityTypeBuilder.build(registryname));
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(CRYSTIE.get(), CrystieEntity.createAttributes().build());
        event.put(BEHEMOTH.get(), BehemothEntity.createAttributes().build());
        event.put(POLYPPIE.get(), PolyppieEntity.createAttributes().build());
        event.put(SOUR_SLUDGE.get(), SourSludgeEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerEntitiesSpawn(SpawnPlacementRegisterEvent event) {
        event.register(CRYSTIE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.WORLD_SURFACE, CrystieEntity::checkCrystieSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(BEHEMOTH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BehemothEntity::canMobSpawn, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(SOUR_SLUDGE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
    }

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
        SERIALIZER.register(bus);
    }
}