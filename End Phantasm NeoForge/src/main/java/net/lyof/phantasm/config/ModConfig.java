package net.lyof.phantasm.config;

import com.google.gson.Gson;
import net.lyof.phantasm.Phantasm;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ModConfig {
    static final ConfigEntry<Double> VERSION = new ConfigEntry<>("TECHNICAL.VERSION_DO_NOT_EDIT", 0d);
    static final ConfigEntry<Boolean> RELOAD = new ConfigEntry<>("TECHNICAL.FORCE_RESET", false);

    static Map CONFIG = new TreeMap<>();


    public static void register() {
        register(false);
    }

    public static void register(boolean force) {
        String path = System.getProperty("user.dir") + File.separator +
                "config" + File.separator + Phantasm.MOD_ID + ".json";

        Phantasm.log("Loading Configs for Phantasm");

        // Create config file if it doesn't exist already
        File config = new File(path);
        boolean create = !config.isFile();

        if (create || force) {
            try {
                config.delete();
                config.createNewFile();

                FileWriter writer = new FileWriter(path);
                writer.write(DEFAULT_CONFIG);
                writer.close();

                Phantasm.log("Phantasm Config file created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        String configContent = DEFAULT_CONFIG;
        try {
            configContent = FileUtils.readFileToString(config, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CONFIG = new Gson().fromJson(parseJson(configContent), Map.class);
        ConfigEntries.reload();

        if (!force && (RELOAD.get() || VERSION.get() < getVersion())) {
            register(true);
        }
    }

    static String parseJson(String text) {
        StringBuilder result = new StringBuilder();

        for (String line : text.split("\n")) {
            if (!line.strip().startsWith("//"))
                result.append("\n").append(line);
        }

        return result.toString();
    }

    static double getVersion() {
        String text = DEFAULT_CONFIG;
        int start = 0;

        while (!List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.').contains(text.charAt(start))) {
            start++;
        }
        int end = start + 1;
        while (List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.').contains(text.charAt(end))) {
            end++;
        }

        return Double.parseDouble(text.substring(start, end));
    }


    static final String DEFAULT_CONFIG = """
            {
              "TECHNICAL": {
                "VERSION_DO_NOT_EDIT": 1.5,
                "FORCE_RESET": false
              },
            
              // This config file uses a custom defined parser. That's why there are comments here, they wouldn't be valid in any other .json file.
              //    To add a comment yourself, just start a line with // like I did here
              //    (although their main use is explaining you what the entries do)
            
              // CATEGORY: WORLD GEN
              "world_gen": {
                "biomes": {
                  // Compatibility mode for datapacks modifying the End generation, such as Nullscape or Endercon
                  // Value must be one of:
                    // "default": Phantasm will replace an even proportion of the End Highlands with its custom biomes, mimicking its noise values
                      // theoretically works with any datapack, but might not be the prettiest
                    // "nullscape": Optimizes noise values to work best with Nullscape
                    // "endercon": Optimizes noise values to work best with Endercon
                    // "automatic": Same as default, but will work like "nullscape" or "endercon" depending on loaded mods
                      // if using the datapack versions, set this manually
                  "datapack_compatibility": "automatic",
                  // Should the End's biome source be partially overrode so that the main island biome always generate at the center
                  "force_main_island": true,
            
                  // DEAMING DEN
                  "dreaming_den": {
                    "generate": true,
                    "generation_weight": 1.5
                  },
            
                  // ACIDBURNT ABYSSES
                  "acidburnt_abysses": {
                    "generate": true,
                    // Fabric's biome API prioritizes biomes added first, so this one having a higher weight is only to balance that
                    "generation_weight": 2.5
                  }
                },
                // Should Fallen Stars appear in the End's sky
                "do_fallen_stars": true,
                // Should Raw Purpur stripes appear on the islands' sides
                "do_raw_purpur": true,
                // y offset to be added to the Raw Purpur stripes generation
                "raw_purpur_offset": 0,
            
                "end_spires": {
                  // Should the main island's obsidian spires be prettified with crying obsidian
                  "beautify": true,
                  // Should the main island's obsidian spires never have iron bars around the End Crystal
                  "no_crystal_cages": false,
                  // Height to be added to the spires, mostly useful in case of datapacks raising the main island level
                  "extra_height": 0
                }
              },
            
              // CATEGORY: GAMEPLAY
              "gameplay": {
                // Should End portals teleport to the outer islands instead of the Dragon's
                "outer_end_in_gameloop": true,
            
                "dragon": {
                  // Should the Dragon's Fireballs create a firey explosion on impact
                  "explosive_fireballs": true,
                  // Should Endermen not be aggroed when looked at during the fight
                  "no_pesky_endermen": false
                },
            
                "behemoth": {
                  // Distance (in blocks) at which walking close to a Behemoth will aggro it
                  "aggro_range": 6,
                  // Distance (in blocks) at which walking close to a Behemoth while sneaking will aggro it
                  "aggro_range_sneaking": 0
                }
              },
            
              // CATEGORY: EQUIPMENT
              "equipment": {
                // Advancement needed to enable elytra boosting. Leave blank "" to disable the need for one
                "elytra_boost_advancement": "minecraft:end/kill_dragon",
                // Crystalline tools boost ratio
                "crystal_xp_boost": 3,
            
                // Should eating a Chorus Fruit Salad teleport in/out of the End
                "chorus_fruit_salad_teleportation": true,
                // Max Chorus Fruit Salad stack size
                "chorus_fruit_salad_stack_size": 1,
            
                // Max uses for Shattered Pendants
                "shattered_pendant_durability": 6,
            
                // Range (in blocks) for Crossbow Choral Arrows and Subwoofer Blocks
                "subwoofer_range": 6
              }
            }""";
}
