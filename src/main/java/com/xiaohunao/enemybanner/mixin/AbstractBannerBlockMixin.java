package com.xiaohunao.enemybanner.mixin;

import com.xiaohunao.enemybanner.BannerUtil;
import com.xiaohunao.enemybanner.EnemyBanner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(AbstractBannerBlock.class)
public abstract class AbstractBannerBlockMixin extends BaseEntityBlock {
    protected AbstractBannerBlockMixin(Properties p_49224_) {
        super(p_49224_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return !level.isClientSide() ? createTickerHelper(blockEntityType, BlockEntityType.BANNER, BannerUtil::serverBannerBlockTick) : null;
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
        BlockEntity blockEntity = p_60538_.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof BannerBlockEntity bannerBlockEntity) {
            LootContext lootContext = p_60538_.create(LootContextParamSets.BLOCK);
            ServerLevel serverLevel = lootContext.getLevel();
            return serverLevel.getServer().getLootTables().get(EnemyBanner.asResource("blocks/enemy_banner"))
                    .getRandomItems(lootContext);
        }
        return super.getDrops(p_60537_, p_60538_);

    }
}