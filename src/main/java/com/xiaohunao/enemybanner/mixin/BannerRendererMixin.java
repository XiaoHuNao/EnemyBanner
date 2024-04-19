package com.xiaohunao.enemybanner.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.xiaohunao.enemybanner.EnemyBanner;
import com.xiaohunao.enemybanner.EntityBannerPattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
                Entity entity = entityPattern.entityType.create(minecraft.level);
                if (minecraft.level != null && entity != null){
                    poseStack.pushPose();
                    modelPart.translateAndRotate(poseStack);
                    poseStack.translate(0.0D, 0.0D, -0.15D);
                    renderEntity(entity,poseStack, bufferSource, packedLight);
                    poseStack.popPose();
                }
                ci.cancel();
            }

            if (BuiltInRegistries.BANNER_PATTERN.wrapAsHolder(bannerPattern).is(EnemyBanner.FUNCTION_SILKS_TAG_KEY)){
                String hashName = bannerPattern.getHashname();
                ResourceLocation resource = EnemyBanner.asResource("textures/banner/" + hashName + ".png");
                VertexConsumer basicFlagVertexconsumer = bufferSource.getBuffer(RenderType.entitySolid(resource));
                modelPart.render(poseStack, basicFlagVertexconsumer, packedLight, packedOverlay);
                ci.cancel();
            }

            if (BuiltInRegistries.BANNER_PATTERN.wrapAsHolder(bannerPattern).is(EnemyBanner.COLOR_SILKS_TAG_KEY)){
                String hashName = bannerPattern.getHashname();
                ResourceLocation bar = EnemyBanner.asResource("textures/banner/" + hashName + ".png");
                VertexConsumer barVertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(bar));
                modelPart.render(poseStack, barVertexConsumer, packedLight, packedOverlay);
                ci.cancel();
            }
        });
    }

    private static void renderEntity(Entity entity,PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();
        EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        AABB boundingBox = entity.getBoundingBox();
        double width = boundingBox.getXsize();
        double height = boundingBox.getYsize();

        double max_width = 0.6;
        double max_height = 2.0;

        if(width > max_width) {
            double scale = max_width / width;
            width *= scale;
            height *= scale;
        }
        if(height > max_height) {
            double scale = max_height / height;
            width *= scale;
            height *= scale;
        }

        poseStack.translate(0.0, 1.2 + ((1.3 * height) / 4.0), 0.0);

        double scale = width / boundingBox.getXsize();
        poseStack.scale(0.6f, 0.6f, 0.02f);
        poseStack.scale((float) scale, (float) scale, (float) scale);

        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(-30.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(225.0f));

        entityRenderDispatcher.setRenderShadow(false);
        entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0f, 0f, poseStack, bufferSource, packedLight);
        entityRenderDispatcher.setRenderShadow(true);
    }
}