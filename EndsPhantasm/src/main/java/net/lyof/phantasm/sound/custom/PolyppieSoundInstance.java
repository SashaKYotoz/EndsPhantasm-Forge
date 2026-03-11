package net.lyof.phantasm.sound.custom;


import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.world.entity.Entity;

public interface PolyppieSoundInstance extends TickableSoundInstance {
    void update(Entity entity);

    void setFinished();

    Entity getEntity();

    void setPos(double x, double y, double z);

    default void tick() {
        Entity entity = this.getEntity();
        if (!entity.isRemoved() || entity.getRemovalReason() == Entity.RemovalReason.UNLOADED_WITH_PLAYER) {
            this.setPos(entity.getX(), entity.getY(), entity.getZ());
        }
    }

    default boolean canPlay() {
        return !this.getEntity().isSilent();
    }
}
