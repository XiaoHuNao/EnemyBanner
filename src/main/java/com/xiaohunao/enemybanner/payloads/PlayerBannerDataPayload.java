package com.xiaohunao.enemybanner.payloads;

import com.xiaohunao.enemybanner.AttachmentType.PlayerBannerData;
import com.xiaohunao.enemybanner.EnemyBanner;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record PlayerBannerDataPayload(Map<String, PlayerBannerData> playerBannerDataMap, String monsterId, String silksId) implements CustomPacketPayload {
    public static final Type<PlayerBannerDataPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnemyBanner.MODID, "player_banner"));

    public static final StreamCodec<ByteBuf, PlayerBannerDataPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(value -> new HashMap<>(), ByteBufCodecs.STRING_UTF8, PlayerBannerData.STREAM_CODEC, Integer.MAX_VALUE), PlayerBannerDataPayload::playerBannerDataMap,
            ByteBufCodecs.STRING_UTF8, PlayerBannerDataPayload::monsterId,
            ByteBufCodecs.STRING_UTF8, PlayerBannerDataPayload::silksId,
            PlayerBannerDataPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
