package com.xiaohunao.enemybanner.mixin;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(LoomScreen.class)
public abstract class LoomScreenMixin extends AbstractContainerScreen<LoomMenu> {
    @Shadow @Final private static ResourceLocation BG_LOCATION;
    @Shadow private float scrollOffs;
    @Shadow private boolean displayPatterns;
    @Shadow @Nullable private List<Pair<Holder<BannerPattern>, DyeColor>> resultBannerPatterns;
    @Shadow private boolean hasMaxPatterns;
    @Shadow private ModelPart flag;
    @Shadow private int startRow;

    @Shadow protected abstract void renderPattern(GuiGraphics p_282452_, Holder<BannerPattern> p_281940_, int p_281872_, int p_282995_);

    public LoomScreenMixin(LoomMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void renderBg(GuiGraphics p_282870_, float p_281777_, int p_283331_, int p_283087_) {
        this.renderBackground(p_282870_);
        int i = this.leftPos;
        int j = this.topPos;
        p_282870_.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        Slot slot = this.menu.getBannerSlot();
        Slot slot1 = this.menu.getDyeSlot();
        Slot slot2 = this.menu.getPatternSlot();
        Slot slot3 = this.menu.getResultSlot();
        if (!slot.hasItem()) {
            p_282870_.blit(BG_LOCATION, i + slot.x, j + slot.y, this.imageWidth, 0, 16, 16);
        }

        if (!slot1.hasItem()) {
            p_282870_.blit(BG_LOCATION, i + slot1.x, j + slot1.y, this.imageWidth + 16, 0, 16, 16);
        }

        if (!slot2.hasItem()) {
            p_282870_.blit(BG_LOCATION, i + slot2.x, j + slot2.y, this.imageWidth + 32, 0, 16, 16);
        }

        int k = (int) (41.0F * this.scrollOffs);
        p_282870_.blit(BG_LOCATION, i + 119, j + 13 + k, 232 + (this.displayPatterns ? 0 : 12), 0, 12, 15);
        Lighting.setupForFlatItems();
        if (this.resultBannerPatterns != null && !this.hasMaxPatterns) {
            p_282870_.pose().pushPose();
            p_282870_.pose().translate((float) (i + 139), (float) (j + 52), 0.0F);
            p_282870_.pose().scale(24.0F, -24.0F, 1.0F);
            p_282870_.pose().translate(0.5F, 0.5F, 0.5F);
            float f = 0.6666667F;
            p_282870_.pose().scale(0.6666667F, -0.6666667F, -0.6666667F);
            this.flag.xRot = 0.0F;
            this.flag.y = -32.0F;
            BannerRenderer.renderPatterns(p_282870_.pose(), p_282870_.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, this.flag, ModelBakery.BANNER_BASE, true, this.resultBannerPatterns);
            p_282870_.pose().popPose();
            p_282870_.flush();
        } else if (this.hasMaxPatterns) {
            p_282870_.blit(BG_LOCATION, i + slot3.x - 2, j + slot3.y - 2, this.imageWidth, 17, 17, 16);
        }

        if (this.displayPatterns) {
            int l2 = i + 60;
            int l = j + 13;
            List<Holder<BannerPattern>> list = this.menu.getSelectablePatterns();

            label64:
            for (int i1 = 0; i1 < 4; ++i1) {
             for (int j1 = 0; j1 < 4; ++j1) {
                 int k1 = i1 + this.startRow;
                 int l1 = k1 * 4 + j1;
                 if (l1 >= list.size()) {
                     break label64;
                 }

                 int i2 = l2 + j1 * 14;
                 int j2 = l + i1 * 14;
                 boolean flag = p_283331_ >= i2 && p_283087_ >= j2 && p_283331_ < i2 + 14 && p_283087_ < j2 + 14;
                 int k2;
                 if (l1 == this.menu.getSelectedBannerPatternIndex()) {
                     k2 = this.imageHeight + 14;
                 } else if (flag) {
                     k2 = this.imageHeight + 28;
                 } else {
                     k2 = this.imageHeight;
                 }

                 p_282870_.blit(BG_LOCATION, i2, j2, 0, k2, 14, 14);
                 this.renderPattern(p_282870_, list.get(l1), i2, j2);
             }
            }
        }

        Lighting.setupFor3DItems();
    }
}
