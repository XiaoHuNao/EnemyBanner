package com.xiaohunao.enemybanner.payloads;

import com.xiaohunao.enemybanner.AttachmentType.AttachmentTypeRegister;
import com.xiaohunao.enemybanner.BannerParameters;
import com.xiaohunao.enemybanner.gui.BannerBoxMenu;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class ServerPayloadHandler{
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void handle(@NotNull PlayerBannerDataPayload data, @NotNull IPayloadContext context){
        context.player().setData(AttachmentTypeRegister.PLAYER_BANNER_DATA.get(), data.playerBannerDataMap());
        BannerBoxMenu bannerBoxMenu = (BannerBoxMenu) context.player().containerMenu;
        bannerBoxMenu.setSelected(new BannerParameters(data.monsterId(), DyeColor.WHITE.getId(), data.silksId()));
    }
}
