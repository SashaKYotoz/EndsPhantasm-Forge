package net.lyof.phantasm.entities;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entities.custom.BehemothEntity;
import net.lyof.phantasm.entities.custom.ChoralArrowEntity;
import net.lyof.phantasm.entities.custom.CrystieEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Phantasm.MOD_ID);
    public static final RegistryObject<EntityType<CrystieEntity>> CRYSTIE = register("crystie",
            EntityType.Builder.of(CrystieEntity::new, MobCategory.MONSTER).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<BehemothEntity>> BEHEMOTH = register("behemoth",
            EntityType.Builder.of(BehemothEntity::new, MobCategory.MONSTER).sized(1f, 2f));

    public static final RegistryObject<EntityType<ChoralArrowEntity>> CHORAL_ARROW = register("choral_arrow",
            EntityType.Builder.of(ChoralArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
        return ENTITIES.register(registryname, () -> entityTypeBuilder.build(registryname));
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(CRYSTIE.get(), CrystieEntity.createAttributes().build());
        event.put(BEHEMOTH.get(), BehemothEntity.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerEntitiesSpawn(SpawnPlacementRegisterEvent event){
        event.register(CRYSTIE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.WORLD_SURFACE,CrystieEntity::checkCrystieSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(BEHEMOTH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
    }
}