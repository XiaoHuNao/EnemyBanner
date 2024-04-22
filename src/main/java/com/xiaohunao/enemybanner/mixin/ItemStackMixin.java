package com.xiaohunao.enemybanner.mixin;

import com.xiaohunao.enemybanner.BannerUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
    private void getHoverName(CallbackInfoReturnable<Component> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if(BannerUtil.hasEntityPattern(stack)) {
            cir.setReturnValue(Component.translatable("item.enemybanner.enemy_banner"));
        }
    }
}
