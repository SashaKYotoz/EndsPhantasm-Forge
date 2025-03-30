package net.lyof.phantasm.setup;


import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.setup.datagen.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;
@Mod.EventBusSubscriber(modid = Phantasm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		BlockTagsProvider blocktags = new ModBlockTagProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
		event.getGenerator().addProvider(event.includeServer(), blocktags);
		generator.addProvider(true,ModLootTableProvider.create(packOutput));
		generator.addProvider(event.includeServer(), new ModRegistryProvider(packOutput, lookupProvider));
		generator.addProvider(true,new ModBlockTagProvider(packOutput,lookupProvider,existingFileHelper));
	}
}
