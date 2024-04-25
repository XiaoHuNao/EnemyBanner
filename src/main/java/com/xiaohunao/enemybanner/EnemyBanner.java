package com.xiaohunao.enemybanner;

import com.google.common.collect.Maps;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

@Mod(EnemyBanner.MOD_ID)
public class EnemyBanner {
    public static final String MOD_ID = "enemybanner";
    public static final LinkedHashMap<EntityType<?>, EntityBannerPattern> ENTITY_BANNER_PATTERNS = Maps.newLinkedHashMap();
    public static final LinkedHashMap<EntityType<?>,LivingEntity> RENDER_ENTITY_CACHE = Maps.newLinkedHashMap();
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EnemyBanner.MOD_ID);
    public static final DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister.create(Registry.BANNER_PATTERN_REGISTRY, MOD_ID);
    public static final TagKey<BannerPattern> FUNCTION_SILKS_TAG_KEY = createBannerPatternTagKey("function_silks");
    public static final TagKey<BannerPattern> COLOR_SILKS_TAG_KEY = createBannerPatternTagKey("color_silks");

    public static final CreativeModeTab CREATIVE_TABS = new CreativeModeTab("enemybanner_tab") {
        @Override
        @NotNull
        public ItemStack makeIcon() {
            return BannerUtil.appendEntityPattern(Items.WHITE_BANNER.getDefaultInstance(), ENTITY_BANNER_PATTERNS.get(EntityType.ZOMBIE), BASIC_SILKS.get(), WHITE_SILKS.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> p_40778_) {
            super.fillItemList(p_40778_);
            p_40778_.add(0,BannerUtil.appendEntityPattern(Items.WHITE_BANNER.getDefaultInstance(), ENTITY_BANNER_PATTERNS.get(EntityType.ZOMBIE), BASIC_SILKS.get(), WHITE_SILKS.get()));
        }
    };

    public static final RegistryObject<BannerPattern> BASIC_SILKS = BANNER_PATTERNS.register("basic_silks", () -> new BannerPattern("basic_silks"));
    public static final RegistryObject<BannerPatternItem> BASIC = ITEMS.register("basic_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("basic_silks"), getProperties(1)));
    public static final RegistryObject<BannerPattern> DAMAGE_SILKS = BANNER_PATTERNS.register("damage_silks", () -> new BannerPattern("damage_silks"));
    public static final RegistryObject<BannerPatternItem> DAMAGE = ITEMS.register("damage_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("damage_silks"), getProperties(1)));
    public static final RegistryObject<BannerPattern> INHIBIT_SILKS = BANNER_PATTERNS.register("inhibit_silks", () -> new BannerPattern("inhibit_silks"));
    public static final RegistryObject<BannerPatternItem> INHIBIT = ITEMS.register("inhibit_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("inhibit_silks"), getProperties(1)));
    public static final RegistryObject<BannerPattern> LOOT_SILKS = BANNER_PATTERNS.register("loot_silks", () -> new BannerPattern("loot_silks"));
    public static final RegistryObject<BannerPatternItem> LOOT = ITEMS.register("loot_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("loot_silks"), getProperties(1)));
    public static final RegistryObject<BannerPattern> PULL_SILKS = BANNER_PATTERNS.register("pull_silks", () -> new BannerPattern("pull_silks"));
    public static final RegistryObject<BannerPatternItem> PULL = ITEMS.register("pull_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("pull_silks"), getProperties(1)));
    public static final RegistryObject<BannerPattern> PUSH_SILKS = BANNER_PATTERNS.register("push_silks", () -> new BannerPattern("push_silks"));
    public static final RegistryObject<BannerPatternItem> PUSH = ITEMS.register("push_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("push_silks"),getProperties(1)));
    public static final RegistryObject<BannerPattern> RANGE_SILKS = BANNER_PATTERNS.register("range_silks", () -> new BannerPattern("range_silks"));
    public static final RegistryObject<BannerPatternItem> RANGE = ITEMS.register("range_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("range_silks"), getProperties(1)));
    public static final RegistryObject<BannerPattern> RESIST_SILKS = BANNER_PATTERNS.register("resist_silks", () -> new BannerPattern("resist_silks"));
    public static final RegistryObject<BannerPatternItem> RESIST = ITEMS.register("resist_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("resist_silks"), getProperties(1)));

    //16色的bannerSilks
    //white
    public static final RegistryObject<BannerPattern> WHITE_SILKS = BANNER_PATTERNS.register("white_silks", () -> new BannerPattern("white_silks"));
    public static final RegistryObject<BannerPattern> ORANGE_SILKS = BANNER_PATTERNS.register("orange_silks", () -> new BannerPattern("orange_silks"));
    public static final RegistryObject<BannerPattern> MAGENTA_SILKS = BANNER_PATTERNS.register("magenta_silks", () -> new BannerPattern("magenta_silks"));
    public static final RegistryObject<BannerPattern> LIGHT_BLUE_SILKS = BANNER_PATTERNS.register("light_blue_silks", () -> new BannerPattern("light_blue_silks"));
    public static final RegistryObject<BannerPattern> YELLOW_SILKS = BANNER_PATTERNS.register("yellow_silks", () -> new BannerPattern("yellow_silks"));
    public static final RegistryObject<BannerPattern> LIME_SILKS = BANNER_PATTERNS.register("lime_silks", () -> new BannerPattern("lime_silks"));
    public static final RegistryObject<BannerPattern> PINK_SILKS = BANNER_PATTERNS.register("pink_silks", () -> new BannerPattern("pink_silks"));
    public static final RegistryObject<BannerPattern> GRAY_SILKS = BANNER_PATTERNS.register("gray_silks", () -> new BannerPattern("gray_silks"));
    public static final RegistryObject<BannerPattern> LIGHT_GRAY_SILKS = BANNER_PATTERNS.register("light_gray_silks", () -> new BannerPattern("light_gray_silks"));
    public static final RegistryObject<BannerPattern> CYAN_SILKS = BANNER_PATTERNS.register("cyan_silks", () -> new BannerPattern("cyan_silks"));
    public static final RegistryObject<BannerPattern> PURPLE_SILKS = BANNER_PATTERNS.register("purple_silks", () -> new BannerPattern("purple_silks"));
    public static final RegistryObject<BannerPattern> BLUE_SILKS = BANNER_PATTERNS.register("blue_silks", () -> new BannerPattern("blue_silks"));
    public static final RegistryObject<BannerPattern> BROWN_SILKS = BANNER_PATTERNS.register("brown_silks", () -> new BannerPattern("brown_silks"));
    public static final RegistryObject<BannerPattern> GREEN_SILKS = BANNER_PATTERNS.register("green_silks", () -> new BannerPattern("green_silks"));
    public static final RegistryObject<BannerPattern> RED_SILKS = BANNER_PATTERNS.register("red_silks", () -> new BannerPattern("red_silks"));
    public static final RegistryObject<BannerPattern> BLACK_SILKS = BANNER_PATTERNS.register("black_silks", () -> new BannerPattern("black_silks"));


    @NotNull
    private static Item.Properties getProperties(int stacksTo) {
        return new Item.Properties().stacksTo(stacksTo).tab(CREATIVE_TABS);
    }

    public EnemyBanner() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onFMLCommonSetup);
        modEventBus.addListener(this::register);
        ITEMS.register(modEventBus);
        BANNER_PATTERNS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EnemyBannerConfig.CONFIG, MOD_ID + ".toml");
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {
        event.register(Registry.BANNER_PATTERN.key(),
                helper -> {
                    ForgeRegistries.ENTITY_TYPES.getEntries().stream()
                            .filter(entry -> entry.getValue().getCategory() != MobCategory.MISC)
                            .forEach(entry -> {
                                EntityType<?> entityType = entry.getValue();
                                EntityBannerPattern entityBannerPattern = new EntityBannerPattern(entityType);
                                String hashName = entityBannerPattern.getHashname();
                                ENTITY_BANNER_PATTERNS.put(entityType, entityBannerPattern);
                                helper.register(new ResourceLocation(MOD_ID, hashName), entityBannerPattern);
                            });
                }
        );
    }

    public static LivingEntity createOrGetEntity(EntityType<?> entityType, ClientLevel clientLevel) {
        LivingEntity livingEntity = RENDER_ENTITY_CACHE.get(entityType);
        if (livingEntity == null) {
            Entity entity = entityType.create(clientLevel);
            if (entity instanceof LivingEntity) {
                livingEntity = (LivingEntity) entity;
                RENDER_ENTITY_CACHE.put(entityType, livingEntity);
            }
        }
        return livingEntity;
    }



    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static TagKey<BannerPattern> createBannerPatternTagKey(String key) {
        return TagKey.create(Registry.BANNER_PATTERN.key(), asResource(key));
    }
    @SubscribeEvent
    public void onFMLCommonSetup(FMLCommonSetupEvent event) {
        EnemyBannerConfig.loadEntityKillCount();
    }
}

