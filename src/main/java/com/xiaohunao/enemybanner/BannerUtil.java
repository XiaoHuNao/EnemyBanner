package com.xiaohunao.enemybanner;

import com.xiaohunao.enemybanner.mixed.IEnemyBannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;

public class BannerUtil {
    public static BannerPattern getDyeSilkPattern(DyeItem dyeItem) {
        DyeColor dyeColor = dyeItem.getDyeColor();
        return switch (dyeColor) {
            case ORANGE -> EnemyBanner.ORANGE_SILKS.get();
            case MAGENTA -> EnemyBanner.MAGENTA_SILKS.get();
            case LIGHT_BLUE -> EnemyBanner.LIGHT_BLUE_SILKS.get();
            case YELLOW -> EnemyBanner.YELLOW_SILKS.get();
            case LIME -> EnemyBanner.LIME_SILKS.get();
            case PINK -> EnemyBanner.PINK_SILKS.get();
            case GRAY -> EnemyBanner.GRAY_SILKS.get();
            case LIGHT_GRAY -> EnemyBanner.LIGHT_GRAY_SILKS.get();
            case CYAN -> EnemyBanner.CYAN_SILKS.get();
            case PURPLE -> EnemyBanner.PURPLE_SILKS.get();
            case BLUE -> EnemyBanner.BLUE_SILKS.get();
            case BROWN -> EnemyBanner.BROWN_SILKS.get();
            case GREEN -> EnemyBanner.GREEN_SILKS.get();
            case RED -> EnemyBanner.RED_SILKS.get();
            case BLACK -> EnemyBanner.BLACK_SILKS.get();
            case WHITE -> EnemyBanner.WHITE_SILKS.get();
        };
    }

    public static ItemStack appendEntityPattern(ItemStack stack, EntityBannerPattern entityBannerPattern, BannerPattern function, BannerPattern dye) {
        CompoundTag bannerTag = stack.getOrCreateTag();
        CompoundTag blockEntityTag = new CompoundTag();
        ListTag patternsTag = new ListTag();


        CompoundTag functionCompoundTag = new CompoundTag();
        functionCompoundTag.putString("Pattern", function.getHashname());
        functionCompoundTag.putInt("Color", 10);
        patternsTag.add(functionCompoundTag);

        CompoundTag dyeCompoundTag = new CompoundTag();
        dyeCompoundTag.putString("Pattern", dye.getHashname());
        dyeCompoundTag.putInt("Color", 10);
        patternsTag.add(dyeCompoundTag);


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

    public static void replacePattern(ListTag tag, BannerPattern pattern) {
        for (Tag tag1 : tag) {
            CompoundTag compoundTag = (CompoundTag)tag1;
            if (BannerPattern.byHash(compoundTag.getString("Pattern")).is(EnemyBanner.FUNCTION_SILKS_TAG_KEY) && BuiltInRegistries.BANNER_PATTERN.wrapAsHolder(pattern).is(EnemyBanner.FUNCTION_SILKS_TAG_KEY)) {
                compoundTag.putString("Pattern", pattern.getHashname());
                return;
            }
            if(BannerPattern.byHash(compoundTag.getString("Pattern")).is(EnemyBanner.COLOR_SILKS_TAG_KEY) && BuiltInRegistries.BANNER_PATTERN.wrapAsHolder(pattern).is(EnemyBanner.COLOR_SILKS_TAG_KEY)) {
                compoundTag.putString("Pattern", pattern.getHashname());
                return;
            }
        }
    }
    public static void serverBannerBlockTick(Level level, BlockPos pos, BlockState blockState, BannerBlockEntity bannerBlockEntity) {
        if (BannerUtil.hasEntityPattern(bannerBlockEntity.getItem())){
            IEnemyBannerBlockEntity enemyBannerBlockEntity = (IEnemyBannerBlockEntity) bannerBlockEntity;
            enemyBannerBlockEntity.serverBannerBlockTick(level, pos, blockState, bannerBlockEntity);
        }
    }
}
