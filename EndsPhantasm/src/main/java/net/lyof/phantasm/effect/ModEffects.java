package net.lyof.phantasm.effect;

import net.lyof.phantasm.Phantasm;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Phantasm.MOD_ID);
    public static final RegistryObject<MobEffect> CHARM = EFFECTS.register("charm", () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0xffffaa));
    public static final RegistryObject<MobEffect> CORROSION = EFFECTS.register("corrosion", () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0xca2656));

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Phantasm.MOD_ID);
    public static final RegistryObject<Potion> CORROSION_POTION = POTIONS.register("corrosion", () -> new Potion(new MobEffectInstance(CORROSION.get(), 3600)));
    public static final RegistryObject<Potion> LONG_CORROSION_POTION = POTIONS.register("long_corrosion", () -> new Potion("corrosion", new MobEffectInstance(CORROSION.get(), 9600)));
    public static final RegistryObject<Potion> STRONG_CORROSION_POTION = POTIONS.register("strong_corrosion", () -> new Potion("corrosion", new MobEffectInstance(CORROSION.get(), 9600)));

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
        POTIONS.register(bus);
    }
}