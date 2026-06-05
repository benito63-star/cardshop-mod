package com.cardshop.screen;

import com.cardshop.CardShopMod;
import com.cardshop.block.ShopStandMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, CardShopMod.MOD_ID);

    public static final RegistryObject<MenuType<ShopStandMenu>> SHOP_STAND =
        MENU_TYPES.register("shop_stand",
            () -> IForgeMenuType.create((syncId, inv, data) ->
                new ShopStandMenu(syncId, inv)));

    public static final RegistryObject<MenuType<AugmentMenu>> AUGMENT =
        MENU_TYPES.register("augment",
            () -> IForgeMenuType.create((syncId, inv, data) ->
                new AugmentMenu(syncId, inv)));
}
