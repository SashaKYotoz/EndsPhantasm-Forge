package net.lyof.phantasm.client.particles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ZzzParticle extends TextureSheetParticle {
    public ZzzParticle(ClientLevel clientWorld, double x, double y, double z) {
        super(clientWorld, x, y, z);
    }

    public ZzzParticle(ClientLevel clientWorld, double x, double y, double z, SpriteSet sprite, float vx, float vy, float vz) {
        super(clientWorld, x, y, z);

        this.setSpriteFromAge(sprite);

        this.setPos(x, y, z);
        this.setParticleSpeed(vx, vy, vz);

        this.gravity = 0;
        this.lifetime = 100;
    }

    @Override
    public void tick() {
        float ratio = (float) (this.lifetime - this.age) / this.lifetime;

        //this.scale = ;
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
    public static @NotNull Provider provider(SpriteSet spriteSet) {
        return new Provider(spriteSet);
    }
    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteProvider) {
            this.sprites = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ZzzParticle(level, x, y, z, this.sprites, (float) velocityX, (float) velocityY, (float) velocityZ);
        }
    }
}