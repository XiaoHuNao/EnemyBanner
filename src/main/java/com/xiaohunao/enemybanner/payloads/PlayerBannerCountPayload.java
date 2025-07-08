package com.xiaohunao.enemybanner.payloads;

import com.xiaohunao.enemybanner.EnemyBanner;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record PlayerBannerCountPayload(Map<String, Integer> playerBannerCount, String monsterId) implements CustomPacketPayload {
    public static final Type<PlayerBannerCountPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnemyBanner.MODID, "player_banner_count"));

    public static final StreamCodec<ByteBuf, PlayerBannerCountPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(value -> new HashMap<>(), ByteBufCodecs.STRING_UTF8, ByteBufCodecs.INT, Integer.MAX_VALUE), PlayerBannerCountPayload::playerBannerCount,
            ByteBufCodecs.STRING_UTF8, PlayerBannerCountPayload::monsterId,
            PlayerBannerCountPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
