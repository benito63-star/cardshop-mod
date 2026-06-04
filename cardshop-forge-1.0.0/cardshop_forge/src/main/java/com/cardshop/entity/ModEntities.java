package com.cardshop.entity;

import com.cardshop.CardShopMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CardShopMod.MOD_ID);

    public static final RegistryObject<EntityType<CardMerchantEntity>>
        CARD_MERCHANT = ENTITY_TYPES.register("card_merchant",
            () -> EntityType.Builder
                .<CardMerchantEntity>of(CardMerchantEntity::new,
                    MobCategory.MISC)
                .sized(0.6f, 1.95f)
                .build("card_merchant"));
}
