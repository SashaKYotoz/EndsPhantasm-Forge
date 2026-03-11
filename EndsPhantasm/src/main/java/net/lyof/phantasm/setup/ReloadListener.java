package net.lyof.phantasm.setup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.challenge.Challenge;
import net.lyof.phantasm.block.challenge.ChallengeRegistry;
import net.lyof.phantasm.config.ModConfig;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.screen.DiscVisuals;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.Map;

public class ReloadListener {
    public static final ReloadListener INSTANCE = new ReloadListener();

    public void preload(ResourceManager manager) {
        ModConfig.register();

        EndDataCompat.clear();
        ChallengeRegistry.clear();
        PolyppieEntity.Variant.clear();

        for (Map.Entry<ResourceLocation, Resource> entry : manager.listResources("worldgen/end_biomes",
                path -> path.toString().endsWith(".json")).entrySet()) {

            try {
                String content = new String(entry.getValue().open().readAllBytes());
                JsonElement json = new Gson().fromJson(content, JsonElement.class);

                if (json == null || !json.isJsonObject()) continue;

                EndDataCompat.read(json.getAsJsonObject());
            } catch (Throwable e) {
                Phantasm.log("Could not read data file " + entry.getKey(), 2);
            }
        }

        FileToIdConverter finder = FileToIdConverter.json("challenges");
        for (Map.Entry<ResourceLocation, Resource> entry : finder.listMatchingResources(manager).entrySet()) {
            try {
                String content = new String(entry.getValue().open().readAllBytes());
                JsonElement json = new Gson().fromJson(content, JsonElement.class);

                if (json == null || !json.isJsonObject()) continue;

                Challenge.read(finder.fileToId(entry.getKey()), json.getAsJsonObject());
            } catch (Throwable e) {
                Phantasm.log("Could not read data file " + entry.getKey(), 2);
            }
        }

        finder = FileToIdConverter.json("entities/polyppie_variants");
        for (Map.Entry<ResourceLocation, Resource> entry : finder.listMatchingResources(manager).entrySet()) {
            try {
                String content = new String(entry.getValue().open().readAllBytes());
                JsonElement json = new Gson().fromJson(content, JsonElement.class);

                if (json == null || !json.isJsonObject()) continue;

                PolyppieEntity.Variant.read(finder.fileToId(entry.getKey()), json.getAsJsonObject());
            } catch (Throwable e) {
                Phantasm.log("Could not read data file " + entry.getKey(), 2);
            }
        }
    }

    public void reloadClient() {
        PolyppieEntity.Variant.clear();

        DiscVisuals.load();
    }


    public static class Client implements ResourceManagerReloadListener {
        @Override
        public void onResourceManagerReload(ResourceManager manager) {
            DiscVisuals.clear();

            FileToIdConverter finder = FileToIdConverter.json("disc_visuals");
            for (Map.Entry<ResourceLocation, Resource> entry : finder.listMatchingResources(manager).entrySet()) {
                try {
                    String content = new String(entry.getValue().open().readAllBytes());
                    JsonElement json = new Gson().fromJson(content, JsonElement.class);

                    if (json == null || !json.isJsonObject()) continue;

                    DiscVisuals.read(json.getAsJsonObject());
                } catch (Throwable e) {
                    Phantasm.log("Could not read resource file " + entry.getKey(), 2);
                }
            }
        }
    }
}
