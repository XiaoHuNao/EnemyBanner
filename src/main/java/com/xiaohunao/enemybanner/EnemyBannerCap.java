package com.xiaohunao.enemybanner;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnemyBannerCap implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<EnemyBannerCap> CAP = CapabilityManager.get(new CapabilityToken<>() {});

    private int range = 16;
    private float damageReduction = 0.3F;
    private float damageAttack = 0.5F;
    private int looting = 0;
    private boolean repulsion = false;
    private boolean attraction = false;
    private boolean inhibitory = false;
    private String bannerName = "basic";

    public EnemyBannerCap() {
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CAP.orEmpty(cap, LazyOptional.of(() -> this)).cast();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("damageReduction", damageReduction);
        tag.putInt("range", range);
        tag.putFloat("damageAttack", damageAttack);
        tag.putInt("looting", looting);
        tag.putBoolean("repulsion", repulsion);
        tag.putBoolean("attraction", attraction);
        tag.putBoolean("inhibitory", inhibitory);
        tag.putString("bannerName", bannerName);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.damageReduction = nbt.getFloat("damageReduction");
        this.range = nbt.getInt("range");
        this.damageAttack = nbt.getFloat("damageAttack");
        this.looting = nbt.getInt("looting");
        this.repulsion = nbt.getBoolean("repulsion");
        this.attraction = nbt.getBoolean("attraction");
        this.inhibitory = nbt.getBoolean("inhibitory");
        this.bannerName = nbt.getString("bannerName");
    }

    public float getDamageReduction() {
        return damageReduction;
    }

    public EnemyBannerCap setDamageReduction(float damageReduction) {
        this.damageReduction = damageReduction;
        return this;
    }

    public int getRange() {
        return range;
    }

    public EnemyBannerCap setRange(int range) {
        this.range = range;
        return this;
    }

    public float getDamageAttack() {
        return damageAttack;
    }

    public EnemyBannerCap setDamageAttack(float damageAttack) {
        this.damageAttack = damageAttack;
        return this;
    }

    public int getLooting() {
        return looting;
    }

    public EnemyBannerCap setLooting(int looting) {
        this.looting = looting;
        return this;
    }

    public boolean isRepulsion() {
        return repulsion;
    }

    public EnemyBannerCap setRepulsion(boolean repulsion) {
        this.repulsion = repulsion;
        return this;
    }

    public boolean isAttraction() {
        return attraction;
    }

    public EnemyBannerCap setAttraction(boolean attraction) {
        this.attraction = attraction;
        return this;
    }

    public boolean isInhibitory() {
        return inhibitory;
    }

    public EnemyBannerCap setInhibitory(boolean inhibitory) {
        this.inhibitory = inhibitory;
        return this;
    }

    public String getBannerName() {
        return bannerName;
    }

    public EnemyBannerCap setBannerName(String bannerName) {
        this.bannerName = bannerName;
        return this;
    }

    @Mod.EventBusSubscriber
    public static class registerCapabilitiesEvent {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.register(EnemyBannerCap.class);
        }

        @SubscribeEvent
        public static void attachLevelCapability(AttachCapabilitiesEvent<BlockEntity> event) {
            BlockEntity blockEntity = event.getObject();
            if (blockEntity instanceof BannerBlockEntity bannerBlockEntity) {
                event.addCapability(EnemyBanner.asResource("enemy_banner_cap"), new EnemyBannerCap());
            }
        }
    }
}
