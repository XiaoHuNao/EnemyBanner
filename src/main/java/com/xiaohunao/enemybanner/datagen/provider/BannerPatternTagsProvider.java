package com.xiaohunao.enemybanner.datagen.provider;

import com.xiaohunao.enemybanner.EnemyBanner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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

        this.tag(EnemyBanner.createBannerPatternTagKey("basic"))
                .add(EnemyBanner.BASIC_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("damage"))
                .add(EnemyBanner.DAMAGE_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("inhibit"))
                .add(EnemyBanner.INHIBIT_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("loot"))
                .add(EnemyBanner.LOOT_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("pull"))
                .add(EnemyBanner.PULL_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("push"))
                .add(EnemyBanner.PUSH_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("range"))
                .add(EnemyBanner.RANGE_SILKS.getKey());
        this.tag(EnemyBanner.createBannerPatternTagKey("resist"))
                .add(EnemyBanner.RESIST_SILKS.getKey());
    }

}
