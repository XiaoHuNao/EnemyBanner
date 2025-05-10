package com.xiaohunao.enemybanner.AttachmentType;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public class PlayerBannerData {

    public static final Codec<PlayerBannerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("monsterId").forGetter(PlayerBannerData::getMonsterId),
            Codec.INT.fieldOf("usedCount").forGetter(PlayerBannerData::getUsedCount),
            Codec.INT.fieldOf("canUsedCount").forGetter(PlayerBannerData::getCanUsedCount)
    ).apply(instance, PlayerBannerData::new));

    public static final StreamCodec<ByteBuf, PlayerBannerData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, PlayerBannerData::getMonsterId,
            ByteBufCodecs.INT, PlayerBannerData::getUsedCount,
            ByteBufCodecs.INT, PlayerBannerData::getCanUsedCount,
            PlayerBannerData::new
    );

    private String monsterId;
    private int usedCount;
    private int canUsedCount;

    public PlayerBannerData(String monsterId, int usedCount, int canUsedCount) {
        this.monsterId = monsterId;
        this.usedCount = usedCount;
        this.canUsedCount = canUsedCount;
    }

    public String getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(String monsterId) {
        this.monsterId = monsterId;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public int getCanUsedCount() {
        return canUsedCount;
    }

    public void setCanUsedCount(int canUsedCount) {
        this.canUsedCount = canUsedCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlayerBannerData that = (PlayerBannerData) o;
        return usedCount == that.usedCount && canUsedCount == that.canUsedCount && Objects.equals(monsterId, that.monsterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monsterId, usedCount, canUsedCount);
    }

    @Override
    public String toString() {
        return "PlayerBannerData{" +
                "monsterId='" + monsterId + '\'' +
                ", usedCount=" + usedCount +
                ", canUsedCount=" + canUsedCount +
                '}';
    }
}
