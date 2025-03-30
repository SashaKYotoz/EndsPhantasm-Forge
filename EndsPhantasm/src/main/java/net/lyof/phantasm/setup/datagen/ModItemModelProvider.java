package net.lyof.phantasm.setup.datagen;

import net.lyof.phantasm.Phantasm;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Phantasm.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
