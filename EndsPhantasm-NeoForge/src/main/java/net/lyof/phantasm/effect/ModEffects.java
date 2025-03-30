package net.lyof.phantasm.effect;

import net.lyof.phantasm.Phantasm;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Phantasm.MOD_ID);
    public static final DeferredHolder<MobEffect, ModMobEffect> CHARM = EFFECTS.register("charm", () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0xffffaa));
    public static final DeferredHolder<MobEffect, ModMobEffect> CORROSION = EFFECTS.register("corrosion", () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0xca2656));

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, Phantasm.MOD_ID);
    public static final DeferredHolder<Potion, Potion> CORROSION_POTION = POTIONS.register("corrosion", () -> new Potion(new MobEffectInstance(CORROSION.getDelegate(), 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_CORROSION_POTION = POTIONS.register("long_corrosion", () -> new Potion("corrosion", new MobEffectInstance(CORROSION.getDelegate(), 9600)));
    public static final DeferredHolder<Potion, Potion> STRONG_CORROSION_POTION = POTIONS.register("strong_corrosion", () -> new Potion("corrosion", new MobEffectInstance(CORROSION.getDelegate(), 9600)));

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
        POTIONS.register(bus);
    }
}