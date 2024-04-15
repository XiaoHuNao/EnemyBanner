package com.xiaohunao.enemybanner;

import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Mod(EnemyBanner.MOD_ID)
public class EnemyBanner {
    public static final String MOD_ID = "enemybanner";
    public static final LinkedHashMap<EntityType<?>, EntityBannerPattern> ENTITY_BANNER_PATTERNS = Maps.newLinkedHashMap();
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EnemyBanner.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, MOD_ID);

    //basic
    public static final RegistryObject<FunctionBannerPattern> BASIC_PATTEN = BANNER_PATTERNS.register("basic", () -> new FunctionBannerPattern("basic"));
    public static final RegistryObject<BannerPatternItem> BASIC = ITEMS.register("basic",
            () -> new  BannerPatternItem(createBannerPatternTagKey("basic"),new Item.Properties().stacksTo(1)));

    //damage
    public static final RegistryObject<FunctionBannerPattern> DAMAGE_PATTEN = BANNER_PATTERNS.register("damage", () -> new FunctionBannerPattern("damage"));
    public static final RegistryObject<BannerPatternItem> DAMAGE = ITEMS.register("damage",
            () -> new  BannerPatternItem(createBannerPatternTagKey("damage"),new Item.Properties().stacksTo(1)));
    //inhibit
    public static final RegistryObject<FunctionBannerPattern> INHIBIT_PATTEN = BANNER_PATTERNS.register("inhibit", () -> new FunctionBannerPattern("inhibit"));
    public static final RegistryObject<BannerPatternItem> INHIBIT = ITEMS.register("inhibit",
            () -> new  BannerPatternItem(createBannerPatternTagKey("inhibit"),new Item.Properties().stacksTo(1)));
    //loot
    public static final RegistryObject<FunctionBannerPattern> LOOT_PATTEN = BANNER_PATTERNS.register("loot", () -> new FunctionBannerPattern("loot"));
    public static final RegistryObject<BannerPatternItem> LOOT = ITEMS.register("loot",
            () -> new  BannerPatternItem(createBannerPatternTagKey("loot"),new Item.Properties().stacksTo(1)));
    //pull
    public static final RegistryObject<FunctionBannerPattern> PULL_PATTEN = BANNER_PATTERNS.register("pull", () -> new FunctionBannerPattern("pull"));
    public static final RegistryObject<BannerPatternItem> PULL = ITEMS.register("pull",
            () -> new  BannerPatternItem(createBannerPatternTagKey("pull"),new Item.Properties().stacksTo(1)));
    //push
    public static final RegistryObject<FunctionBannerPattern> PUSH_PATTEN = BANNER_PATTERNS.register("push", () -> new FunctionBannerPattern("push"));
    public static final RegistryObject<BannerPatternItem> PUSH = ITEMS.register("push",
            () -> new  BannerPatternItem(createBannerPatternTagKey("push"),new Item.Properties().stacksTo(1)));
    //range
    public static final RegistryObject<FunctionBannerPattern> RANGE_PATTEN = BANNER_PATTERNS.register("range", () -> new FunctionBannerPattern("range"));
    public static final RegistryObject<BannerPatternItem> RANGE = ITEMS.register("range",
            () -> new  BannerPatternItem(createBannerPatternTagKey("range"),new Item.Properties().stacksTo(1)));
    //resist
    public static final RegistryObject<FunctionBannerPattern> RESIST_PATTEN = BANNER_PATTERNS.register("resist", () -> new FunctionBannerPattern("resist"));
    public static final RegistryObject<BannerPatternItem> RESIST = ITEMS.register("resist",
            () -> new  BannerPatternItem(createBannerPatternTagKey("resist"),new Item.Properties().stacksTo(1)));





//    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("tab", () -> CreativeModeTab.builder()
//            .withTabsBefore(CreativeModeTabs.COMBAT)
//            .icon(() -> appendEntityPattern(Items.WHITE_BANNER.getDefaultInstance(), ENTITY_BANNER_PATTERNS.get(EntityType.ZOMBIE)))
//            .displayItems((parameters, output) -> {
//                ForgeRegistries.ENTITY_TYPES.getEntries().stream()
//                        .filter(entry -> entry.getValue().getCategory() != MobCategory.MISC)
//                        .forEach(entry -> {
//                            EntityType<?> value = entry.getValue();
//                            output.accept(appendEntityPattern(Items.WHITE_BANNER.getDefaultInstance(), ENTITY_BANNER_PATTERNS.get(value)));
//                        });
//            }).build());

    public EnemyBanner() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::register);

        ITEMS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        BANNER_PATTERNS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
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

    public static void serverBannerBlockTick(Level level, BlockPos pos, BlockState blockState, BannerBlockEntity bannerBlockEntity) {

    }
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
    private static TagKey<BannerPattern> createBannerPatternTagKey(String key) {
        return TagKey.create(Registries.BANNER_PATTERN, asResource(key));
    }

    public static ItemStack appendEntityPattern(ItemStack stack, EntityBannerPattern entityBannerPattern,FunctionBannerPattern functionBannerPattern) {
        CompoundTag bannerTag = stack.getOrCreateTag();
        CompoundTag blockEntityTag = new CompoundTag();
        ListTag patternsTag = new ListTag();


        CompoundTag functionCompoundTag = new CompoundTag();
        functionCompoundTag.putString("Pattern", functionBannerPattern.getHashname());
        functionCompoundTag.putInt("Color", 10);
        patternsTag.add(functionCompoundTag);


        CompoundTag entityPatternTag = new CompoundTag();
        ResourceLocation id = EntityType.getKey(entityBannerPattern.entityType);
        entityPatternTag.putString("Pattern", id.toLanguageKey());
        entityPatternTag.putInt("Color", 10);
        patternsTag.add(entityPatternTag);


        blockEntityTag.put("Patterns", patternsTag);
        bannerTag.put("BlockEntityTag", blockEntityTag);
        return stack;
    }
    public static boolean hasEntityPattern(ItemStack stack) {
        CompoundTag compoundtag = BlockItem.getBlockEntityData(stack);
        if (compoundtag != null && compoundtag.contains("Patterns")) {
            ListTag listtag = compoundtag.getList("Patterns", 10);

            for (Tag tag : listtag) {
                CompoundTag compoundtag1 = (CompoundTag) tag;
                Holder<BannerPattern> pattern = BannerPattern.byHash(compoundtag1.getString("Pattern"));
                if (pattern != null && pattern.value() instanceof EntityBannerPattern) {
                    return true;
                }
            }
        }
        return false;
    }
    public static List<Holder<BannerPattern>> getPatterns(ItemStack stack) {
        CompoundTag compoundtag = BlockItem.getBlockEntityData(stack);
        if (compoundtag != null && compoundtag.contains("Patterns")) {
            ListTag listtag = compoundtag.getList("Patterns", 10);
            return listtag.stream()
                    .map(tag -> (CompoundTag) tag)
                    .map(tag -> BannerPattern.byHash(tag.getString("Pattern")))
                    .filter(Objects::nonNull)
                    .toList();
        }
        return List.of();
    }


    public static List<Holder<BannerPattern>> getEntityPatterns(ItemStack stack) {
        CompoundTag compoundtag = BlockItem.getBlockEntityData(stack);
        if (compoundtag != null && compoundtag.contains("Patterns")) {
            ListTag listtag = compoundtag.getList("Patterns", 10);
            return listtag.stream()
                    .map(tag -> (CompoundTag) tag)
                    .map(tag -> BannerPattern.byHash(tag.getString("Pattern")))
                    .filter(Objects::nonNull)
                    .filter(holder -> holder.value() instanceof EntityBannerPattern)
                    .toList();
        }
        return List.of();
    }

    public static void clearEntityPatterns(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("BlockEntityTag")) {
            CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");
            if (blockEntityTag.contains("Patterns")) {
                ListTag patternsTag = blockEntityTag.getList("Patterns", 10);
                patternsTag.removeIf(pattern -> {
                    CompoundTag compoundTag = (CompoundTag) pattern;
                    Holder<BannerPattern> holder = BannerPattern.byHash(compoundTag.getString("Pattern"));
                    return holder != null && holder.value() instanceof EntityBannerPattern;
                });
                blockEntityTag.put("Patterns", patternsTag);
                tag.put("BlockEntityTag", blockEntityTag);
                stack.setTag(tag);
            }
        }
    }

    //给List<Pattern>排序，,list里面只有一个EntityBannerPattern，和一个基础Base，其他的都是普通的Pattern，确保EntityBannerPattern在最后面
    public static void sortEntityPatterns(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("BlockEntityTag")) {
            CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");
            if (blockEntityTag.contains("Patterns")) {
                ListTag patternsTag = blockEntityTag.getList("Patterns", 10);
                for (Tag tag1 : patternsTag) {
                    CompoundTag compoundTag = (CompoundTag) tag1;
                    Holder<BannerPattern> holder = BannerPattern.byHash(compoundTag.getString("Pattern"));
                    if (holder != null && holder.value() instanceof EntityBannerPattern) {
                        patternsTag.remove(tag1);
                        patternsTag.add(tag1);
                        break;
                    }
                }
                blockEntityTag.put("Patterns", patternsTag);
                tag.put("BlockEntityTag", blockEntityTag);
                stack.setTag(tag);
            }
        }
    }

}
