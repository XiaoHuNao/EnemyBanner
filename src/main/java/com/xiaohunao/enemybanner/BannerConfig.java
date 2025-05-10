package com.xiaohunao.enemybanner;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
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

@EventBusSubscriber(modid = EnemyBanner.MODID, bus = EventBusSubscriber.Bus.MOD)
public class BannerConfig {
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final List<Banner> BANNERS;

    private static final List<String> DEFAULT_MONSTER_IDS = new ArrayList<>();
    private static final List<ModConfigSpec.ConfigValue<Integer>> DEFAULT_SPECIAL_MAX_LEVEL = new ArrayList<>();
    private static final List<ModConfigSpec.ConfigValue<Integer>> DEFAULT_SPECIAL_BASIC_KILLS = new ArrayList<>();

    static {
        BANNERS = new ArrayList<>();

        BuiltInRegistries.ENTITY_TYPE.stream().forEach(entityType -> {
            if (entityType.getCategory().equals(MobCategory.MONSTER)) {
                BANNERS.add(new Banner(EntityType.getKey(entityType).toString()));
            }
        });

//        BANNERS.add(new Banner("minecraft:zombie"));

        for (Banner banner : BANNERS){
            DEFAULT_MONSTER_IDS.add(banner.monsterId);
        }
    }

    private static final ModConfigSpec.IntValue MAX_LEVEL = BUILDER
            .push("banner")
            .comment(Component.translatable("config.enemybanner.comment.max_level").getString())
            .defineInRange("maxBannerLevel", 10, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue BASIC_KILLS = BUILDER
            .comment(Component.translatable("config.enemybanner.comment.basic_kills").getString())
            .defineInRange("basicKills", 50, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> MONSTER_IDS = BUILDER
            .comment(Component.translatable("config.enemybanner.comment.banners").getString())
            .defineList("monsterIds", DEFAULT_MONSTER_IDS, String::new, Objects::nonNull);

    static {
        BUILDER.push("special");
        for (Banner banner : BANNERS){
            if (banner.maxLevel != -1){
                ModConfigSpec.ConfigValue<Integer> maxLevel = BUILDER.push("maxLevel")
                        .comment(Component.translatable("config.enemybanner.comment.special.maxLevel").getString())
                        .define(banner.monsterId, banner.maxLevel);
                DEFAULT_SPECIAL_MAX_LEVEL.add(maxLevel);
                BUILDER.pop(1);
            }
            if (banner.basicKills != -1){
                ModConfigSpec.ConfigValue<Integer> basicKills = BUILDER.push("basicKills")
                        .comment(Component.translatable("config.enemybanner.comment.special.basicKills").getString())
                        .define(banner.monsterId, banner.basicKills);
                DEFAULT_SPECIAL_BASIC_KILLS.add(basicKills);
                BUILDER.pop(1);
            }
        }
        BUILDER.pop(1);
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static int maxLevel;
    public static int basicKills;
    public static Map<EntityType<?>, Banner> banners;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event){
        maxLevel = MAX_LEVEL.get();
        basicKills = BASIC_KILLS.get();
        banners = new HashMap<>();

        for(String id : MONSTER_IDS.get()){
            Optional<EntityType<?>> entityType = EntityType.byString(id);
            int bannerMaxLevel = maxLevel;
            int bannerBasicKills = basicKills;
            UnmodifiableConfig specConfig = SPEC.getValues();
            if(entityType.isPresent()){
                    if(specConfig.contains("banner.special.maxLevel." + id))
                        bannerMaxLevel = (int) ((ModConfigSpec.ConfigValue<?>)specConfig.get("banner.special.maxLevel." + id)).get();
                    if(specConfig.contains("banner.special.basicKills." + id))
                        bannerBasicKills = (int) ((ModConfigSpec.ConfigValue<?>)specConfig.get("banner.special.basicKills." + id)).get();
                banners.put(entityType.get(), new Banner(id, bannerMaxLevel, bannerBasicKills));
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
        public int maxLevel;
        public int basicKills;

        /**
         *
         * @param monsterId 对应的生物ID
         * @param maxLevel  旗帜最大等级，默认-1，值为-1时，在此忽略此配置，沿用上级配置。
         * @param basicKills 旗帜每级所需击杀数的差值，默认-1，值为-1时，在此忽略此配置，沿用上级配置。
         */
        public Banner(String monsterId, int maxLevel, int basicKills){
            this.monsterId = monsterId;
            this.maxLevel = maxLevel;
            this.basicKills = basicKills;
        }

        public Banner(String monsterId, int maxLevel){
            this(monsterId, maxLevel, -1);
        }

        public Banner(int basicKills, String monsterId){
            this(monsterId, -1, basicKills);
        }

        public Banner(String monsterId){
            this(monsterId, -1, -1);
        }

        public Banner() {

        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Banner banner = (Banner) o;
            return maxLevel == banner.maxLevel && basicKills == banner.basicKills && Objects.equals(monsterId, banner.monsterId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(monsterId, maxLevel, basicKills);
        }

        @Override
        public String toString() {
            return "Banner{" +
                    "monsterId='" + monsterId + '\'' +
                    ", maxLevel=" + maxLevel +
                    ", basicKills=" + basicKills +
                    '}';
        }
    }
}
