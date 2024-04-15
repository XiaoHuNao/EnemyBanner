package com.xiaohunao.enemybanner.mixin;

import com.google.common.collect.Lists;
import com.xiaohunao.enemybanner.EnemyBanner;
import com.xiaohunao.enemybanner.EnemyBannerCap;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(LoomMenu.class)
public abstract class LoomMenuMixin extends AbstractContainerMenu {
    @Shadow @Final Slot bannerSlot;
    @Shadow @Final Slot dyeSlot;
    @Shadow @Final private Slot patternSlot;
    @Shadow @Final private Slot resultSlot;
    @Shadow private List<Holder<BannerPattern>> selectablePatterns;
    @Shadow @Final DataSlot selectedBannerPatternIndex;

    protected LoomMenuMixin(@Nullable MenuType<?> p_38851_, int p_38852_) {
        super(p_38851_, p_38852_);
    }

    @Shadow protected abstract boolean isValidPatternIndex(int p_242850_);
    @Shadow protected abstract List<Holder<BannerPattern>> getSelectablePatterns(ItemStack stack);
    @Shadow protected abstract void setupResultSlot(Holder<BannerPattern> p_219992_);

    @Inject(method = "slotsChanged", at = @At("HEAD"), cancellable = true)
    public void slotsChanged(Container p_39863_, CallbackInfo ci) {
        ItemStack bannerStack = bannerSlot.getItem();
        ItemStack dyeStack = dyeSlot.getItem();
        ItemStack patternStack = patternSlot.getItem();

        if (EnemyBanner.hasEntityPattern(bannerStack) && !dyeStack.isEmpty()) {
            int i = this.selectedBannerPatternIndex.get();
            boolean flag = this.isValidPatternIndex(i);
            List<Holder<BannerPattern>> list = this.selectablePatterns;
//            this.selectablePatterns = this.getSelectablePatterns(patternStack);
//            this.selectablePatterns.addAll(EnemyBanner.getEntityPatterns(bannerStack));
            this.selectablePatterns = EnemyBanner.getEntityPatterns(bannerStack);


            Holder<BannerPattern> holder;
            if (this.selectablePatterns.size() == 1) {
                this.selectedBannerPatternIndex.set(0);
                holder = this.selectablePatterns.get(0);
            } else if (!flag) {
                this.selectedBannerPatternIndex.set(-1);
                holder = null;
            } else {
                Holder<BannerPattern> holder1 = list.get(i);
                int j = this.selectablePatterns.indexOf(holder1);
                if (j != -1) {
                    holder = holder1;
                    this.selectedBannerPatternIndex.set(j);
                } else {
                    holder = null;
                    this.selectedBannerPatternIndex.set(-1);
                }
            }



            if (holder != null) {
                CompoundTag compoundtag = BlockItem.getBlockEntityData(bannerStack);
                boolean flag1 = compoundtag != null && compoundtag.contains("Patterns", 9) &&
                        !bannerStack.isEmpty() && compoundtag.getList("Patterns", 10).size() >= 6;
                if (flag1) {
                    this.selectedBannerPatternIndex.set(-1);
                    this.resultSlot.set(ItemStack.EMPTY);
                } else {
                    this.setupResultSlot(holder);
                }
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }

            this.broadcastChanges();
            ci.cancel();
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
            this.selectablePatterns = List.of();
            this.selectedBannerPatternIndex.set(-1);
        }

    }



    @Inject(method = "setupResultSlot", at = @At("HEAD"), cancellable = true)
    private void setupResultSlot(Holder<BannerPattern> p_219992_, CallbackInfo ci) {
        ItemStack itemstack = this.bannerSlot.getItem();
        ItemStack itemstack1 = this.dyeSlot.getItem();
        ItemStack itemstack2 = ItemStack.EMPTY;
        if (EnemyBanner.hasEntityPattern(itemstack) && !itemstack1.isEmpty()) {
            itemstack2 = itemstack.copyWithCount(1);
            EnemyBanner.clearEntityPatterns(itemstack2);
            DyeColor dyecolor = ((DyeItem)itemstack1.getItem()).getDyeColor();
            CompoundTag compoundtag = BlockItem.getBlockEntityData(itemstack2);
            ListTag listtag;
            if (compoundtag != null && compoundtag.contains("Patterns", 9)) {
                listtag = compoundtag.getList("Patterns", 10);
            } else {
                listtag = new ListTag();
                if (compoundtag == null) {
                    compoundtag = new CompoundTag();
                }

                compoundtag.put("Patterns", listtag);
            }
            CompoundTag compoundtag1 = new CompoundTag();
            compoundtag1.putString("Pattern", p_219992_.value().getHashname());
            compoundtag1.putInt("Color", dyecolor.getId());
            listtag.add(compoundtag1);
            BlockItem.setBlockEntityData(itemstack2, BlockEntityType.BANNER, compoundtag);

            if (!ItemStack.matches(itemstack2, this.resultSlot.getItem())) {
                this.resultSlot.set(itemstack2);
            }

            ci.cancel();
        }


    }
}
