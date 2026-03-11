package net.lyof.phantasm.screen;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public record DiscVisuals(ResourceLocation progressBar, ResourceLocation notes) {

    private static final DiscVisuals DEFAULT = new DiscVisuals(ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/polyppie/progress_bar.png"),
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/polyppie/notes.png"));
    private static final Map<ResourceLocation, DiscVisuals> toLoad = new HashMap<>();
    private static final Map<Item, DiscVisuals> instances = new HashMap<>();

    public static void clear() {
        toLoad.clear();
        instances.clear();
    }

    public static void load() {
        instances.clear();
        for (Map.Entry<ResourceLocation, DiscVisuals> entry : toLoad.entrySet()) {
            Item item = BuiltInRegistries.ITEM.get(entry.getKey());
            if (item == Items.AIR) continue;

            instances.put(item, entry.getValue());
        }
    }

    public static void read(JsonObject json) {
        if (!json.has("disc")) return;

        ResourceLocation disc = ResourceLocation.parse(json.get("disc").getAsString());

        ResourceLocation progressBar = DEFAULT.progressBar;
        ResourceLocation notes = DEFAULT.notes;
        if (json.has("progress_bar"))
            progressBar = ResourceLocation.parse(json.get("progress_bar").getAsString());
        if (json.has("notes"))
            notes = ResourceLocation.parse(json.get("notes").getAsString());

        toLoad.putIfAbsent(disc, new DiscVisuals(progressBar, notes));
    }

    public static DiscVisuals get(ItemStack stack) {
        return instances.getOrDefault(stack.getItem(), DEFAULT);
    }
}
