package com.xiaohunao.enemybanner.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.enemybanner.BannerUtil;
import com.xiaohunao.enemybanner.EnemyBanner;
import com.xiaohunao.enemybanner.EntityBannerPattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = BannerRenderer.class)
public class BannerRendererMixin {
    @Inject(method = "renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLjava/util/List;Z)V",
            at = @At("HEAD"), cancellable = true)
    private static void renderPatterns(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, ModelPart modelPart, Material material, boolean p_112081_, List<Pair<Holder<BannerPattern>, DyeColor>> patterns, boolean p_112083_, CallbackInfo ci) {
        patterns.forEach(loomPatternData ->{
            BannerPattern bannerPattern = loomPatternData.getFirst().get();
            Minecraft minecraft = Minecraft.getInstance();
            if(bannerPattern instanceof EntityBannerPattern entityPattern){
                if (minecraft.level != null){
                    LivingEntity livingEntity = EnemyBanner.createOrGetEntity(entityPattern.entityType, minecraft.level);
                    EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
                    poseStack.pushPose();
                    modelPart.translateAndRotate(poseStack);
                    poseStack.translate(0.0D, 0.0D, -0.15D);
                    BannerUtil.renderEntity(entityRenderDispatcher,livingEntity,poseStack, bufferSource, packedLight);
                    poseStack.popPose();
                }
                ci.cancel();
            }

            if (Registry.BANNER_PATTERN.getHolderOrThrow(Registry.BANNER_PATTERN.getResourceKey(bannerPattern).orElseThrow()).is(EnemyBanner.FUNCTION_SILKS_TAG_KEY)){
                String hashName = bannerPattern.getHashname();
                ResourceLocation resource = EnemyBanner.asResource("textures/entity/banner/" + hashName + ".png");
                VertexConsumer basicFlagVertexconsumer = bufferSource.getBuffer(RenderType.entitySolid(resource));
                modelPart.render(poseStack, basicFlagVertexconsumer, packedLight, packedOverlay);
                ci.cancel();
            }

            if (Registry.BANNER_PATTERN.getHolderOrThrow(Registry.BANNER_PATTERN.getResourceKey(bannerPattern).orElseThrow()).is(EnemyBanner.COLOR_SILKS_TAG_KEY)){
                String hashName = bannerPattern.getHashname();
                ResourceLocation bar = EnemyBanner.asResource("textures/entity/banner/" + hashName + ".png");
                VertexConsumer barVertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(bar));
                modelPart.render(poseStack, barVertexConsumer, packedLight, packedOverlay);
                ci.cancel();
            }
        });
    }


}