package net.lyof.phantasm.entity.listeners;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

public class BehemothEventListener implements GameEventListener {
    private final BehemothEntity behemoth;

    public BehemothEventListener(BehemothEntity behemoth) {
        this.behemoth = behemoth;
    }

    @Override
    public PositionSource getListenerSource() {
        return new EntityPositionSource(behemoth, behemoth.getEyeHeight());
    }

    @Override
    public int getListenerRadius() {
        return ConfigEntries.behemothAggroRange;
    }

    @Override
    public boolean handleGameEvent(ServerLevel level, GameEvent event, GameEvent.Context emitter, Vec3 vec3) {
        if (event.is(ModTags.GameEvents.BEHEMOTH_CAN_LISTEN) && !(emitter.sourceEntity() instanceof BehemothEntity)) {
            if (emitter.sourceEntity() instanceof Player player && !player.isCreative() && !player.isSpectator()) {
                if (player.isCrouching()) {
                    if (player.distanceTo(behemoth) < ConfigEntries.behemothSneakAggroRange)
                        behemoth.setTarget(player);
                } else
                    behemoth.setTarget(player);
            }
            return true;
        }
        return false;
    }
}