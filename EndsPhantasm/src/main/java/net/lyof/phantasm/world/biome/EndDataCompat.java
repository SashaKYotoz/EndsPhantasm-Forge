package net.lyof.phantasm.world.biome;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EndDataCompat {
    public static void register() {
        add(ModBiomes.DREAMING_DEN.location(),
                () -> Math.max(ConfigEntries.dreamingDenWeight, 0));
        add(ModBiomes.ACIDBURNT_ABYSSES.location(),
                () -> Math.max(ConfigEntries.acidburntAbyssesWeight, 0));
    }

    public static EndDataCompat.Mode getCompatibilityMode() {
        return switch (ConfigEntries.datapackMode) {
            case "automatic" -> {
                if (ModList.get().isLoaded("nullscape"))
                    yield Mode.NULLSCAPE;
                else yield Mode.CUSTOM;
            }
            case "vanilla" -> Mode.VANILLA;
            case "default" -> Mode.DEFAULT;
            case "nullscape" -> Mode.NULLSCAPE;
            default -> Mode.CUSTOM;
        };
    }


    private static final List<Pair<ResourceLocation, Supplier<Double>>> BIOMES_WEIGHT = new ArrayList<>();
    private static final List<Pair<ResourceLocation, JsonObject>> BIOMES_NOISE = new ArrayList<>();
    private static final List<Pair<ResourceLocation, JsonObject>> BIOMES_RULES = new ArrayList<>();

    public static void add(ResourceLocation biome, Supplier<Double> weight) {
        if (BIOMES_WEIGHT.stream().noneMatch(pair -> pair.getFirst().equals(biome)))
            BIOMES_WEIGHT.add(new Pair<>(biome, weight));
    }

    public static void add(ResourceLocation biome, JsonObject noise) {
        BIOMES_NOISE.add(new Pair<>(biome, noise));
    }

    public static void addRules(ResourceLocation biome, JsonObject rules) {
        BIOMES_RULES.add(new Pair<>(biome, rules));
    }

    public static void clear() {
        BIOMES_WEIGHT.removeAll(BIOMES_WEIGHT.stream().filter(p -> p.getSecond().get() < 0).toList());
        BIOMES_NOISE.clear();
        BIOMES_RULES.clear();
    }

    public static boolean contains(ResourceLocation biome) {
        for (Pair<ResourceLocation, Supplier<Double>> pair : BIOMES_WEIGHT) {
            if (pair.getFirst().equals(biome))
                return true;
        }
        for (Pair<ResourceLocation, JsonObject> pair : BIOMES_NOISE) {
            if (pair.getFirst().equals(biome))
                return true;
        }
        return false;
    }

    public static List<Pair<ResourceLocation, Double>> getEnabledWeightedBiomes() {
        List<Pair<ResourceLocation, Double>> result = new ArrayList<>();
        for (Pair<ResourceLocation, Supplier<Double>> pair : BIOMES_WEIGHT) {
            if (BIOMES_NOISE.stream().noneMatch(p -> p.getFirst().equals(pair.getFirst())) && Math.abs(pair.getSecond().get()) > 0)
                result.add(new Pair<>(pair.getFirst(), Math.abs(pair.getSecond().get())));
        }
        return result;
    }

    public static List<Pair<ResourceLocation, JsonObject>> getNoiseBiomes() {
        return BIOMES_NOISE;
    }

    public static List<Pair<ResourceLocation, JsonObject>> getCustomRules() {
        return BIOMES_RULES;
    }

    public static JsonElement splitHypercube(ResourceLocation biome, JsonObject highlands, String noise, double min, double max) {
        JsonArray array = new JsonArray();
        array.add(min);
        array.add(max);
        JsonObject parameters = highlands.deepCopy();
        parameters.asMap().replace(noise, array);

        JsonObject result = new JsonObject();
        result.addProperty("biome", biome.toString());
        result.add("parameters", parameters);
        return result;
    }

    public static void read(JsonObject json) {
        if (!json.has("biome")) return;

        if (json.has("weight")) {
            double weight = json.get("weight").getAsDouble();
            add(ResourceLocation.parse(json.get("biome").getAsString()), () -> -weight);
        }
        else if (json.has("parameters")) {
            JsonObject noise = new JsonObject();
            noise.add("biome", json.get("biome"));
            noise.add("parameters", json.get("parameters"));
            add(ResourceLocation.parse(json.get("biome").getAsString()), noise);
        }

        if (json.has("surface_rule")) {
            addRules(ResourceLocation.parse(json.get("biome").getAsString()), json.get("surface_rule").getAsJsonObject());
        }
    }


    public enum Mode {
        VANILLA,
        DEFAULT,
        CUSTOM,
        NULLSCAPE
    }
}
