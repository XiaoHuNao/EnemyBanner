package com.xiaohunao.enemybanner.mixin;

import com.xiaohunao.enemybanner.EnemyBannerCap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityMixin extends BlockEntity{
    @Unique
    private LazyOptional<EnemyBannerCap> bannerCap = LazyOptional.of(EnemyBannerCap::new);


    public BannerBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }


    @Inject(method = "load", at = @At("HEAD"))
    public void load(CompoundTag p_155232_, CallbackInfo p_155233_) {
        if (p_155232_.contains("enemyBannerCap")) {
            this.bannerCap.ifPresent(cap -> {
                cap.deserializeNBT(p_155232_.getCompound("enemyBannerCap"));
                setChanged();
            });
        }
    }

    @Inject(method = "saveAdditional", at = @At("HEAD"))
    public void saveAdditional(CompoundTag p_155234_, CallbackInfo p_155235_) {
        this.bannerCap.ifPresent(cap -> {
            p_155234_.put("enemyBannerCap", cap.serializeNBT());
            setChanged();
        });
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = this.saveWithoutMetadata();
        this.bannerCap.ifPresent(cap -> {
            compoundTag.put("enemyBannerCap", cap.serializeNBT());
            setChanged();
        });
        return compoundTag;
    }

    @Override
    public void handleUpdateTag(CompoundTag p_155231_) {
        super.handleUpdateTag(p_155231_);
        if (p_155231_.contains("enemyBannerCap")) {
            this.bannerCap.ifPresent(cap -> {
                cap.deserializeNBT(p_155231_.getCompound("enemyBannerCap"));
                setChanged();
            });
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == EnemyBannerCap.CAP) {
            return this.bannerCap.cast();
        }
        return super.getCapability(cap);
    }
}
