package com.cardshop.item;

import com.cardshop.CardShopMod;
import com.cardshop.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, CardShopMod.MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CardShopMod.MOD_ID);

    // ── Items de jeu ─────────────────────────────────────────────────
    public static final RegistryObject<CardItem>        CARD =
        ITEMS.register("card",         CardItem::new);
    public static final RegistryObject<BoosterPackItem> BOOSTER_PACK =
        ITEMS.register("booster_pack", BoosterPackItem::new);
    public static final RegistryObject<AugmentItem>     AUGMENT =
        ITEMS.register("augment",      AugmentItem::new);

    // ── Ressources économiques ────────────────────────────────────────
    public static final RegistryObject<ResourceItem> ENCHANTED_PAPER =
        ITEMS.register("enchanted_paper", () -> new ResourceItem(
            "Papier Enchante", ResourceItem.ResourceRarity.BASIC,
            "Papier + Redstone + Lapis", 5L));
    public static final RegistryObject<ResourceItem> RARE_INK =
        ITEMS.register("rare_ink", () -> new ResourceItem(
            "Encre Rare", ResourceItem.ResourceRarity.UNCOMMON,
            "Sacoche de calmar + Lapis", 15L));
    public static final RegistryObject<ResourceItem> GOLD_FOIL =
        ITEMS.register("gold_foil", () -> new ResourceItem(
            "Feuille d Or", ResourceItem.ResourceRarity.RARE,
            "Pepites d or + Papier Enchante", 40L));
    public static final RegistryObject<ResourceItem> MANA_CRYSTAL =
        ITEMS.register("mana_crystal", () -> new ResourceItem(
            "Cristal de Mana", ResourceItem.ResourceRarity.EXOTIC,
            "Amethyste + Emeraude + Cristal End", 120L));
    public static final RegistryObject<ResourceItem> VOID_ESSENCE =
        ITEMS.register("void_essence", () -> new ResourceItem(
            "Essence du Vide", ResourceItem.ResourceRarity.MYTHIC,
            "Ender + Ghast + Etoile Nether", 400L));

    // ── Onglet créatif ────────────────────────────────────────────────
    public static final RegistryObject<CreativeModeTab> CARD_SHOP_TAB =
        CREATIVE_TABS.register("tab", () ->
            CreativeModeTab.builder()
                .title(Component.literal("\u00a76\u2663 Card Shop"))
                .icon(() -> new ItemStack(BOOSTER_PACK.get()))
                .displayItems((params, output) -> {

                    // ── Section : Boutique ──────────────────────────
                    output.accept(ModBlocks.SHOP_STAND.get());

                    // ── Section : Meubles ───────────────────────────
                    output.accept(ModBlocks.FURNITURE_COUNTER.get());
                    output.accept(ModBlocks.FURNITURE_SHELF.get());
                    output.accept(ModBlocks.FURNITURE_REGISTER.get());
                    output.accept(ModBlocks.FURNITURE_DISPLAY.get());
                    output.accept(ModBlocks.FURNITURE_SIGN.get());
                    output.accept(ModBlocks.FURNITURE_FOUNTAIN.get());

                    // ── Section : Cartes & Boosters ─────────────────
                    output.accept(BOOSTER_PACK.get());
                    output.accept(CARD.get());

                    // ── Section : Augments ──────────────────────────
                    output.accept(AUGMENT.get());

                    // ── Section : Ressources économiques ────────────
                    output.accept(ENCHANTED_PAPER.get());
                    output.accept(RARE_INK.get());
                    output.accept(GOLD_FOIL.get());
                    output.accept(MANA_CRYSTAL.get());
                    output.accept(VOID_ESSENCE.get());
                })
                .build());

    // ── Enregistrement des BlockItems ────────────────────────────────
    // (appelé statiquement depuis ModBlocks)
    public static void registerBlockItem(String name,
            java.util.function.Supplier<? extends net.minecraft.world.level.block.Block> block) {
        ITEMS.register(name,
            () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
