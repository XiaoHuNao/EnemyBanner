package com.xiaohunao.enemybanner.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.xiaohunao.enemybanner.BannerUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

        if (BannerUtil.hasEntityPattern(bannerStack)){
            int i = this.selectedBannerPatternIndex.get();
            boolean flag = this.isValidPatternIndex(i);
            List<Holder<BannerPattern>> list = this.selectablePatterns;
            List<Holder<BannerPattern>> list1 = Lists.newLinkedList();

            if (!dyeStack.isEmpty() && dyeStack.getItem() instanceof DyeItem dyeItem){
                list1.add(BannerPattern.byHash(BannerUtil.getDyeSilkPattern(dyeItem).getHashname()));
            }
            if (!patternStack.isEmpty() && patternStack.getItem() instanceof BannerPatternItem bannerPatternItem){
                list1.addAll(Registry.BANNER_PATTERN.getTag(bannerPatternItem.getBannerPattern()).map(ImmutableList::copyOf).orElse(ImmutableList.of()));
            }
            this.selectablePatterns = list1;



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
        }
    }



    @Inject(method = "setupResultSlot", at = @At("HEAD"), cancellable = true)
    private void setupResultSlot(Holder<BannerPattern> bannerPatternHolder, CallbackInfo ci) {
        ItemStack itemstack = this.bannerSlot.getItem();
        ItemStack itemstack1 = this.dyeSlot.getItem();
        ItemStack itemstack2 = ItemStack.EMPTY;
        if (BannerUtil.hasEntityPattern(itemstack)) {
            itemstack2 = itemstack.copy();
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
            BannerUtil.replacePattern(listtag, bannerPatternHolder.get());

            BlockItem.setBlockEntityData(itemstack2, BlockEntityType.BANNER, compoundtag);

            if (!ItemStack.matches(itemstack2, this.resultSlot.getItem())) {
                this.resultSlot.set(itemstack2);
            }

            ci.cancel();
        }


    }
}
