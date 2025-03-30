package net.lyof.phantasm.item;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entities.ModEntities;
import net.lyof.phantasm.item.components.ShatteredPendantData;
import net.lyof.phantasm.item.custom.ChoralArrowItem;
import net.lyof.phantasm.item.custom.ShatteredPendantItem;
import net.lyof.phantasm.setup.ModSounds;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModItems {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Phantasm.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ShatteredPendantData>> SHATTERED_PENDANT_DATA = register("shattered_pendant_data",
            builder -> builder.persistent(ShatteredPendantData.CODEC));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Phantasm.MOD_ID);
    public static final DeferredHolder<Item, Item> PREAM_BERRY = ITEMS.register("pream_berry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(4).effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0, true, false), 1).build())));
    public static final DeferredHolder<Item, Item> OBLIFRUIT = ITEMS.register("oblifruit", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(6).saturationModifier(1).build())));
    public static final DeferredHolder<Item, ShovelItem> CRYSTALLINE_SHOVEL = ITEMS.register("crystalline_shovel", () -> new ShovelItem(ModTiers.CRYSTALLINE, new Item.Properties().attributes(ShovelItem.createAttributes(ModTiers.CRYSTALLINE, 1.5F, -3.0f))));
    public static final DeferredHolder<Item, PickaxeItem> CRYSTALLINE_PICKAXE = ITEMS.register("crystalline_pickaxe", () -> new PickaxeItem(ModTiers.CRYSTALLINE, new Item.Properties().attributes(PickaxeItem.createAttributes(ModTiers.CRYSTALLINE, 1.0F, -2.8f))));
    public static final DeferredHolder<Item, AxeItem> CRYSTALLINE_AXE = ITEMS.register("crystalline_axe", () -> new AxeItem(ModTiers.CRYSTALLINE, new Item.Properties().attributes(AxeItem.createAttributes(ModTiers.CRYSTALLINE, 6.0F, -3.1f))));
    public static final DeferredHolder<Item, HoeItem> CRYSTALLINE_HOE = ITEMS.register("crystalline_hoe", () -> new HoeItem(ModTiers.CRYSTALLINE, new Item.Properties().attributes(HoeItem.createAttributes(ModTiers.CRYSTALLINE, 0F, -3.0f))));
    public static final DeferredHolder<Item, SwordItem> CRYSTALLINE_SWORD = ITEMS.register("crystalline_sword", () -> new SwordItem(ModTiers.CRYSTALLINE, new Item.Properties().attributes(SwordItem.createAttributes(ModTiers.CRYSTALLINE, 5, -2.4f))));
    public static final DeferredHolder<Item, SignItem> PREAM_SIGN = ITEMS.register("pream_sign", () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.PREAM_SIGN.get(), ModBlocks.PREAM_WALL_SIGN.get()));
    public static final DeferredHolder<Item, HangingSignItem> PREAM_HANGING_SIGN = ITEMS.register("pream_hanging_sign", () -> new HangingSignItem(ModBlocks.PREAM_HANGING_SIGN.get(), ModBlocks.PREAM_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, DoubleHighBlockItem> TALL_VIVID_NIHILIS = ITEMS.register("tall_vivid_nihilis", () -> new DoubleHighBlockItem(ModBlocks.TALL_VIVID_NIHILIS.get(), new Item.Properties()));
    public static final DeferredItem<DoubleHighBlockItem> TALL_ACIDIC_NIHILIS = ITEMS.register("tall_acidic_nihilis", () -> new DoubleHighBlockItem(ModBlocks.TALL_ACIDIC_NIHILIS.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> CHORUS_FRUIT_SALAD = ITEMS.register("chorus_fruit_salad", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(1.5f).build()).craftRemainder(Items.BOWL).stacksTo(ConfigEntries.chorusSaladStack)) {
        @Override
        public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
            if (ConfigEntries.chorusSaladTp) {
                if (level instanceof ServerLevel server && !entity.isShiftKeyDown()) {
                    ResourceKey<Level> registryKey = level.dimension() == Level.END ? Level.OVERWORLD : Level.END;
                    ServerLevel serverWorld = server.getServer().getLevel(registryKey);
                    if (serverWorld == null || entity.canChangeDimensions(level, serverWorld)) {
                        return super.finishUsingItem(stack, level, entity);
                    }
                    entity.changeDimension(new DimensionTransition(serverWorld, entity, DimensionTransition.PLACE_PORTAL_TICKET));
                }
            }
            if (super.finishUsingItem(stack, level, entity).isEmpty()) return this.getCraftingRemainingItem(stack);
            return stack;
        }
    });
    public static final DeferredHolder<Item, Item> BEHEMOTH_MEAT = ITEMS.register("behemoth_meat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(6).saturationModifier(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, true, true), 1)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0, true, true), 1).build())));
    public static final DeferredHolder<Item, Item> BEHEMOTH_STEAK = ITEMS.register("behemoth_steak", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(6).saturationModifier(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 1, true, true), 1)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 0, true, true), 1).build())));

    public static final DeferredItem<ShatteredPendantItem> SHATTERED_PENDANT = ITEMS.register("shattered_pendant", () -> new ShatteredPendantItem(new Item.Properties().component(SHATTERED_PENDANT_DATA.get(), new ShatteredPendantData(0, 0, 0, "minecraft:overworld"))));
    public static final DeferredItem<Item> POME_SLICE = ITEMS.register("pome_slice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(1.3f).build())));
    public static final DeferredItem<Item> MUSIC_DISC_ABRUPTION = ITEMS.register("music_disc_abruption", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(ModSounds.ABRUPTION)));
    public static final DeferredItem<ChoralArrowItem> CHORAL_ARROW = ITEMS.register("choral_arrow", () -> new ChoralArrowItem(new Item.Properties()));
    public static final DeferredItem<Item> CRYSTIE_SPAWN_EGG = ITEMS.register("crystie_spawn_egg", () -> new SpawnEggItem(ModEntities.CRYSTIE.get(), 0xfaf0ff, 0xa0a0ff, new Item.Properties()));
    public static final DeferredItem<Item> BEHEMOTH_SPAWN_EGG = ITEMS.register("behemoth_spawn_egg", () -> new SpawnEggItem(ModEntities.BEHEMOTH.get(), 0xafa0ff, 0x0f000f, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
        ITEMS.register(eventBus);
    }
}