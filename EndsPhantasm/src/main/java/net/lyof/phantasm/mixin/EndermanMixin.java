package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnderMan.class)
public abstract class EndermanMixin extends Monster {
    protected EndermanMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "isLookingAtMe", at = @At("HEAD"), cancellable = true)
    public void cancelDragonFightStare(Player player, CallbackInfoReturnable<Boolean> cir) {
        List<EnderDragon> list = player.level().getEntities(EntityTypeTest.forClass(EnderDragon.class),
                new AABB(player.getOnPos()).inflate(100), a -> true);
        if (ConfigEntries.noEndermenFight && !list.isEmpty()) cir.setReturnValue(false);
    }
}