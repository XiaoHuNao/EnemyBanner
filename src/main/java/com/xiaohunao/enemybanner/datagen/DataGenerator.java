package com.xiaohunao.enemybanner.datagen;

import com.xiaohunao.enemybanner.EnemyBanner;
import com.xiaohunao.enemybanner.datagen.provider.BannerPatternTagsProvider;
import com.xiaohunao.enemybanner.datagen.provider.ModLanguageProvider;
import net.minecraft.core.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnemyBanner.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(),new BannerPatternTagsProvider(generator, Registry.BANNER_PATTERN,existingFileHelper));
        generator.addProvider(event.includeServer(), new ModLanguageProvider(generator, "en_us"));
        generator.addProvider(event.includeServer(), new ModLanguageProvider(generator, "zh_cn"));
    }
}