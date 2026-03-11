package net.lyof.phantasm.sound.custom;


import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class PolyppieDiscSoundInstance extends AbstractSoundInstance implements PolyppieSoundInstance {
    protected Entity entity;
    protected boolean done;

    public PolyppieDiscSoundInstance(SoundEvent sound, float pitch, Entity entity, long seed) {
        super(sound, SoundSource.RECORDS, RandomSource.create(seed));
        this.volume = 4;
        this.pitch = pitch;
        this.entity = entity;
        this.setPos(this.entity.getX(), this.entity.getY(), this.entity.getZ());
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public void setPos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void update(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void setFinished() {
        this.done = true;
        this.looping = false;
    }

    @Override
    public boolean isStopped() {
        return this.done;
    }
}
