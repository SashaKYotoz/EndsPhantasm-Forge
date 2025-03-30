package net.lyof.phantasm.entities;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entities.custom.BehemothEntity;
import net.lyof.phantasm.entities.custom.ChoralArrowEntity;
import net.lyof.phantasm.entities.custom.CrystieEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Phantasm.MOD_ID);
    public static final DeferredHolder<EntityType<?>,EntityType<CrystieEntity>> CRYSTIE = register("crystie",
            EntityType.Builder.of(CrystieEntity::new, MobCategory.MONSTER).sized(0.5f, 0.5f));
    public static final DeferredHolder<EntityType<?>,EntityType<BehemothEntity>> BEHEMOTH = register("behemoth",
            EntityType.Builder.of(BehemothEntity::new, MobCategory.MONSTER).sized(1f, 2f));
    public static final DeferredHolder<EntityType<?>,EntityType<ChoralArrowEntity>> CHORAL_ARROW = register("choral_arrow",
            EntityType.Builder.of(ChoralArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryName, EntityType.Builder<T> entityTypeBuilder) {
        return ENTITIES.register(registryName, () -> entityTypeBuilder.build(registryName));
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(CRYSTIE.get(), CrystieEntity.createAttributes().build());
        event.put(BEHEMOTH.get(), BehemothEntity.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerEntitiesSpawn(RegisterSpawnPlacementsEvent event){
        event.register(CRYSTIE.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.WORLD_SURFACE,CrystieEntity::checkCrystieSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(BEHEMOTH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    }
}