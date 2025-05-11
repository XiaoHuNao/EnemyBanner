package com.xiaohunao.enemybanner.handler;

import com.xiaohunao.enemybanner.AttachmentType.AttachmentTypeRegister;
import com.xiaohunao.enemybanner.AttachmentType.PlayerBannerData;
import com.xiaohunao.enemybanner.EnemyBanner;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Map;

@EventBusSubscriber(modid = EnemyBanner.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.DEDICATED_SERVER)
public class ServerEventHandler {

//    @SubscribeEvent
//    public static void dropEnemyBanner(LivingDeathEvent event){
//        Entity damageSourceEntity = event.getSource().getDirectEntity();
//        LivingEntity entity = event.getEntity();
//        if(damageSourceEntity instanceof Player player && entity instanceof Monster monster){
//            ServerPlayer serverPlayer = Objects.requireNonNull(player.getServer()).getPlayerList().getPlayer(player.getUUID());
//            if (serverPlayer != null){
//                ServerStatsCounter stats = serverPlayer.getStats();
//
//                if(BannerConfig.banners.containsKey(monster.getType())){
//                    BannerConfig.Banner banner = BannerConfig.banners.get(monster.getType());
//                    int kills = stats.getValue(Stats.ENTITY_KILLED, monster.getType()) + 1; //补上此次事件死亡的实体
//
//                    if(kills != 0 && kills % banner.basicKills == 0){
//                        ItemStack bannerItem = BannerUtils.createBannerItem(new BannerParameters(monster.getEncodeId()));
//                        serverPlayer.addItem(bannerItem);
//                    }
//                }
//            }
//        }
//    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event){
        if (event.isWasDeath() && event.getOriginal().hasData(AttachmentTypeRegister.PLAYER_BANNER_DATA)) {
            Map<String, PlayerBannerData> originData = event.getOriginal().getData(AttachmentTypeRegister.PLAYER_BANNER_DATA);
            event.getEntity().setData(AttachmentTypeRegister.PLAYER_BANNER_DATA, Map.copyOf(originData));
        }
    }
}
