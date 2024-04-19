package com.xiaohunao.enemybanner.mixin;

import com.xiaohunao.enemybanner.BannerUtil;
import com.xiaohunao.enemybanner.EnemyBanner;
import com.xiaohunao.enemybanner.EntityBannerPattern;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BannerItem.class)
public class BannerItemMixin extends StandingAndWallBlockItem {
    public BannerItemMixin(Block p_248873_, Block p_251044_, Properties p_249308_, Direction p_250800_) {
        super(p_248873_, p_251044_, p_249308_, p_250800_);
    }

    @Inject(method = "appendHoverTextFromBannerBlockEntityTag", at = @At(value = "HEAD"), cancellable = true)
    private static void appendHoverTextFromBannerBlockEntityTag(ItemStack p_40543_, List<Component> p_40544_, CallbackInfo ci) {
        if (BannerUtil.hasEntityPattern(p_40543_)){
            BannerUtil.getPatterns(p_40543_).forEach((pattern) -> {
                if (pattern.get() instanceof EntityBannerPattern entityBannerPattern) {
                    EntityType<?> entityType = entityBannerPattern.entityType;
                    p_40544_.add(Component.translatable(entityType.getDescriptionId()).withStyle(ChatFormatting.GRAY));
                }
                if (pattern.is(EnemyBanner.FUNCTION_SILKS_TAG_KEY)){
                    p_40544_.add(Component.translatable("item.enemybanner." + pattern.get().getHashname()).withStyle(ChatFormatting.GRAY));
                }
            });
            ci.cancel();
        }
    }


}
