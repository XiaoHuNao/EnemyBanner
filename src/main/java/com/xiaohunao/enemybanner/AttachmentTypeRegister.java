package com.xiaohunao.enemybanner;

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

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Map<String, Integer>>> PLAYER_BANNER_COUNT = ATTACHMENT_TYPES.register(
            "player_banner_count",
            () -> AttachmentType.builder((Supplier<Map<String, Integer>>) HashMap::new).serialize(Codec.unboundedMap(Codec.STRING, Codec.INT)).copyOnDeath().build()
    );

    public static void register(IEventBus bus){
        ATTACHMENT_TYPES.register(bus);
    }
}
