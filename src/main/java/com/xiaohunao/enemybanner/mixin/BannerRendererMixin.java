package com.xiaohunao.enemybanner.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.xiaohunao.enemybanner.EnemyBanner;
import com.xiaohunao.enemybanner.EnemyBannerCap;
import com.xiaohunao.enemybanner.EntityBannerPattern;
import com.xiaohunao.enemybanner.FunctionBannerPattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.LazyOptional;
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
    @Final
    @Shadow
    private ModelPart flag;
    @Unique
    private static BannerBlockEntity blockEntity = null;


    @Inject(method = "renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLjava/util/List;Z)V",
            at = @At("HEAD"), cancellable = true)
    private static void renderPatterns(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, ModelPart modelPart, Material material, boolean p_112081_, List<Pair<Holder<BannerPattern>, DyeColor>> patterns, boolean p_112083_, CallbackInfo ci) {
        patterns.forEach(loomPatternData ->{
            BannerPattern bannerPattern = loomPatternData.getFirst().get();
            Minecraft minecraft = Minecraft.getInstance();
            if(bannerPattern instanceof EntityBannerPattern entityPattern){
                DyeColor dyeColor = loomPatternData.getSecond();
                ResourceLocation bar = EnemyBanner.asResource("textures/banner/" + dyeColor.getName() + ".png");
                VertexConsumer barVertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(bar));
                modelPart.render(poseStack, barVertexConsumer, packedLight, packedOverlay);

                EntityType<?> entityType = entityPattern.entityType;
                Entity entity = entityType.create(minecraft.level);
                if (minecraft.level != null && entity != null){
                    poseStack.pushPose();
                    modelPart.translateAndRotate(poseStack);
                    poseStack.translate(0.0D, 0.0D, -0.15D);
                    renderEntity(entity, poseStack, bufferSource, packedLight);
                    poseStack.popPose();
                }
                ci.cancel();
            }

            if (bannerPattern instanceof FunctionBannerPattern functionPattern){
                String hashName = functionPattern.getHashname();
                ResourceLocation resource = EnemyBanner.asResource("textures/banner/" + hashName + ".png");
                VertexConsumer basicFlagVertexconsumer = bufferSource.getBuffer(RenderType.entitySolid(resource));
                modelPart.render(poseStack, basicFlagVertexconsumer, packedLight, packedOverlay);
                ci.cancel();
            }
        });
    }
    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLjava/util/List;)V"))
    public void render(BannerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo ci) {
    }





    private static void renderEntity(Entity entity, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();
        EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        AABB boundingBox = entity.getBoundingBox();
        var width = boundingBox.getXsize();
        var height = boundingBox.getYsize();

        if(width > 0.6) {
            width *= 1f/(width/0.6f);
            height = boundingBox.getYsize()*(width/boundingBox.getXsize());
        }
        if(height > 2.0) {
            width *= 1f/(height/2f);
            height = boundingBox.getYsize()*(width/boundingBox.getXsize());
        }
        poseStack.translate(0.0, 1.2+(((1.3*height)/2.0)/2.0), 0.0);

        poseStack.scale(0.6f, 0.6f, 0.02f);
        poseStack.scale((float) (width/boundingBox.getXsize()), (float) (width/boundingBox.getXsize()), (float) (width/boundingBox.getXsize()));

        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(-30.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(225.0f));

        entityRenderDispatcher.setRenderShadow(false);
        entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0f, 0f, poseStack, bufferSource, packedLight);
        entityRenderDispatcher.setRenderShadow(true);
    }
}