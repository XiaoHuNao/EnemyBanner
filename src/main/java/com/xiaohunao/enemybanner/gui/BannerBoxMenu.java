package com.xiaohunao.enemybanner.gui;

import com.xiaohunao.enemybanner.AttachmentType.AttachmentTypeRegister;
import com.xiaohunao.enemybanner.AttachmentType.PlayerBannerData;
import com.xiaohunao.enemybanner.BannerConfig;
import com.xiaohunao.enemybanner.BannerParameters;
import com.xiaohunao.enemybanner.items.ItemRegister;
import com.xiaohunao.enemybanner.items.SilksItem;
import com.xiaohunao.enemybanner.payloads.PlayerBannerDataPayload;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BannerBoxMenu extends ItemCombinerMenu {
    public static final Logger LOGGER = LogUtils.getLogger();

    private Map<String, PlayerBannerData> playerBannerDataMap;

    private Inventory playerInv;

    private BannerParameters parameters;

    public BannerBoxMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
        this(containerId, playerInv, ContainerLevelAccess.NULL);
    }

    public BannerBoxMenu(int containerId, Inventory playerInv, ContainerLevelAccess access){
        super(Menus.BANNER_BOX_MENU.get(), containerId, playerInv, access);
        this.playerInv = playerInv;
        playerBannerDataMap = new HashMap<>();
        loadBannerMap();
        parameters = new BannerParameters(!playerBannerDataMap.isEmpty() ? playerBannerDataMap.keySet().iterator().next() : "minecraft:zombie");
    }

    @Override
    protected boolean mayPickup(@NotNull Player player, boolean hasStack) {
        return hasStack;
    }

    @Override
    protected void onTake(@NotNull Player player, @NotNull ItemStack itemStack) {
        ItemStack bannerInput = this.inputSlots.getItem(0);
        ItemStack silks = this.inputSlots.getItem(1);
        bannerInput.setCount(bannerInput.getCount() - 1);
        if (!silks.isEmpty()){
            silks.setCount(silks.getCount() - 1);
        }
        PlayerBannerData data = playerBannerDataMap.get(parameters.getMonsterId());
        data.setUsedCount(data.getUsedCount() + 1);
        data.setCanUsedCount(Math.max(0, data.getCanUsedCount() - 1));
        playerBannerDataMap.put(parameters.getMonsterId(), data);
        player.setData(AttachmentTypeRegister.PLAYER_BANNER_DATA, playerBannerDataMap);
        loadBannerMap();
        createResult();
    }

    @Override
    protected boolean isValidBlock(@NotNull BlockState blockState) {
        return true;
    }


    @Override
    public void createResult() {
        if (getSlot(0).hasItem() && playerBannerDataMap.get(parameters.getMonsterId()).getCanUsedCount() > 0){
            ItemStack stack = ItemRegister.ENEMY_BANNER.toStack();
            stack.set(BannerParameters.BANNER_DATA_COMPONENT, parameters);
            this.resultSlots.setItem(0, stack);
            this.broadcastChanges();
        }
        else {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        }
    }

    public void setSelected(BannerParameters parameters){
        this.parameters = parameters;
        createResult();
    }

    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 11, 16, itemStack -> itemStack.getItem() instanceof BannerItem)
                .withSlot(1, 11, 54, itemStack -> itemStack.getItem() instanceof SilksItem)
                .withResultSlot(2, 149, 35)
                .build();
    }

    private void loadBannerMap(){
        Player player = playerInv.player;
        if (player.hasData(AttachmentTypeRegister.PLAYER_BANNER_DATA.get())) {
            playerBannerDataMap = player.getData(AttachmentTypeRegister.PLAYER_BANNER_DATA.get());
        }
        if(!player.level().isClientSide()){
            ServerPlayer serverPlayer = player.getServer().getPlayerList().getPlayer(player.getUUID());
            Set<EntityType<?>> entityTypes = BannerConfig.banners.keySet();
            ServerStatsCounter status = serverPlayer.getStats();
            Map<String, Integer> killCount = new HashMap<>();
            for (EntityType<?> entityType : entityTypes ){
                int value = status.getValue(Stats.ENTITY_KILLED, entityType);
                killCount.put(EntityType.getKey(entityType).toString(), value);
            }
            for (String key : killCount.keySet()){
                if (playerBannerDataMap.containsKey(key)) {
                    PlayerBannerData playerBannerData = playerBannerDataMap.get(key);
                    BannerConfig.Banner banner = BannerConfig.getBanner(key);
                    if (banner != null){
                        playerBannerData.setCanUsedCount(Math.max(0, (killCount.get(key) / banner.basicKills) - playerBannerData.getUsedCount()));
                    }
                } else
                    playerBannerDataMap.put(key, new PlayerBannerData(key, 0, killCount.get(key)));
            }
            PacketDistributor.sendToPlayer(serverPlayer, new PlayerBannerDataPayload(playerBannerDataMap, playerBannerDataMap.keySet().iterator().next(), ItemRegister.BASIC_SILKS.getId().getPath()));
        }
    }

    public Map<String, PlayerBannerData> getPlayerBannerData(){
        return this.playerBannerDataMap;
    }
}
