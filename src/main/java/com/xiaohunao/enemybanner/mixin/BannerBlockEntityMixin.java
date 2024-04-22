package com.xiaohunao.enemybanner.mixin;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.enemybanner.EnemyBanner;
import com.xiaohunao.enemybanner.EntityBannerPattern;
import com.xiaohunao.enemybanner.mixed.IEnemyBannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityMixin extends BlockEntity implements IEnemyBannerBlockEntity {

    @Shadow public abstract List<Pair<Holder<BannerPattern>, DyeColor>> getPatterns();

    @Unique private int range = 16; //32
    @Unique private float resist = 0.3F; //0.5
    @Unique private float damage = 0.5F; //1.0
    @Unique private int looting = 0; //3
    @Unique private boolean push = false;
    @Unique private boolean pull = false;
    @Unique private boolean inhibitory = false;
    @Unique private EntityType<?> entityType;
    @Unique private List<LivingEntity> entities = Lists.newArrayList();


    public BannerBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Override
    public void serverBannerBlockTick(Level level, BlockPos pos, BlockState blockState, BannerBlockEntity bannerBlockEntity){
        updateData(bannerBlockEntity);
        updateEntities();
    }

    private void updateEntities() {
        List<?> entitiesInRange = getEntitiesInRange();
        for (Object object : entitiesInRange) {
            if (object instanceof LivingEntity livingEntity) {
                if(!entities.contains(livingEntity)) {
                    entities.add(livingEntity);
                    livingEntity.getPersistentData().putLong("BannerPos", getBlockPos().asLong());
                }
            }
        }

        entities.removeIf(entity -> !entitiesInRange.contains(entity));
        for (LivingEntity entity : entities) {
            if (!entitiesInRange.contains(entity)) {
                entity.getPersistentData().remove("BannerPos");
            }
        }
    }

    private void updateData(BannerBlockEntity bannerBlockEntity) {
        List<Pair<Holder<BannerPattern>, DyeColor>> patterns = getPatterns();
        patterns.forEach(pair -> {
            BannerPattern value = pair.getFirst().value();
            if (value instanceof EntityBannerPattern entityBannerPattern) {
                this.entityType = entityBannerPattern.entityType;
            }
            if (value == EnemyBanner.RANGE_SILKS.get()) {
                this.range = 32;
            }
            if (value == EnemyBanner.DAMAGE_SILKS.get()) {
                this.damage = 1.0F;
            }
            if (value == EnemyBanner.RESIST_SILKS.get()) {
                this.resist = 0.5F;
            }
            if (value == EnemyBanner.LOOT_SILKS.get()) {
                this.looting = 3;
            }
            if (value == EnemyBanner.INHIBIT_SILKS.get()) {
                this.inhibitory = true;
            }
            if (value == EnemyBanner.PULL_SILKS.get()) {
                this.pull = true;
            }
            if (value == EnemyBanner.PUSH_SILKS.get()) {
                this.push = true;
            }
            if (value == EnemyBanner.BASIC_SILKS.get()) {
                this.range = 16;
                this.damage = 0.5F;
                this.resist = 0.3F;
                this.looting = 0;
                this.pull = false;
                this.push = false;
                this.inhibitory = false;
            }
        });
    }


    private List<?> getEntitiesInRange() {
        if (this.level != null){
            BlockPos blockPos = getBlockPos();
            AABB aabb = new AABB(blockPos.offset(-range, -range, -range), blockPos.offset(range, range, range));
            return level.getEntities(entityType, aabb, entity -> true);
        }
        return List.of();
    }

    @Override
    public int getRange() {
        return range;
    }
    @Override
    public float getResist() {
        return resist;
    }
    @Override
    public float getDamage() {
        return damage;
    }
    @Override
    public int getLooting() {
        return looting;
    }
    @Override
    public boolean isPush() {
        return push;
    }
    @Override
    public boolean isPull() {
        return pull;
    }
    @Override
    public boolean isInhibitory() {
        return inhibitory;
    }
    @Override
    public EntityType<?> getEntityType() {
        return entityType;
    }
    @Override
    public List<LivingEntity> getEntities() {
        return entities;
    }
}
