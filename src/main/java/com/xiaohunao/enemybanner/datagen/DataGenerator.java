package com.xiaohunao.enemybanner.datagen;

import com.google.common.collect.Sets;
import com.xiaohunao.enemybanner.EnemyBanner;
import com.xiaohunao.enemybanner.datagen.provider.BannerPatternTagsProvider;
import com.xiaohunao.enemybanner.datagen.provider.ModLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = EnemyBanner.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        generator.addProvider(event.includeServer(),new BannerPatternTagsProvider(output, Registries.BANNER_PATTERN, provider));
        generator.addProvider(event.includeServer(), new ModLanguageProvider(output, "en_us"));
        generator.addProvider(event.includeServer(), new ModLanguageProvider(output, "zh_cn"));
    }
}