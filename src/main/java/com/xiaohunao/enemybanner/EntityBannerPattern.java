package com.xiaohunao.enemybanner;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityBannerPattern extends BannerPattern {
    public EntityType<?> entityType;
    public EntityBannerPattern(EntityType<?> entityType) {
        super(ForgeRegistries.ENTITY_TYPES.getKey(entityType).toLanguageKey());
        this.entityType = entityType;
    }
}
