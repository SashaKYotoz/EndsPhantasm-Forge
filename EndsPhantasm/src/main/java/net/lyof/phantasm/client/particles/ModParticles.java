package net.lyof.phantasm.client.particles;

import net.lyof.phantasm.Phantasm;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Phantasm.MOD_ID);
    public static final RegistryObject<SimpleParticleType> ZZZ = PARTICLE_TYPES.register("zzz",()->new SimpleParticleType(true));
}
