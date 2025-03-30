package net.lyof.phantasm.setup.datagen;

import net.lyof.phantasm.Phantasm;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockModelProvider extends BlockStateProvider {
    public ModBlockModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Phantasm.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
