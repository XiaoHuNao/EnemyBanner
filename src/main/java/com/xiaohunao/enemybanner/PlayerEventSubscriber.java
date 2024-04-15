package com.xiaohunao.enemybanner;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.stream.Stream;

@Mod.EventBusSubscriber
public class PlayerEventSubscriber {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return;
        }
        Entity entity = event.getTarget();
        EntityType<?> type = entity.getType();
        ItemStack mainHandItem = player.getMainHandItem();
        EntityBannerPattern entityBannerPattern = EnemyBanner.ENTITY_BANNER_PATTERNS.get(type);
        ItemStack itemStack = EnemyBanner.appendEntityPattern(mainHandItem, entityBannerPattern,EnemyBanner.BASIC_PATTEN.get());
        player.setItemInHand(hand, itemStack);
    }

    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return;
        }
        ItemStack item = player.getMainHandItem();
        if (item.getItem() instanceof BannerPatternItem bannerPatternItem)  {
//            Optional<HolderSet.Named<BannerPattern>> tag = BuiltInRegistries.BANNER_PATTERN.getTag(bannerPatternItem.getBannerPattern());
//            Stream<Pair<TagKey<BannerPattern>, HolderSet.Named<BannerPattern>>> tags = BuiltInRegistries.BANNER_PATTERN.getTags();
//            tags.forEach(tagKeyNamedPair -> {
//                TagKey<BannerPattern> first = tagKeyNamedPair.getFirst();
//                System.out.println(first);
//            });
//            System.out.println(tag);
        }
    }


}