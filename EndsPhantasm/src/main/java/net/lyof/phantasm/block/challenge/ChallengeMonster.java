package net.lyof.phantasm.block.challenge;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.lyof.phantasm.entity.access.Challenger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Vex;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallengeMonster {
    public final int weight;

    private final EntityType<? extends LivingEntity> entityType;
    private final Map<Attribute, Float> attributeMultipliers;

    public ChallengeMonster(int weight, EntityType<? extends LivingEntity> entityType, Map<Attribute, Float> attributeMultipliers) {
        this.weight = weight;
        this.entityType = entityType;
        this.attributeMultipliers = attributeMultipliers;
    }

    public LivingEntity create(ChallengeRuneBlockEntity rune) {
        LivingEntity entity = this.entityType.create(rune.getLevel());
        entity.setPos(rune.getBlockPos().above().getCenter().add(0, -0.5, 0));

        for (Map.Entry<Attribute, Float> entry : this.attributeMultipliers.entrySet())
            entity.getAttribute(entry.getKey()).addPermanentModifier(new AttributeModifier(
                    "Challenge bonus", entry.getValue(), AttributeModifier.Operation.MULTIPLY_BASE));
        entity.setHealth(entity.getMaxHealth());

        entity.addTag(Phantasm.MOD_ID + ".challenge");
        ((Challenger) entity).setChallengeRune(rune);
        if (entity instanceof Mob mob)
            mob.setPersistenceRequired();
        if (entity instanceof Vex vex)
            vex.setBoundOrigin(rune.getBlockPos().above(5));
        if (entity instanceof Slime slime)
            slime.setSize((int) Math.pow(2, rune.getLevel().getRandom().nextInt(3)), true);

        return entity;
    }

    @SuppressWarnings("unchecked")
    public static void read(JsonObject json, List<ChallengeMonster> monsters) {
        if (json.has("entity")) {
            EntityType<?> entity = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(json.get("entity").getAsString()));
            if (entity == EntityType.PIG && !json.get("entity").getAsString().equals("minecraft:pig")) return;
            try { EntityType<? extends LivingEntity> e = (EntityType<? extends LivingEntity>) entity; }
            catch (Exception ignored) { return; }

            JsonObject attributesObject = json.has("attributes")
                    ? json.getAsJsonObject("attributes") : new JsonObject();
            Map<Attribute, Float> attributes = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : attributesObject.entrySet()) {
                if (!entry.getValue().isJsonPrimitive() || !entry.getValue().getAsJsonPrimitive().isNumber())
                    continue;

                Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.parse(entry.getKey()));
                if (attr == null) continue;

                attributes.putIfAbsent(attr, entry.getValue().getAsFloat());
            }

            monsters.add(new ChallengeMonster(
                    json.has("weight") ? json.get("weight").getAsInt() : 1,
                    (EntityType<? extends LivingEntity>) entity,
                    attributes
            ));
        }
    }
}
