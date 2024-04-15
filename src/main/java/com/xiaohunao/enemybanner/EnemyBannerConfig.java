package com.xiaohunao.enemybanner;

import com.google.common.collect.Maps;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EnemyBannerConfig {
    public static ForgeConfigSpec CONFIG;

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> entityTypes;

    public static Map<String,Integer> entityKillCount = Maps.newHashMap();
    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        entityTypes = builder
                .comment("List of entity types that can be displayed on the enemy banner")
                .defineList("entityTypes", List.of("minecraft:zombie-100"), str -> {
                    if (!(str instanceof String)) {
                        return false;
                    }
                    return Pattern.matches("\\w+:\\w+-\\d+", (String)str);
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
        return entityKillCount.getOrDefault(entity, 50);
    }

}