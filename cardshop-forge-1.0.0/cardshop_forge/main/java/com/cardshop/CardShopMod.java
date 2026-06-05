package com.cardshop;

import com.cardshop.augment.AugmentLoader;
import com.cardshop.augment.AugmentManager;
import com.cardshop.block.ModBlocks;
import com.cardshop.card.CardLoader;
import com.cardshop.card.CardRegistry;
import com.cardshop.augment.AugmentRegistry;
import com.cardshop.entity.ModEntities;
import com.cardshop.item.ModItems;
import com.cardshop.network.ModNetwork;
import com.cardshop.screen.ModMenuTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CardShopMod.MOD_ID)
public class CardShopMod {

    public static final String MOD_ID = "cardshop";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public CardShopMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModEntities.ENTITY_TYPES.register(bus);
        ModMenuTypes.MENU_TYPES.register(bus);
        ModItems.CREATIVE_TABS.register(bus);

        bus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModNetwork.register();
            CardLoader.loadAll();
            AugmentLoader.loadAll();
            LOGGER.info("[CardShop] {} cartes | {} augments | Pret.",
                CardRegistry.getTotalCount(),
                AugmentRegistry.getTotalCount());
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("[CardShop] Serveur demarre.");
    }
}
