package net.lyof.phantasm.config;

import net.lyof.phantasm.Phantasm;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Set;

//following code takes its place from magic of Lyof:
//https://github.com/Lyof429/EndsPhantasm/blob/master/src/main/java/net/lyof/phantasm/setup/datagen/config/ConfiguredDataResourcePack.java
public class ConfiguredDataResourcePack implements PackResources {
    public static final ConfiguredDataResourcePack INSTANCE = new ConfiguredDataResourcePack();

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String... strings) {
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
        return null;
    }

    @Override
    public void listResources(PackType packType, String s, String s1, ResourceOutput resourceOutput) {

    }

    @Override
    public Set<String> getNamespaces(PackType packType) {
        return Set.of();
    }

    @Override
    public @Nullable <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) {
        return null;
    }

    @Override
    public String packId() {
        return Phantasm.MOD_ID + "_configured_data";
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isHidden() {
        return PackResources.super.isHidden();
    }
}
