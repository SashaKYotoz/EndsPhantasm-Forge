package net.lyof.phantasm.entity.access;

import net.lyof.phantasm.block.challenge.Challenge;
import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Challenger {
    static Challenger get(UUID uuid, Level level) {
        return (Challenger) level.getPlayerByUUID(uuid);
    }

    Player asPlayer();

    @Nullable ChallengeRuneBlockEntity getChallengeRune();
    void setChallengeRune(ChallengeRuneBlockEntity rune);

    default boolean isInRange() {
        return this.isInRange(this.getChallengeRune());
    }

    default boolean isInRange(ChallengeRuneBlockEntity center) {
        if (center == null) return false;
        if (center != this.getChallengeRune()) return false;

        Vec3 self = this.asPlayer().getEyePosition();
        Vec3 rune = center.getBlockPos().above((int) Challenge.R/2).getCenter();
        return Math.abs(self.x - rune.x) < Challenge.R
                && Math.abs(self.y - rune.y) < Challenge.R
                && Math.abs(self.z - rune.z) < Challenge.R;
    }
}
