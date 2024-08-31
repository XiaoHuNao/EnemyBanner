package com.xiaohunao.enemybanner;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(EnemyBanner.MOD_ID)
public class EnemyBanner {
    public static final String MOD_ID = "enemybanner";
    private static final Logger log = LoggerFactory.getLogger("EnemyBanner");
    public EnemyBanner(IEventBus modEventBus, ModContainer modContainer) {
//        NeoForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
