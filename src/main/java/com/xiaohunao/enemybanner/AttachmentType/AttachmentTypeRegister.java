package com.xiaohunao.enemybanner.AttachmentType;

import com.xiaohunao.enemybanner.EnemyBanner;
import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AttachmentTypeRegister {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, EnemyBanner.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Map<String, PlayerBannerData>>> PLAYER_BANNER_DATA = ATTACHMENT_TYPES.register(
            "player_banner_data",
            () -> AttachmentType.builder((Supplier<Map<String, PlayerBannerData>>) HashMap::new).serialize(Codec.unboundedMap(Codec.STRING, PlayerBannerData.CODEC)).copyOnDeath().build()
    );
    public static void register(IEventBus bus){
        ATTACHMENT_TYPES.register(bus);
    }
}
