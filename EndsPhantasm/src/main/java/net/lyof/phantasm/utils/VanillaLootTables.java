package net.lyof.phantasm.utils;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Set;

public class VanillaLootTables {
    private static final Set<ResourceLocation> LOOT_TABLES = Sets.newHashSet();
    private static final Set<ResourceLocation> LOOT_TABLES_READ_ONLY;
    public static final ResourceLocation EMPTY;
    public static final ResourceLocation SPAWN_BONUS_CHEST;
    public static final ResourceLocation END_CITY_TREASURE_CHEST;
    public static final ResourceLocation SIMPLE_DUNGEON_CHEST;
    public static final ResourceLocation VILLAGE_WEAPONSMITH_CHEST;
    public static final ResourceLocation VILLAGE_TOOLSMITH_CHEST;
    public static final ResourceLocation VILLAGE_ARMORER_CHEST;
    public static final ResourceLocation VILLAGE_CARTOGRAPHER_CHEST;
    public static final ResourceLocation VILLAGE_MASON_CHEST;
    public static final ResourceLocation VILLAGE_SHEPARD_CHEST;
    public static final ResourceLocation VILLAGE_BUTCHER_CHEST;
    public static final ResourceLocation VILLAGE_FLETCHER_CHEST;
    public static final ResourceLocation VILLAGE_FISHER_CHEST;
    public static final ResourceLocation VILLAGE_TANNERY_CHEST;
    public static final ResourceLocation VILLAGE_TEMPLE_CHEST;
    public static final ResourceLocation VILLAGE_DESERT_HOUSE_CHEST;
    public static final ResourceLocation VILLAGE_PLAINS_CHEST;
    public static final ResourceLocation VILLAGE_TAIGA_HOUSE_CHEST;
    public static final ResourceLocation VILLAGE_SNOWY_HOUSE_CHEST;
    public static final ResourceLocation VILLAGE_SAVANNA_HOUSE_CHEST;
    public static final ResourceLocation ABANDONED_MINESHAFT_CHEST;
    public static final ResourceLocation NETHER_BRIDGE_CHEST;
    public static final ResourceLocation STRONGHOLD_LIBRARY_CHEST;
    public static final ResourceLocation STRONGHOLD_CROSSING_CHEST;
    public static final ResourceLocation STRONGHOLD_CORRIDOR_CHEST;
    public static final ResourceLocation DESERT_PYRAMID_CHEST;
    public static final ResourceLocation JUNGLE_TEMPLE_CHEST;
    public static final ResourceLocation JUNGLE_TEMPLE_DISPENSER_CHEST;
    public static final ResourceLocation IGLOO_CHEST_CHEST;
    public static final ResourceLocation WOODLAND_MANSION_CHEST;
    public static final ResourceLocation UNDERWATER_RUIN_SMALL_CHEST;
    public static final ResourceLocation UNDERWATER_RUIN_BIG_CHEST;
    public static final ResourceLocation BURIED_TREASURE_CHEST;
    public static final ResourceLocation SHIPWRECK_MAP_CHEST;
    public static final ResourceLocation SHIPWRECK_SUPPLY_CHEST;
    public static final ResourceLocation SHIPWRECK_TREASURE_CHEST;
    public static final ResourceLocation PILLAGER_OUTPOST_CHEST;
    public static final ResourceLocation BASTION_TREASURE_CHEST;
    public static final ResourceLocation BASTION_OTHER_CHEST;
    public static final ResourceLocation BASTION_BRIDGE_CHEST;
    public static final ResourceLocation BASTION_HOGLIN_STABLE_CHEST;
    public static final ResourceLocation ANCIENT_CITY_CHEST;
    public static final ResourceLocation ANCIENT_CITY_ICE_BOX_CHEST;
    public static final ResourceLocation RUINED_PORTAL_CHEST;
    public static final ResourceLocation WHITE_SHEEP_ENTITY;
    public static final ResourceLocation ORANGE_SHEEP_ENTITY;
    public static final ResourceLocation MAGENTA_SHEEP_ENTITY;
    public static final ResourceLocation LIGHT_BLUE_SHEEP_ENTITY;
    public static final ResourceLocation YELLOW_SHEEP_ENTITY;
    public static final ResourceLocation LIME_SHEEP_ENTITY;
    public static final ResourceLocation PINK_SHEEP_ENTITY;
    public static final ResourceLocation GRAY_SHEEP_ENTITY;
    public static final ResourceLocation LIGHT_GRAY_SHEEP_ENTITY;
    public static final ResourceLocation CYAN_SHEEP_ENTITY;
    public static final ResourceLocation PURPLE_SHEEP_ENTITY;
    public static final ResourceLocation BLUE_SHEEP_ENTITY;
    public static final ResourceLocation BROWN_SHEEP_ENTITY;
    public static final ResourceLocation GREEN_SHEEP_ENTITY;
    public static final ResourceLocation RED_SHEEP_ENTITY;
    public static final ResourceLocation BLACK_SHEEP_ENTITY;
    public static final ResourceLocation FISHING_GAMEPLAY;
    public static final ResourceLocation FISHING_JUNK_GAMEPLAY;
    public static final ResourceLocation FISHING_TREASURE_GAMEPLAY;
    public static final ResourceLocation FISHING_FISH_GAMEPLAY;
    public static final ResourceLocation CAT_MORNING_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_ARMORER_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_BUTCHER_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_CARTOGRAPHER_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_CLERIC_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_FARMER_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_FISHERMAN_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_FLETCHER_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_LEATHERWORKER_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_LIBRARIAN_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_MASON_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_SHEPHERD_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_TOOLSMITH_GIFT_GAMEPLAY;
    public static final ResourceLocation HERO_OF_THE_VILLAGE_WEAPONSMITH_GIFT_GAMEPLAY;
    public static final ResourceLocation SNIFFER_DIGGING_GAMEPLAY;
    public static final ResourceLocation PIGLIN_BARTERING_GAMEPLAY;
    public static final ResourceLocation DESERT_WELL_ARCHAEOLOGY;
    public static final ResourceLocation DESERT_PYRAMID_ARCHAEOLOGY;
    public static final ResourceLocation TRAIL_RUINS_COMMON_ARCHAEOLOGY;
    public static final ResourceLocation TRAIL_RUINS_RARE_ARCHAEOLOGY;
    public static final ResourceLocation OCEAN_RUIN_WARM_ARCHAEOLOGY;
    public static final ResourceLocation OCEAN_RUIN_COLD_ARCHAEOLOGY;


    public VanillaLootTables() {
    }

    private static ResourceLocation register(String id) {
        return registerLootTable(new ResourceLocation(id));
    }

    private static ResourceLocation registerLootTable(ResourceLocation id) {
        if (LOOT_TABLES.add(id)) {
            return id;
        } else {
            throw new IllegalArgumentException("" + id + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> getAll() {
        return LOOT_TABLES_READ_ONLY;
    }

    static {
        LOOT_TABLES_READ_ONLY = Collections.unmodifiableSet(LOOT_TABLES);
        EMPTY = new ResourceLocation("empty");
        SPAWN_BONUS_CHEST = register("chests/spawn_bonus_chest");
        END_CITY_TREASURE_CHEST = register("chests/end_city_treasure");
        SIMPLE_DUNGEON_CHEST = register("chests/simple_dungeon");
        VILLAGE_WEAPONSMITH_CHEST = register("chests/village/village_weaponsmith");
        VILLAGE_TOOLSMITH_CHEST = register("chests/village/village_toolsmith");
        VILLAGE_ARMORER_CHEST = register("chests/village/village_armorer");
        VILLAGE_CARTOGRAPHER_CHEST = register("chests/village/village_cartographer");
        VILLAGE_MASON_CHEST = register("chests/village/village_mason");
        VILLAGE_SHEPARD_CHEST = register("chests/village/village_shepherd");
        VILLAGE_BUTCHER_CHEST = register("chests/village/village_butcher");
        VILLAGE_FLETCHER_CHEST = register("chests/village/village_fletcher");
        VILLAGE_FISHER_CHEST = register("chests/village/village_fisher");
        VILLAGE_TANNERY_CHEST = register("chests/village/village_tannery");
        VILLAGE_TEMPLE_CHEST = register("chests/village/village_temple");
        VILLAGE_DESERT_HOUSE_CHEST = register("chests/village/village_desert_house");
        VILLAGE_PLAINS_CHEST = register("chests/village/village_plains_house");
        VILLAGE_TAIGA_HOUSE_CHEST = register("chests/village/village_taiga_house");
        VILLAGE_SNOWY_HOUSE_CHEST = register("chests/village/village_snowy_house");
        VILLAGE_SAVANNA_HOUSE_CHEST = register("chests/village/village_savanna_house");
        ABANDONED_MINESHAFT_CHEST = register("chests/abandoned_mineshaft");
        NETHER_BRIDGE_CHEST = register("chests/nether_bridge");
        STRONGHOLD_LIBRARY_CHEST = register("chests/stronghold_library");
        STRONGHOLD_CROSSING_CHEST = register("chests/stronghold_crossing");
        STRONGHOLD_CORRIDOR_CHEST = register("chests/stronghold_corridor");
        DESERT_PYRAMID_CHEST = register("chests/desert_pyramid");
        JUNGLE_TEMPLE_CHEST = register("chests/jungle_temple");
        JUNGLE_TEMPLE_DISPENSER_CHEST = register("chests/jungle_temple_dispenser");
        IGLOO_CHEST_CHEST = register("chests/igloo_chest");
        WOODLAND_MANSION_CHEST = register("chests/woodland_mansion");
        UNDERWATER_RUIN_SMALL_CHEST = register("chests/underwater_ruin_small");
        UNDERWATER_RUIN_BIG_CHEST = register("chests/underwater_ruin_big");
        BURIED_TREASURE_CHEST = register("chests/buried_treasure");
        SHIPWRECK_MAP_CHEST = register("chests/shipwreck_map");
        SHIPWRECK_SUPPLY_CHEST = register("chests/shipwreck_supply");
        SHIPWRECK_TREASURE_CHEST = register("chests/shipwreck_treasure");
        PILLAGER_OUTPOST_CHEST = register("chests/pillager_outpost");
        BASTION_TREASURE_CHEST = register("chests/bastion_treasure");
        BASTION_OTHER_CHEST = register("chests/bastion_other");
        BASTION_BRIDGE_CHEST = register("chests/bastion_bridge");
        BASTION_HOGLIN_STABLE_CHEST = register("chests/bastion_hoglin_stable");
        ANCIENT_CITY_CHEST = register("chests/ancient_city");
        ANCIENT_CITY_ICE_BOX_CHEST = register("chests/ancient_city_ice_box");
        RUINED_PORTAL_CHEST = register("chests/ruined_portal");
        WHITE_SHEEP_ENTITY = register("entities/sheep/white");
        ORANGE_SHEEP_ENTITY = register("entities/sheep/orange");
        MAGENTA_SHEEP_ENTITY = register("entities/sheep/magenta");
        LIGHT_BLUE_SHEEP_ENTITY = register("entities/sheep/light_blue");
        YELLOW_SHEEP_ENTITY = register("entities/sheep/yellow");
        LIME_SHEEP_ENTITY = register("entities/sheep/lime");
        PINK_SHEEP_ENTITY = register("entities/sheep/pink");
        GRAY_SHEEP_ENTITY = register("entities/sheep/gray");
        LIGHT_GRAY_SHEEP_ENTITY = register("entities/sheep/light_gray");
        CYAN_SHEEP_ENTITY = register("entities/sheep/cyan");
        PURPLE_SHEEP_ENTITY = register("entities/sheep/purple");
        BLUE_SHEEP_ENTITY = register("entities/sheep/blue");
        BROWN_SHEEP_ENTITY = register("entities/sheep/brown");
        GREEN_SHEEP_ENTITY = register("entities/sheep/green");
        RED_SHEEP_ENTITY = register("entities/sheep/red");
        BLACK_SHEEP_ENTITY = register("entities/sheep/black");
        FISHING_GAMEPLAY = register("gameplay/fishing");
        FISHING_JUNK_GAMEPLAY = register("gameplay/fishing/junk");
        FISHING_TREASURE_GAMEPLAY = register("gameplay/fishing/treasure");
        FISHING_FISH_GAMEPLAY = register("gameplay/fishing/fish");
        CAT_MORNING_GIFT_GAMEPLAY = register("gameplay/cat_morning_gift");
        HERO_OF_THE_VILLAGE_ARMORER_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/armorer_gift");
        HERO_OF_THE_VILLAGE_BUTCHER_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/butcher_gift");
        HERO_OF_THE_VILLAGE_CARTOGRAPHER_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/cartographer_gift");
        HERO_OF_THE_VILLAGE_CLERIC_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/cleric_gift");
        HERO_OF_THE_VILLAGE_FARMER_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/farmer_gift");
        HERO_OF_THE_VILLAGE_FISHERMAN_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/fisherman_gift");
        HERO_OF_THE_VILLAGE_FLETCHER_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/fletcher_gift");
        HERO_OF_THE_VILLAGE_LEATHERWORKER_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/leatherworker_gift");
        HERO_OF_THE_VILLAGE_LIBRARIAN_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/librarian_gift");
        HERO_OF_THE_VILLAGE_MASON_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/mason_gift");
        HERO_OF_THE_VILLAGE_SHEPHERD_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/shepherd_gift");
        HERO_OF_THE_VILLAGE_TOOLSMITH_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/toolsmith_gift");
        HERO_OF_THE_VILLAGE_WEAPONSMITH_GIFT_GAMEPLAY = register("gameplay/hero_of_the_village/weaponsmith_gift");
        SNIFFER_DIGGING_GAMEPLAY = register("gameplay/sniffer_digging");
        PIGLIN_BARTERING_GAMEPLAY = register("gameplay/piglin_bartering");
        DESERT_WELL_ARCHAEOLOGY = register("archaeology/desert_well");
        DESERT_PYRAMID_ARCHAEOLOGY = register("archaeology/desert_pyramid");
        TRAIL_RUINS_COMMON_ARCHAEOLOGY = register("archaeology/trail_ruins_common");
        TRAIL_RUINS_RARE_ARCHAEOLOGY = register("archaeology/trail_ruins_rare");
        OCEAN_RUIN_WARM_ARCHAEOLOGY = register("archaeology/ocean_ruin_warm");
        OCEAN_RUIN_COLD_ARCHAEOLOGY = register("archaeology/ocean_ruin_cold");
    }
}
