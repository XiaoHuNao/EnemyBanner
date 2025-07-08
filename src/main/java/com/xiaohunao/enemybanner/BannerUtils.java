package com.xiaohunao.enemybanner;

import com.xiaohunao.enemybanner.items.ItemRegister;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BannerUtils {
    public static Logger LOGGER = LogUtils.getLogger();

    public static List<Entity> getEntityInRange(Level level, String EntityId, BlockPos pos, int range){
        Optional<EntityType<?>> entityType = EntityType.byString(EntityId);
        return entityType.map(type -> getEntityInRange(level, type, pos, range)).orElseGet(List::of);
    }

    public static List<Entity> getEntityInRange(Level level, EntityType<?> entityType, BlockPos pos, int range){
        AABB aabb = new AABB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range);
        return level.getEntities((Entity) null, aabb, entity -> entityType.equals(entity.getType()));
    }

    public static void renderBannerFlag(BannerParameters parameters, ModelPart flag,
                                        PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay){

        if (!ItemRegister.SILKS_MAP.containsKey(parameters.getSilksId()))
            LOGGER.error("unknown silks id:{}", parameters.getSilksId());
        VertexConsumer silksBuffer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.fromNamespaceAndPath(EnemyBanner.MODID,
                "textures/entity/banner/" + parameters.getSilksId() + ".png")));
        flag.render(poseStack, silksBuffer, packedLight, packedOverlay);
        VertexConsumer colorBuffer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.fromNamespaceAndPath(EnemyBanner.MODID,
                "textures/entity/banner/" + DyeColor.byId(parameters.getDyeColorId()) + "_silks.png")));
        flag.render(poseStack, colorBuffer, packedLight, packedOverlay);

        poseStack.pushPose();
        EntityType.byString(parameters.getMonsterId()).ifPresent(entityType -> {
            Level level = Minecraft.getInstance().level;
            EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            if (level != null) {
                Entity entity = entityType.create(level);
                if (entity instanceof LivingEntity){
                    flag.translateAndRotate(poseStack);
                    poseStack.translate(0.0D, 0.0D, -0.15D);
                    BannerUtils.renderEntityToBanner(entityRenderDispatcher, (LivingEntity) entity, poseStack, bufferSource, packedLight);
                }
            }
        poseStack.popPose();
        });
    }

    public static void renderEntityToBanner(EntityRenderDispatcher entityRenderDispatcher, LivingEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        AABB boundingBox = entity.getBoundingBox();
        double width = boundingBox.getXsize();
        double height = boundingBox.getYsize();

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
