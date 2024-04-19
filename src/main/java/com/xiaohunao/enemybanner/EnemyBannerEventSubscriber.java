package com.xiaohunao.enemybanner;

import com.xiaohunao.enemybanner.mixed.IEnemyBannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class EnemyBannerEventSubscriber {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        Entity entity = source.getEntity();
        LivingEntity livingEntity = event.getEntity();
        String entityType = EntityType.getKey(livingEntity.getType()).toString();
        if(livingEntity.level().isClientSide()) {
            return;
        }

        if (entity instanceof Player player) {
            CompoundTag persistentData = player.getPersistentData();
            CompoundTag compoundTag = new CompoundTag();
            if (!persistentData.contains("enemy_banner_kill_count")) {
                compoundTag.putInt(entityType, 1);
            }else {
                compoundTag = persistentData.getCompound("enemy_banner_kill_count");
                if (compoundTag.contains(entityType)) {
                    int killCount = compoundTag.getInt(entityType);
                    compoundTag.putInt(entityType, killCount + 1);
                }else {
                    compoundTag.putInt(entityType, 1);
                }
            }

            int killCount = EnemyBannerConfig.getKillCount(entityType);
            if (compoundTag.getInt(entityType) >= killCount) {
                ItemStack itemStack = BannerUtil.appendEntityPattern(Items.WHITE_BANNER.getDefaultInstance(), EnemyBanner.ENTITY_BANNER_PATTERNS.get(EntityType.ZOMBIE), EnemyBanner.BASIC_SILKS.get(), EnemyBanner.WHITE_SILKS.get());
                player.addItem(itemStack);
                compoundTag.remove(entityType);
            }
            persistentData.put("enemy_banner_kill_count", compoundTag);
        }
    }


    @SubscribeEvent
    public static void damageResist(LivingDamageEvent event) {
        LivingEntity livingEntity = event.getEntity();
        DamageSource source = event.getSource();
        Entity sourceEntity = source.getEntity();
        float amount = event.getAmount();
        Level level = livingEntity.level();
        if (level.isClientSide()) {
            return;
        }
        if (livingEntity instanceof Player player && sourceEntity instanceof LivingEntity entity) {
            LazyOptional<IEnemyBannerBlockEntity> enemyBanner = getBannerBlockEntity(entity);
            enemyBanner.ifPresent(blockEntity -> {
                float resist = blockEntity.getResist();
                event.setAmount(amount * (1 - resist));
            });
        }
    }

    @SubscribeEvent
    public static void damageToEnemy(LivingDamageEvent event) {
        LivingEntity livingEntity = event.getEntity();
        DamageSource source = event.getSource();
        Entity sourceEntity = source.getEntity();
        float amount = event.getAmount();
        Level level = livingEntity.level();
        if (level.isClientSide()) {
            return;
        }

        if (sourceEntity instanceof Player) {
            LazyOptional<IEnemyBannerBlockEntity> enemyBanner = getBannerBlockEntity(livingEntity);
            enemyBanner.ifPresent(blockEntity -> {
                float damage = blockEntity.getDamage();
                event.setAmount(amount * (1 + damage));
            });
        }
    }

    @SubscribeEvent
    public static void looting(LootingLevelEvent event) {
        int lootingLevel = event.getLootingLevel();
        DamageSource damageSource = event.getDamageSource();
        LivingEntity livingEntity = event.getEntity();
        Entity sourceEntity = damageSource.getEntity();
        Level level = livingEntity.level();
        if (level.isClientSide()) {
            return;
        }

        if (sourceEntity instanceof Player player) {
            LazyOptional<IEnemyBannerBlockEntity> enemyBanner = getBannerBlockEntity(livingEntity);
            enemyBanner.ifPresent(blockEntity -> {
                int looting = blockEntity.getLooting();
                event.setLootingLevel(lootingLevel + looting);
            });
        }
    }

    @SubscribeEvent
    public static void inhibitory(EntityTeleportEvent.EnderEntity event) {
        LivingEntity entityLiving = event.getEntityLiving();
        Level level = entityLiving.level();
        if (level.isClientSide()) {
            return;
        }
        getBannerBlockEntity(entityLiving).ifPresent(blockEntity -> {
            if (blockEntity.isInhibitory()) {
                event.setCanceled(true);
            }
        });
    }

    private static LazyOptional<IEnemyBannerBlockEntity> getBannerBlockEntity(LivingEntity entity) {
        CompoundTag persistentData = entity.getPersistentData();
        if (persistentData.contains("Banner")) {
            BlockPos blockPos = BlockPos.of(persistentData.getLong("Banner"));
            if (entity.level().getBlockEntity(blockPos) instanceof IEnemyBannerBlockEntity blockEntity) return LazyOptional.of(() -> blockEntity);
        }
        return LazyOptional.empty();
    }

    @SubscribeEvent
    public static void onPlayerEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND || !player.isShiftKeyDown() || !player.isCreative()) {
            return;
        }
        Entity entity = event.getTarget();
        EntityType<?> type = entity.getType();
        ItemStack mainHandItem = player.getMainHandItem();
        EntityBannerPattern entityBannerPattern = EnemyBanner.ENTITY_BANNER_PATTERNS.get(type);
        ItemStack itemStack = BannerUtil.appendEntityPattern(mainHandItem, entityBannerPattern,EnemyBanner.BASIC_SILKS.get(),EnemyBanner.WHITE_SILKS.get());
        player.setItemInHand(hand, itemStack);
    }
}