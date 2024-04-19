package com.xiaohunao.enemybanner.mixed;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IEnemyBannerBlockEntity {
    void serverBannerBlockTick(Level level, BlockPos pos, BlockState blockState, BannerBlockEntity bannerBlockEntity);

    int getRange();

    float getResist();

    float getDamage();

    int getLooting();

    boolean isPush();

    boolean isPull();

    boolean isInhibitory();

    EntityType<?> getEntityType();

    List<LivingEntity> getEntities();
}
