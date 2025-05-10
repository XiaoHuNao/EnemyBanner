package com.xiaohunao.enemybanner.gui;

import com.xiaohunao.enemybanner.AttachmentType.PlayerBannerData;
import com.xiaohunao.enemybanner.BannerConfig;
import com.xiaohunao.enemybanner.BannerParameters;
import com.xiaohunao.enemybanner.EnemyBanner;
import com.xiaohunao.enemybanner.gui.widget.BannerCheckBox;
import com.xiaohunao.enemybanner.gui.widget.ListWidget;
import com.xiaohunao.enemybanner.gui.widget.ScrollBar;
import com.xiaohunao.enemybanner.gui.widget.ScrollWidget;
import com.xiaohunao.enemybanner.items.ItemRegister;
import com.xiaohunao.enemybanner.payloads.PlayerBannerDataPayload;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.*;

public class BannerBoxScreen extends ItemCombinerScreen<BannerBoxMenu> {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(EnemyBanner.MODID, "textures/gui/container/banner_box_screen.png");

    private ScrollWidget bannerScroll;
    private ListWidget listWidget;
    private ScrollBar scrollBar;
    private Map<String, BannerCheckBox> bannerCheckBoxMap;

    private boolean hasBannerInput;
    private String silksId;

    private RadioBoxManager radioBoxManager;

    private Map<String, PlayerBannerData> playerBannerData;

    public BannerBoxScreen(BannerBoxMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ResourceLocation.fromNamespaceAndPath(EnemyBanner.MODID, "banner_box_menu"));
        titleLabelX = 10;
        inventoryLabelX = 10;
        this.playerBannerData = menu.getPlayerBannerData();
        this.radioBoxManager = new RadioBoxManager();
        this.bannerCheckBoxMap = new HashMap<>();
    }

    @Override
    protected void subInit() {
        this.playerBannerData = menu.getPlayerBannerData();
        bannerScroll = new ScrollWidget(leftPos + 32, topPos + 17, 112, 44, Component.nullToEmpty("Monster"));
        bannerScroll.setOrientation(LinearLayout.Orientation.HORIZONTAL);
        bannerScroll.setScrollRate(15);

        listWidget = new ListWidget(leftPos, topPos);
        listWidget.setOrientation(LinearLayout.Orientation.HORIZONTAL);
        bannerScroll.setChild(listWidget);

        addWidget(bannerScroll);
        for (String key : playerBannerData.keySet()){
            if (BannerConfig.contains(key) && playerBannerData.get(key).getCanUsedCount() != 0){
                BannerParameters bannerParameters = new BannerParameters(key);
                addNewBannerCheckBox(bannerParameters);
            }
        }

        scrollBar = new ScrollBar.Builder(leftPos + 32, topPos + 64, 112, 6, bannerScroll, BACKGROUND_LOCATION)
                .setTextureBarPos(32, 64)
                .setTextureScrollBlockPos(44, 166)
                .setScrollBlockSize(16, 4)
                .build();

        addWidget(scrollBar.getScrollBlock());
        radioBoxManager.setCheckBoxList(new ArrayList<>(bannerCheckBoxMap.values()));
    }

    @Override
    public void slotChanged(@NotNull AbstractContainerMenu containerToSend, int slotInd, @NotNull ItemStack stack) {
        super.slotChanged(containerToSend, slotInd, stack);
        playerBannerData = getMenu().getPlayerBannerData();
        silksId = (menu.getSlot(1).hasItem() ? menu.getSlot(1).getItem().getItem().toString() : ItemRegister.BASIC_SILKS.getRegisteredName()).split(":")[1];
        hasBannerInput = getMenu().getSlot(0).getItem().is(ItemTags.BANNERS);

        for (BannerCheckBox checkBox : bannerCheckBoxMap.values()) {
            checkBox.getParameters().setSilksId(silksId);
            checkBox.setBannerCount(playerBannerData.get(checkBox.getParameters().getMonsterId()).getCanUsedCount());
        }

        if (radioBoxManager.getSelected() != null){
            sendData(radioBoxManager.getSelected().getParameters());
        }
    }

    private void sendData(BannerParameters parameters){
        PacketDistributor.sendToServer(new PlayerBannerDataPayload(playerBannerData, parameters.getMonsterId(), parameters.getSilksId()));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        scrollBar.render(guiGraphics, mouseX, mouseY, partialTick);
        if (hasBannerInput){
            bannerScroll.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        }
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void addNewBannerCheckBox(BannerParameters parameters){
        BannerCheckBox bannerCheckBox = new BannerCheckBox(parameters.getMonsterId(), parameters, 22, 42, Component.empty());
        bannerCheckBox.setBannerCount(playerBannerData.get(parameters.getMonsterId()).getCanUsedCount());
        listWidget.add(bannerCheckBox);
        bannerCheckBoxMap.put(parameters.getMonsterId(), bannerCheckBox);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderErrorIcon(@NotNull GuiGraphics guiGraphics, int i, int i1) {

    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        scrollBar.getScrollBlock().mouseMoved(mouseX, mouseY);
    }

    private class RadioBoxManager{
        private List<BannerCheckBox> checkBoxList;
        private BannerCheckBox selected;

        public RadioBoxManager(){
            this.checkBoxList = new ArrayList<>();
        }

        private void init(){
            if (!checkBoxList.isEmpty())
                checkBoxList.getFirst().setSelected(true);
            for (BannerCheckBox checkBox : checkBoxList)
                setCheckBoxListener(checkBox);
        }

        public void setCheckBoxList(List<BannerCheckBox> list){
            this.checkBoxList = list;
            init();
        }

        public BannerCheckBox getSelected(){
            return selected;
        }

        private void onSelected(BannerCheckBox selected){
            this.selected = selected;
            getMenu().setSelected(selected.getParameters());
            sendData(selected.getParameters());
        }

        private void setCheckBoxListener(BannerCheckBox checkBox){
            checkBox.addClickListener((banner) -> {
                if (banner.isSelected()){
                    for (BannerCheckBox bannerCheckBox : checkBoxList) {
                        if (!Objects.equals(bannerCheckBox.getId(), banner.getId()))
                            bannerCheckBox.setSelected(false);
                    }
                    onSelected(checkBox);
                }
                return null;
            });
        }
    }

}
