package net.lyof.phantasm.block.challenge;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;

public class Challenge {
    public static float R = 15.99f;

    public boolean dataDriven = false;

    public final ResourceLocation id;
    public final ResourceLocation lootTable;
    private final List<ChallengeMonster> monsters;
    public final int monsterObjective;
    public final int levelCost;
    public final boolean postDragon;
    private final int totalWeight;

    public Challenge(ResourceLocation id, ResourceLocation lootTable, List<ChallengeMonster> monsters, int monsterObjective,
                     int levelCost, boolean postDragon) {
        this.id = id;
        this.lootTable = lootTable;
        this.monsters = monsters;
        this.monsterObjective = monsterObjective;
        this.totalWeight = this.monsters.stream().reduce(0, (sum, monster) -> sum + monster.weight, Integer::sum);
        this.levelCost = levelCost;
        this.postDragon = postDragon;
    }

    public Challenge setDataDriven(boolean value) {
        this.dataDriven = value;
        return this;
    }

    public void spawnMonster(ChallengeRuneBlockEntity rune) {
        int select = rune.getLevel().getRandom().nextInt(this.totalWeight);
        for (ChallengeMonster monster : this.monsters) {
            select -= monster.weight;
            if (select < 0) {
                LivingEntity entity = monster.create(rune);
                rune.getLevel().addFreshEntity(entity);
                return;
            }
        }
    }

    public void spawnLoot(ChallengeRuneBlockEntity rune) {
        if (!(rune.getLevel() instanceof ServerLevel world)) return;

        world.getServer().getLootData().getLootTable(this.lootTable)
                .getRandomItems(new LootContext.Builder(new LootParams.Builder(world)
                                .withParameter(LootContextParams.ORIGIN, rune.getBlockPos().getCenter()).create(LootContextParamSets.CHEST))
                                .create(null),
                        stack -> world.addFreshEntity(new ItemEntity(world, rune.getBlockPos().getX() + 0.5,
                                rune.getBlockPos().getY() + 1, rune.getBlockPos().getZ() + 0.5, stack)));
    }


    public static void read(ResourceLocation id, JsonObject json) {
        if (json.has("loot_table") && json.has("monsters") && json.has("objective")
                && json.has("level_cost") && json.has("post_dragon")) {

            List<ChallengeMonster> monsters = new ArrayList<>();
            for (JsonElement elmt : json.getAsJsonArray("monsters"))
                ChallengeMonster.read(elmt.getAsJsonObject(), monsters);

            ChallengeRegistry.register(id, new Challenge(
                    id,
                    ResourceLocation.parse(json.get("loot_table").getAsString()),
                    monsters,
                    json.get("objective").getAsInt(),
                    json.get("level_cost").getAsInt(),
                    json.get("post_dragon").getAsBoolean()
            ).setDataDriven(true));
        }
    }
}