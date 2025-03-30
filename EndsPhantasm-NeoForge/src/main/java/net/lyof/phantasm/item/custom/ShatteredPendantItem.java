package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.item.components.ShatteredPendantData;
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
        if (entity.tickCount % 20 == 0 && entity.onGround() && canSeeSky(entity.getOnPos(), level))
            updateLocation(stack,entity.getBlockX(),entity.getBlockY(),entity.getBlockZ(),level.dimension().toString());
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

    private void updateLocation(ItemStack stack, int x, int y, int z, String id) {
        stack.set(ModItems.SHATTERED_PENDANT_DATA.get(), new ShatteredPendantData(x, y, z, id));
    }
    private ShatteredPendantData getData(ItemStack stack){
        return stack.get(ModItems.SHATTERED_PENDANT_DATA.get());
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 30;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (getData(stack).x() == 0 && getData(stack).y() == 0 && getData(stack).z() == 0) {
            Phantasm.log("naah");
            return super.finishUsingItem(stack, level, user);
        }
        if (level.dimension().toString().equals(getData(stack).dimensionId())) {
            user.fallDistance = 0;
            user.teleportTo(getData(stack).x(), getData(stack).y(), getData(stack).z());
            if (user instanceof Player player) {
                player.getCooldowns().addCooldown(this, 200);
                stack.hurtAndBreak(1, player, user.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
            user.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1, 2);
        }
        return super.finishUsingItem(stack, level, user);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        String[] txt = Component.translatable("item.phantasm.shattered_pendant.desc").getString().split("\\n");
        for (String t : txt)
            tooltipComponents.add(Component.literal(t).withStyle(ChatFormatting.GRAY));
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
