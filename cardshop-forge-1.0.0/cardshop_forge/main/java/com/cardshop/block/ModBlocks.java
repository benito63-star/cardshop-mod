package com.cardshop.block;

import com.cardshop.CardShopMod;
import com.cardshop.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, CardShopMod.MOD_ID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CardShopMod.MOD_ID);

    public static final RegistryObject<ShopStandBlock> SHOP_STAND =
        BLOCKS.register("shop_stand", ShopStandBlock::new);

    public static final RegistryObject<FurnitureBlock> FURNITURE_COUNTER =
        BLOCKS.register("furniture_counter",
            () -> new FurnitureBlock("counter", 1));
    public static final RegistryObject<FurnitureBlock> FURNITURE_SHELF =
        BLOCKS.register("furniture_shelf",
            () -> new FurnitureBlock("shelf", 2));
    public static final RegistryObject<FurnitureBlock> FURNITURE_REGISTER =
        BLOCKS.register("furniture_register",
            () -> new FurnitureBlock("register", 3));
    public static final RegistryObject<FurnitureBlock> FURNITURE_DISPLAY =
        BLOCKS.register("furniture_display",
            () -> new FurnitureBlock("display", 2));
    public static final RegistryObject<FurnitureBlock> FURNITURE_SIGN =
        BLOCKS.register("furniture_sign",
            () -> new FurnitureBlock("sign", 1));
    public static final RegistryObject<FurnitureBlock> FURNITURE_FOUNTAIN =
        BLOCKS.register("furniture_fountain",
            () -> new FurnitureBlock("fountain", 1));

    public static final RegistryObject<BlockEntityType<ShopStandBlockEntity>>
        SHOP_STAND_BE = BLOCK_ENTITIES.register("shop_stand",
            () -> BlockEntityType.Builder
                .of(ShopStandBlockEntity::new, SHOP_STAND.get())
                .build(null));

    // Enregistre aussi les BlockItems
    static {
        registerBlockItem("shop_stand",         SHOP_STAND);
        registerBlockItem("furniture_counter",  FURNITURE_COUNTER);
        registerBlockItem("furniture_shelf",    FURNITURE_SHELF);
        registerBlockItem("furniture_register", FURNITURE_REGISTER);
        registerBlockItem("furniture_display",  FURNITURE_DISPLAY);
        registerBlockItem("furniture_sign",     FURNITURE_SIGN);
        registerBlockItem("furniture_fountain", FURNITURE_FOUNTAIN);
    }

    private static void registerBlockItem(String name,
            RegistryObject<? extends Block> block) {
        ModItems.ITEMS.register(name,
            () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
