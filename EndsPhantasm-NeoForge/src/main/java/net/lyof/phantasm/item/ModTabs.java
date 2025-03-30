package net.lyof.phantasm.item;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(bus =EventBusSubscriber.Bus.MOD)
public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Phantasm.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PHANTASM = CREATIVE_TAB.register("phantasm",()->
            CreativeModeTab.builder().title(Component.translatable("itemgroup.phantasm"))
                    .icon(() -> new ItemStack(ModBlocks.FALLEN_STAR.get()))
                    .displayItems((displayContext, entries) -> {
                        entries.accept(ModBlocks.FALLEN_STAR.get());
                        entries.accept(ModBlocks.VIVID_NIHILIUM.get());
                        entries.accept(ModBlocks.VIVID_NIHILIS.get());
                        entries.accept(ModItems.TALL_VIVID_NIHILIS.get());

                        entries.accept(ModBlocks.PREAM_SAPLING.get());
                        entries.accept(ModBlocks.PREAM_LEAVES.get());

                        entries.accept(ModBlocks.PREAM_LOG.get());
                        entries.accept(ModBlocks.PREAM_WOOD.get());
                        entries.accept(ModBlocks.STRIPPED_PREAM_LOG.get());
                        entries.accept(ModBlocks.STRIPPED_PREAM_WOOD.get());

                        entries.accept(ModBlocks.PREAM_PLANKS.get());
                        entries.accept(ModBlocks.PREAM_STAIRS.get());
                        entries.accept(ModBlocks.PREAM_SLAB.get());
                        entries.accept(ModBlocks.PREAM_DOOR.get());
                        entries.accept(ModBlocks.PREAM_TRAPDOOR.get());
                        entries.accept(ModBlocks.PREAM_FENCE.get());
                        entries.accept(ModBlocks.PREAM_FENCE_GATE.get());

                        entries.accept(ModBlocks.PREAM_PRESSURE_PLATE.get());
                        entries.accept(ModBlocks.PREAM_BUTTON.get());
                        entries.accept(ModItems.PREAM_SIGN.get());
                        entries.accept(ModItems.PREAM_HANGING_SIGN.get());

                        entries.accept(ModItems.PREAM_BERRY.get());
                        entries.accept(ModItems.CHORUS_FRUIT_SALAD.get());

                        entries.accept(ModBlocks.OBLIVINE.get());
                        entries.accept(ModItems.OBLIFRUIT.get());
                        entries.accept(ModBlocks.OBLIVION.get());
                        entries.accept(ModBlocks.CRYSTALILY.get());

                        entries.accept(ModBlocks.PURPUR_LAMP.get());
                        entries.accept(ModBlocks.STARFLOWER.get());
                        entries.accept(ModBlocks.RAW_PURPUR.get());
                        entries.accept(ModBlocks.RAW_PURPUR_BRICKS.get());
                        entries.accept(ModBlocks.RAW_PURPUR_PILLAR.get());
                        entries.accept(ModBlocks.RAW_PURPUR_TILES.get());
                        entries.accept(ModBlocks.RAW_PURPUR_BRICKS_STAIRS.get());
                        entries.accept(ModBlocks.RAW_PURPUR_BRICKS_SLAB.get());

                        entries.accept(ModBlocks.CRYSTAL_SHARD.get());
                        entries.accept(ModBlocks.VOID_CRYSTAL_SHARD.get());

                        entries.accept(ModBlocks.CRYSTAL_BLOCK.get());
                        entries.accept(ModBlocks.VOID_CRYSTAL_BLOCK.get());

                        entries.accept(ModItems.CRYSTALLINE_SWORD.get());
                        entries.accept(ModItems.CRYSTALLINE_SHOVEL.get());
                        entries.accept(ModItems.CRYSTALLINE_PICKAXE.get());
                        entries.accept(ModItems.CRYSTALLINE_AXE.get());
                        entries.accept(ModItems.CRYSTALLINE_HOE.get());


                        entries.accept(ModBlocks.CRYSTAL_TILES.get());
                        entries.accept(ModBlocks.CRYSTAL_PILLAR.get());
                        entries.accept(ModBlocks.CRYSTAL_TILES_STAIRS.get());
                        entries.accept(ModBlocks.CRYSTAL_TILES_SLAB.get());

                        entries.accept(ModBlocks.VOID_CRYSTAL_TILES.get());
                        entries.accept(ModBlocks.VOID_CRYSTAL_PILLAR.get());
                        entries.accept(ModBlocks.VOID_CRYSTAL_TILES_STAIRS.get());
                        entries.accept(ModBlocks.VOID_CRYSTAL_TILES_SLAB.get());

                        entries.accept(ModBlocks.CRYSTAL_GLASS.get());
                        entries.accept(ModBlocks.CRYSTAL_GLASS_PANE.get());
                        entries.accept(ModBlocks.VOID_CRYSTAL_GLASS.get());
                        entries.accept(ModBlocks.VOID_CRYSTAL_GLASS_PANE.get());

                        entries.accept(ModBlocks.POLISHED_OBSIDIAN.get());
                        entries.accept(ModBlocks.POLISHED_OBSIDIAN_BRICKS.get());
                        entries.accept(ModBlocks.POLISHED_OBSIDIAN_BRICKS_STAIRS.get());
                        entries.accept(ModBlocks.POLISHED_OBSIDIAN_BRICKS_SLAB.get());
                        entries.accept(ModItems.BEHEMOTH_MEAT.get());
                        entries.accept(ModItems.BEHEMOTH_STEAK.get());
                        entries.accept(ModItems.SHATTERED_PENDANT.get());
                        entries.accept(ModItems.CRYSTIE_SPAWN_EGG.get());
                        entries.accept(ModItems.BEHEMOTH_SPAWN_EGG.get());
                        entries.accept(ModBlocks.ACIDIC_NIHILIUM.get());
                        entries.accept(ModBlocks.ACIDIC_NIHILIS.get());
                        entries.accept(ModItems.TALL_ACIDIC_NIHILIS.get());
                        entries.accept(ModBlocks.DRAGON_MINT.get());

                        entries.accept(ModBlocks.DRALGAE.get());
                        entries.accept(ModBlocks.POME.get());
                        entries.accept(ModItems.POME_SLICE.get());

                        entries.accept(ModBlocks.ACIDIC_MASS.get());

                        entries.accept(ModBlocks.CIRITE.get());
                        entries.accept(ModBlocks.CIRITE_IRON_ORE.get());
                        entries.accept(ModBlocks.CIRITE_BRICKS.get());
                        entries.accept(ModBlocks.CIRITE_BRICKS_STAIRS.get());
                        entries.accept(ModBlocks.CIRITE_BRICKS_SLAB.get());

                        entries.accept(ModBlocks.CHORAL_BLOCK.get());
                        entries.accept(ModBlocks.CHORAL_FAN.get());
                        entries.accept(ModBlocks.SUBWOOFER_BLOCK.get());

                        entries.accept(ModItems.CHORAL_ARROW.get());

                        entries.accept(ModItems.MUSIC_DISC_ABRUPTION.get());
                    }).build());

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
        if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            tabData.accept(ModItems.CRYSTIE_SPAWN_EGG.get());
            tabData.accept(ModItems.BEHEMOTH_SPAWN_EGG.get());
        }
    }
}
