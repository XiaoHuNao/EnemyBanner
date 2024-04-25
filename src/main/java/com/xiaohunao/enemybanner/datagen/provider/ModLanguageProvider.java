package com.xiaohunao.enemybanner.datagen.provider;

import com.google.gson.JsonObject;
import com.xiaohunao.enemybanner.EnemyBanner;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class ModLanguageProvider extends LanguageProvider {
    private final Map<String, String> enData = new TreeMap<>();
    private final Map<String, String> cnData = new TreeMap<>();
    private final String locale;
    private DataGenerator gen;

    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen, EnemyBanner.MOD_ID, locale);
        this.locale = locale;
        this.gen = gen;
    }


    @Override
    protected void addTranslations() {
        add("item.enemybanner.enemy_banner", "Enemy Banner", "敌怪旗");
        add("itemGroup.enemybanner_tab", "Enemy Banner", "敌怪旗");
        add("tell.enemybanner.kill_entity", "%s has defeated the %s-th %s!", "%s 已经打败了 %s 个 %s");
        addBannerPattern("item.enemybanner.basic_silks", "Basic Silks", "基础丝绸");
        addBannerPattern("item.enemybanner.damage_silks", "Damage Silks", "伤害丝绸");
        addBannerPattern("item.enemybanner.inhibit_silks", "Inhibit Silks", "抑制丝绸");
        addBannerPattern("item.enemybanner.loot_silks", "Loot Silks", "抢夺绸缎");
        addBannerPattern("item.enemybanner.pull_silks", "Pull Silks", "吸引绸缎");
        addBannerPattern("item.enemybanner.push_silks", "Push Silks", "排斥绸缎");
        addBannerPattern("item.enemybanner.range_silks", "Range Silks", "范围绸缎");
        addBannerPattern("item.enemybanner.resist_silks", "Resist Silks", "抵抗绸缎");
    }

    @Override
    public void run(CachedOutput cache) {
        this.addTranslations();
        Path path = this.gen.getOutputFolder(DataGenerator.Target.RESOURCE_PACK)
                .resolve(EnemyBanner.MOD_ID).resolve("lang");
        if (this.locale.equals("en_us") && !this.enData.isEmpty()) {
            this.save(this.enData, cache, path.resolve("en_us.json"));
        }

        if (this.locale.equals("zh_cn") && !this.cnData.isEmpty()) {
            this.save(this.cnData, cache, path.resolve("zh_cn.json"));
        }
    }

    private void save(Map<String, String> data, CachedOutput cache, Path target){
        JsonObject json = new JsonObject();
        data.forEach(json::addProperty);
        try {
            DataProvider.saveStable(cache, json, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void add(String key, String en, String cn) {
        if (this.locale.equals("en_us") && !this.enData.containsKey(key)) {
            this.enData.put(key, en);
        } else if (this.locale.equals("zh_cn") && !this.cnData.containsKey(key)) {
            this.cnData.put(key, cn);
        }
    }

    private void addItem(Supplier<? extends Item> key, String en, String cn) {
        this.add(key.get().getDescriptionId(), en, cn);
    }

    private void addBannerPattern(String key, String en, String cn) {
        add(key, en, cn);
        add(key + ".desc", en, cn);
    }
}
