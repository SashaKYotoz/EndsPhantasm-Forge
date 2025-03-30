package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShatteredPendantItem extends Item {
    public ShatteredPendantItem(Properties properties) {
        super(properties.rarity(Rarity.RARE).stacksTo(1).durability(ConfigEntries.shatteredPendantDurability).fireResistant());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x349988;
    }

    public static boolean canSeeSky(BlockPos pos, Level level) {
        int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
        return top <= pos.getY();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (entity.tickCount % 20 == 0 && entity.onGround() && canSeeSky(entity.getOnPos(), level)) {
            stack.getOrCreateTag().putInt("SavedX", entity.getBlockX());
            stack.getOrCreateTag().putInt("SavedY", entity.getBlockY());
            stack.getOrCreateTag().putInt("SavedZ", entity.getBlockZ());
            stack.getOrCreateTag().putString("SavedDim", level.dimension().toString());
        }
        else if (entity instanceof LivingEntity living && entity.tickCount % 20 == 0 && entity.getY() <= 0
                && level.dimension().location().toString().equals("minecraft:the_end")
                && (!(entity instanceof Player player) || !player.getCooldowns().isOnCooldown(this))) {
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0, true, false));
            this.finishUsingItem(stack, level, living);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return super.use(level, player, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 30;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.getInt("SavedX") == 0 && nbt.getInt("SavedY") == 0 && nbt.getInt("SavedZ") == 0) {
            Phantasm.log("naah");
            return super.finishUsingItem(stack, level, user);
        }
        if (level.dimension().toString().equals(nbt.getString("SavedDim"))) {
            user.fallDistance = 0;
            user.teleportTo(nbt.getInt("SavedX"), nbt.getInt("SavedY"), nbt.getInt("SavedZ"));
            if (user instanceof Player player) {
                player.getCooldowns().addCooldown(this, 200);
                stack.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(
                        user.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
            }
            user.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1, 2);
        }
        return super.finishUsingItem(stack, level, user);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, components, tooltipFlag);
        String[] txt = Component.translatable("item.phantasm.shattered_pendant.desc").getString().split("\\n");
        for (String t : txt)
            components.add(Component.literal(t).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.onUseTick(level, user, stack, remainingUseTicks);
        float sin = (float) Math.sin(remainingUseTicks * Math.PI / 10);
        float cos = (float) Math.cos(remainingUseTicks * Math.PI / 10);
        level.addParticle(ParticleTypes.END_ROD, user.getX() + sin, user.getEyeY() - 0.5, user.getZ() + cos, 0, 0, 0);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }
}
