package com.xiaohunao.enemybanner.handler;

import com.xiaohunao.enemybanner.AttachmentTypeRegister;
import com.xiaohunao.enemybanner.BannerConfig;
import com.xiaohunao.enemybanner.EnemyBanner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = EnemyBanner.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.DEDICATED_SERVER)
public class ServerEventHandler {
    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath() && event.getOriginal().hasData(AttachmentTypeRegister.PLAYER_BANNER_COUNT)) {
            Map<String, Integer> originData = event.getOriginal().getData(AttachmentTypeRegister.PLAYER_BANNER_COUNT);
            event.getEntity().setData(AttachmentTypeRegister.PLAYER_BANNER_COUNT, Map.copyOf(originData));
        }
    }

    @SubscribeEvent
    public static void onPlayKillMob(LivingDeathEvent event) {
        //玩家击杀生物获得的相应的旗帜点数
        Entity entity = event.getSource().getEntity();
        if (entity instanceof Player player) {
            String key = EntityType.getKey(event.getEntity().getType()).toString();
            if (BannerConfig.contains(key)) {
                Map<String, Integer> tmpPlayerData = player.hasData(AttachmentTypeRegister.PLAYER_BANNER_COUNT) ? player.getData(AttachmentTypeRegister.PLAYER_BANNER_COUNT) : new HashMap<>();
                Map<String, Integer> playerData = new HashMap<>(tmpPlayerData);
                Integer lastValue = playerData.getOrDefault(key, 0);
                playerData.put(key, Math.max(lastValue + 1, 0));
                player.setData(AttachmentTypeRegister.PLAYER_BANNER_COUNT, playerData);

                int basicKills = BannerConfig.getBasicKills(key);
                int kills = playerData.get(key);
                if (kills % basicKills == 0) {
                    String k;
                    if (kills == basicKills) {
                        //“<玩家名>击败了<X>个<生物名>”
                        k = EnemyBanner.asDescriptionId("message.player.killcount.short");
                    } else {
                        //“<玩家名>击败了<X>个<生物名>，该旗帜已在旗帜盒中解锁”
                        k = EnemyBanner.asDescriptionId("message.player.killcount");
                    }
                    player.sendSystemMessage(Component.translatable(k, player.getName(), kills, event.getEntity().getName()).withColor(0xfff014));
                }
            }
        }
    }
}
