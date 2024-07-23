package com.xiaohunao.enemybanner.datagen.provider;

import com.xiaohunao.enemybanner.EnemyBanner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.concurrent.CompletableFuture;

public class BannerPatternTagsProvider extends TagsProvider<BannerPattern> {
    public BannerPatternTagsProvider(PackOutput p_256596_, ResourceKey<? extends Registry<BannerPattern>> p_255886_, CompletableFuture<HolderLookup.Provider> p_256513_) {
        super(p_256596_, p_255886_, p_256513_);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EnemyBanner.COLOR_SILKS_TAG_KEY)
                .add(EnemyBanner.WHITE_SILKS.getKey(),
                        EnemyBanner.ORANGE_SILKS.getKey(),
                        EnemyBanner.MAGENTA_SILKS.getKey(),
                        EnemyBanner.LIGHT_BLUE_SILKS.getKey(),
                        EnemyBanner.YELLOW_SILKS.getKey(),
                        EnemyBanner.LIME_SILKS.getKey(),
                        EnemyBanner.PINK_SILKS.getKey(),
                        EnemyBanner.GRAY_SILKS.getKey(),
                        EnemyBanner.LIGHT_GRAY_SILKS.getKey(),
                        EnemyBanner.CYAN_SILKS.getKey(),
                        EnemyBanner.PURPLE_SILKS.getKey(),
                        EnemyBanner.BLUE_SILKS.getKey(),
                        EnemyBanner.BROWN_SILKS.getKey(),
                        EnemyBanner.GREEN_SILKS.getKey(),
                        EnemyBanner.RED_SILKS.getKey(),
                        EnemyBanner.BLACK_SILKS.getKey());

        this.tag(EnemyBanner.FUNCTION_SILKS_TAG_KEY)
                .add(EnemyBanner.BASIC_SILKS.getKey(),
                        EnemyBanner.DAMAGE_SILKS.getKey(),
                        EnemyBanner.INHIBIT_SILKS.getKey(),
                        EnemyBanner.LOOT_SILKS.getKey(),
                        EnemyBanner.PULL_SILKS.getKey(),
                        EnemyBanner.PUSH_SILKS.getKey(),
                        EnemyBanner.RANGE_SILKS.getKey(),
                        EnemyBanner.RESIST_SILKS.getKey());

        this.tag(EnemyBanner.createBannerPatternTagKey("basic_silks"))
                .add(EnemyBanner.BASIC_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("damage_silks"))
                .add(EnemyBanner.DAMAGE_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("inhibit_silks"))
                .add(EnemyBanner.INHIBIT_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("loot_silks"))
                .add(EnemyBanner.LOOT_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("pull_silks"))
                .add(EnemyBanner.PULL_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("push_silks"))
                .add(EnemyBanner.PUSH_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("range_silks"))
                .add(EnemyBanner.RANGE_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("resist_silks"))
                .add(EnemyBanner.RESIST_SILKS.getKey());


        this.tag(EnemyBanner.createBannerPatternTagKey("white_silks"))
                .add(EnemyBanner.WHITE_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("orange_silks"))
                .add(EnemyBanner.ORANGE_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("magenta_silks"))
                .add(EnemyBanner.MAGENTA_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("light_blue_silks"))
                .add(EnemyBanner.LIGHT_BLUE_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("yellow_silks"))
                .add(EnemyBanner.YELLOW_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("lime_silks"))
                .add(EnemyBanner.LIME_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("pink_silks"))
                .add(EnemyBanner.PINK_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("gray_silks"))
                .add(EnemyBanner.GRAY_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("light_gray_silks"))
                .add(EnemyBanner.LIGHT_GRAY_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("cyan_silks"))
                .add(EnemyBanner.CYAN_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("purple_silks"))
                .add(EnemyBanner.PURPLE_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("blue_silks"))
                .add(EnemyBanner.BLUE_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("brown_silks"))
                .add(EnemyBanner.BROWN_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("green_silks"))
                .add(EnemyBanner.GREEN_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("red_silks"))
                .add(EnemyBanner.RED_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("black_silks"))
                .add(EnemyBanner.BLACK_SILKS.getKey());

    }

}
