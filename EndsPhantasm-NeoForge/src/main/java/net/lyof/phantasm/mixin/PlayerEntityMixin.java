package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	public PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(at = @At("RETURN"), method = "getDigSpeed", cancellable = true,remap = false)
	private void modifyBreakSpeed(BlockState state, BlockPos pos, CallbackInfoReturnable<Float> cir) {
		Player player = ((Player) (Object) this);
		ItemStack stack = player.getMainHandItem();
		if (!stack.is(ModTags.Items.XP_BOOSTED) || !stack.getItem().isCorrectToolForDrops(stack,state)) return;
		float bonus = 1;
		bonus += (float) (ConfigEntries.crystalXPBoost * player.experienceLevel / 50f);
		cir.setReturnValue(cir.getReturnValue() * bonus);
	}
	@Inject(method = "getSpeed", at = @At("HEAD"), cancellable = true)
	public void charmMovementSpeed(CallbackInfoReturnable<Float> cir) {
		if (this.hasEffect(ModEffects.CHARM)) cir.setReturnValue(0f);
	}
}