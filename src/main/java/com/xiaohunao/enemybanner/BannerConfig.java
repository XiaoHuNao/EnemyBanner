package com.xiaohunao.enemybanner;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@EventBusSubscriber(modid = com.xiaohunao.enemybanner.EnemyBanner.MODID, bus = EventBusSubscriber.Bus.MOD)
public class BannerConfig {
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final List<Banner> BANNERS;
    private static final List<String> DEFAULT_MONSTER_IDS = new ArrayList<>();

    public static final int DEFAULT_BASIC_KILLS = 50;

    static {
        BANNERS = new ArrayList<>();

        BuiltInRegistries.ENTITY_TYPE.stream().forEach(entityType -> {
            if (entityType.getCategory().equals(MobCategory.MONSTER)) {
                BANNERS.add(new Banner(EntityType.getKey(entityType).toString(), DEFAULT_BASIC_KILLS));
            }
        });

        for (Banner banner : BANNERS){
            DEFAULT_MONSTER_IDS.add(banner.monsterId);
        }
    }

    private static final ModConfigSpec.IntValue BASIC_KILLS = BUILDER
            .comment("每兑换一个旗帜所需要的击杀数")
            .defineInRange("basicKills", DEFAULT_BASIC_KILLS, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> MONSTER_IDS = BUILDER
            .comment("能够兑换的怪物种类")
            .defineList("monsterIds", DEFAULT_MONSTER_IDS, String::new, Objects::nonNull);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> SPECIAL_BASIC_KILLS = BUILDER
            .comment("特定怪物兑换一个旗帜所需的击杀数")
            .comment("例如：\"minecraft:zombie = 5\"")
            .defineList("special.basicKills", new ArrayList<>(), String::new, Objects::nonNull);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static int basicKills;
    public static Map<EntityType<?>, Banner> banners;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event){
        List<? extends String> specialBasicKillsList = SPECIAL_BASIC_KILLS.get();
        basicKills = BASIC_KILLS.get();
        banners = new HashMap<>();
        Map<String, Integer> specialBasicKillsMap = new HashMap<>();

        for (String specialBasicKills : specialBasicKillsList) {
            String[] split = specialBasicKills.split("=");
            specialBasicKillsMap.put(split[0].trim(), Integer.decode(split[1].trim()));
        }

        for(String id : MONSTER_IDS.get()){
            Optional<EntityType<?>> entityType = EntityType.byString(id);
            int bannerBasicKills = basicKills;
            if(entityType.isPresent()){
                if(specialBasicKillsMap.containsKey(id) && specialBasicKillsMap.get(id) > 0)
                    bannerBasicKills = specialBasicKillsMap.get(id);
                banners.put(entityType.get(), new Banner(id, bannerBasicKills));
                LOGGER.debug("add banner config:{}", banners.get(entityType.get()));
            }
            else
                LOGGER.error("cannot be resolved name: {}", id);
        }
    }

    public static boolean contains(String key){
        Optional<EntityType<?>> optionalEntityType = EntityType.byString(key);
        return optionalEntityType.filter(entityType -> banners.containsKey(entityType)).isPresent();
    }

    public static Banner getBanner(String key){
        if (contains(key)){
            return getBanner(EntityType.byString(key).get());
        }
        return null;
    }

    public static Banner getBanner(EntityType<?> entityType){
        if (banners.containsKey(entityType)) {
            return banners.get(entityType);
        }
        return null;
    }

    public static class Banner implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        public String monsterId;
        public int basicKills;

        public Banner(String monsterId, int basicKills){
            this.monsterId = monsterId;
            this.basicKills = basicKills;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Banner banner = (Banner) o;
            return basicKills == banner.basicKills && Objects.equals(monsterId, banner.monsterId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(monsterId, basicKills);
        }

        @Override
        public String toString() {
            return "Banner{" +
                    "monsterId='" + monsterId + '\'' +
                    ", basicKills=" + basicKills +
                    '}';
        }
    }
}
