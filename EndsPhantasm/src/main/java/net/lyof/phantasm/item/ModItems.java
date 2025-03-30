package net.lyof.phantasm.item;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entities.ModEntities;
import net.lyof.phantasm.item.custom.ChoralArrowItem;
import net.lyof.phantasm.item.custom.ShatteredPendantItem;
import net.lyof.phantasm.setup.ModSounds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Phantasm.MOD_ID);
    public static final RegistryObject<Item> PREAM_BERRY = ITEMS.register("pream_berry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().nutrition(4).effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0, true, false), 1).build())));
    public static final RegistryObject<Item> OBLIFRUIT = ITEMS.register("oblifruit", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().nutrition(6).saturationMod(1).build())));
    public static final RegistryObject<Item> CRYSTALLINE_SHOVEL = ITEMS.register("crystalline_shovel", () -> new ShovelItem(ModTiers.CRYSTALLINE, 2, -3f, new Item.Properties()));
    public static final RegistryObject<Item> CRYSTALLINE_PICKAXE = ITEMS.register("crystalline_pickaxe", () -> new PickaxeItem(ModTiers.CRYSTALLINE, 2, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> CRYSTALLINE_AXE = ITEMS.register("crystalline_axe", () -> new AxeItem(ModTiers.CRYSTALLINE, 7, -3f, new Item.Properties()));
    public static final RegistryObject<Item> CRYSTALLINE_HOE = ITEMS.register("crystalline_hoe", () -> new HoeItem(ModTiers.CRYSTALLINE, 0, -3f, new Item.Properties()));
    public static final RegistryObject<Item> CRYSTALLINE_SWORD = ITEMS.register("crystalline_sword", () -> new SwordItem(ModTiers.CRYSTALLINE, 4, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> PREAM_SIGN = ITEMS.register("pream_sign", () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.PREAM_SIGN.get(), ModBlocks.PREAM_WALL_SIGN.get()));
    public static final RegistryObject<Item> PREAM_HANGING_SIGN = ITEMS.register("pream_hanging_sign", () -> new HangingSignItem(ModBlocks.PREAM_HANGING_SIGN.get(), ModBlocks.PREAM_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> TALL_VIVID_NIHILIS = ITEMS.register("tall_vivid_nihilis", () -> new DoubleHighBlockItem(ModBlocks.TALL_VIVID_NIHILIS.get(), new Item.Properties()));
    public static final RegistryObject<Item> TALL_ACIDIC_NIHILIS = ITEMS.register("tall_acidic_nihilis", () -> new DoubleHighBlockItem(ModBlocks.TALL_ACIDIC_NIHILIS.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHORUS_FRUIT_SALAD = ITEMS.register("chorus_fruit_salad", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(1.5f).build()).craftRemainder(Items.BOWL).stacksTo(ConfigEntries.chorusSaladStack)) {
        @Override
        public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
            if (!ConfigEntries.chorusSaladTp) {
                if (level instanceof ServerLevel server && entity.canChangeDimensions() && !entity.isShiftKeyDown()) {
                    ResourceKey<Level> registryKey = level.dimension() == Level.END ? Level.OVERWORLD : Level.END;
                    ServerLevel serverWorld = server.getServer().getLevel(registryKey);
                    if (serverWorld == null) {
                        return super.finishUsingItem(stack, level, entity);
                    }
                    entity.changeDimension(serverWorld);
                }
            }
            if (super.finishUsingItem(stack, level, entity).isEmpty()) return this.getCraftingRemainingItem(stack);
            return stack;
        }
    });
    public static final RegistryObject<Item> BEHEMOTH_MEAT = ITEMS.register("behemoth_meat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().meat().nutrition(6).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, true, true), 1)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0, true, true), 1).build())));
    public static final RegistryObject<Item> BEHEMOTH_STEAK = ITEMS.register("behemoth_steak", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().meat().nutrition(10).saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 1, true, true), 1)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 0, true, true), 1).build())));
    public static final RegistryObject<Item> SHATTERED_PENDANT = ITEMS.register("shattered_pendant", () -> new ShatteredPendantItem(new Item.Properties()));
    public static final RegistryObject<Item> POME_SLICE = ITEMS.register("pome_slice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().nutrition(4).saturationMod(1.3f).build())));
    public static final RegistryObject<Item> MUSIC_DISC_ABRUPTION = ITEMS.register("music_disc_abruption", () -> new RecordItem(4, () -> ModSounds.MUSIC_DISC_ABRUPTION, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 239));
    public static final RegistryObject<Item> CHORAL_ARROW = ITEMS.register("choral_arrow", () -> new ChoralArrowItem(new Item.Properties()));

    public static final RegistryObject<Item> CRYSTIE_SPAWN_EGG = ITEMS.register("crystie_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.CRYSTIE, 0xfaf0ff, 0xa0a0ff, new Item.Properties()));
    public static final RegistryObject<Item> BEHEMOTH_SPAWN_EGG = ITEMS.register("behemoth_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.BEHEMOTH, 0xafa0ff, 0x0f000f, new Item.Properties()));
}
