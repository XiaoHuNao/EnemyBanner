package com.xiaohunao.enemybanner;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

import java.util.LinkedHashMap;

@Mod(EnemyBanner.MOD_ID)
public class EnemyBanner {
    public static final String MOD_ID = "enemybanner";
    public static final LinkedHashMap<EntityType<?>, EntityBannerPattern> ENTITY_BANNER_PATTERNS = Maps.newLinkedHashMap();
    public static final LinkedHashMap<EntityType<?>,LivingEntity> RENDER_ENTITY_CACHE = Maps.newLinkedHashMap();
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EnemyBanner.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, MOD_ID);
    public static final TagKey<BannerPattern> FUNCTION_SILKS_TAG_KEY = createBannerPatternTagKey("function_silks");
    public static final TagKey<BannerPattern> COLOR_SILKS_TAG_KEY = createBannerPatternTagKey("color_silks");

    public static final RegistryObject<BannerPattern> BASIC_SILKS = BANNER_PATTERNS.register("basic_silks", () -> new BannerPattern("basic_silks"));
    public static final RegistryObject<BannerPatternItem> BASIC = ITEMS.register("basic_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("basic_silks"), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BannerPattern> DAMAGE_SILKS = BANNER_PATTERNS.register("damage_silks", () -> new BannerPattern("damage_silks"));
    public static final RegistryObject<BannerPatternItem> DAMAGE = ITEMS.register("damage_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("damage_silks"), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BannerPattern> INHIBIT_SILKS = BANNER_PATTERNS.register("inhibit_silks", () -> new BannerPattern("inhibit_silks"));
    public static final RegistryObject<BannerPatternItem> INHIBIT = ITEMS.register("inhibit_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("inhibit_silks"), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BannerPattern> LOOT_SILKS = BANNER_PATTERNS.register("loot_silks", () -> new BannerPattern("loot_silks"));
    public static final RegistryObject<BannerPatternItem> LOOT = ITEMS.register("loot_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("loot_silks"), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BannerPattern> PULL_SILKS = BANNER_PATTERNS.register("pull_silks", () -> new BannerPattern("pull_silks"));
    public static final RegistryObject<BannerPatternItem> PULL = ITEMS.register("pull_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("pull_silks"), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BannerPattern> PUSH_SILKS = BANNER_PATTERNS.register("push_silks", () -> new BannerPattern("push_silks"));
    public static final RegistryObject<BannerPatternItem> PUSH = ITEMS.register("push_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("push_silks"), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BannerPattern> RANGE_SILKS = BANNER_PATTERNS.register("range_silks", () -> new BannerPattern("range_silks"));
    public static final RegistryObject<BannerPatternItem> RANGE = ITEMS.register("range_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("range_silks"), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BannerPattern> RESIST_SILKS = BANNER_PATTERNS.register("resist_silks", () -> new BannerPattern("resist_silks"));
    public static final RegistryObject<BannerPatternItem> RESIST = ITEMS.register("resist_silks",
            () -> new BannerPatternItem(createBannerPatternTagKey("resist_silks"), new Item.Properties().stacksTo(1)));

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


    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("enemybanner_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.enemybanner_tab"))
            .icon(() -> BannerUtil.appendEntityPattern(Items.WHITE_BANNER.getDefaultInstance(), ENTITY_BANNER_PATTERNS.get(EntityType.ZOMBIE), BASIC_SILKS.get(), WHITE_SILKS.get()))
            .displayItems((parameters, output) -> {
                output.accept(BannerUtil.appendEntityPattern(Items.WHITE_BANNER.getDefaultInstance(), ENTITY_BANNER_PATTERNS.get(EntityType.ZOMBIE), BASIC_SILKS.get(), WHITE_SILKS.get()));
//                ForgeRegistries.ENTITY_TYPES.getEntries().stream()
//                        .filter(entry -> entry.getValue().getCategory() != MobCategory.MISC)
//                        .forEach(entry -> {
//                            EntityType<?> entityType = entry.getValue();
//                            EntityBannerPattern entityBannerPattern = ENTITY_BANNER_PATTERNS.get(entityType);
//                            if (entityBannerPattern != null) {
//                                output.accept(BannerUtil.appendEntityPattern(Items.WHITE_BANNER.getDefaultInstance(), entityBannerPattern, BASIC_SILKS.get(), WHITE_SILKS.get()));
//                            }
//                        });
                output.accept(BASIC.get());
                output.accept(DAMAGE.get());
                output.accept(INHIBIT.get());
                output.accept(LOOT.get());
                output.accept(PULL.get());
                output.accept(PUSH.get());
                output.accept(RANGE.get());
                output.accept(RESIST.get());
            }).build());

    public EnemyBanner() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::register);
        modEventBus.addListener(this::onFMLCommonSetup);

        ITEMS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        BANNER_PATTERNS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EnemyBannerConfig.CONFIG, MOD_ID + ".toml");
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {
        event.register(Registries.BANNER_PATTERN,
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





    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static TagKey<BannerPattern> createBannerPatternTagKey(String key) {
        return TagKey.create(Registries.BANNER_PATTERN, asResource(key));
    }
    @SubscribeEvent
    public void onFMLCommonSetup(FMLCommonSetupEvent event) {
        EnemyBannerConfig.loadEntityKillCount();
    }
}

