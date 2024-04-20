package com.xiaohunao.enemybanner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EnemyBannerConfig {
    public static ForgeConfigSpec CONFIG;

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> entityTypes;
    public static ForgeConfigSpec.IntValue globalKillCount;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> entityBlackList;

    public static Map<String,Integer> entityKillCount = Maps.newHashMap();
    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        entityTypes = builder
                .comment("Modify the number of kills required to get a flag from an entity")
                .comment("Format: modid:entity-killCount,'minecraft:zombie-50'")
                .defineList("entityTypes", List.of(), str -> {
                    if (!(str instanceof String)) {
                        return false;
                    }
                    return Pattern.matches("\\w+:\\w+-\\d+", (String)str);
                });

        globalKillCount = builder
                .comment("global kill count for all entities")
                .defineInRange("globalKillCount", 50, 1, Integer.MAX_VALUE);

        entityBlackList = builder
                .comment("Blacklist of entities that cannot be used to make flags")
                .comment("Format: modid:entity,'minecraft:zombie'")
                .defineList("entityBlackList", List.of(), str -> {
                    if (!(str instanceof String)) {
                        return false;
                    }
                    return Pattern.matches("\\w+:\\w+", (String)str);
                });

        CONFIG = builder.build();
    }

    public static void loadEntityKillCount() {
        entityKillCount.clear();
        for (String entityType : entityTypes.get()) {
            String[] split = entityType.split("-");
            int killCount = Integer.parseInt(split[1]);
            entityKillCount.put(split[0], killCount);
        }
    }

    public static int getKillCount(String entity) {
        return entityKillCount.getOrDefault(entity, globalKillCount.get());
    }

}