package com.cardshop.augment;

import com.cardshop.CardShopMod;
import com.google.gson.*;
import java.io.*;

public class AugmentLoader {

    private static final Gson GSON  = new Gson();
    private static final String[] FILES =
        {"combat","drops","trade","exploration","special"};

    public static void loadAll() {
        for (String f : FILES)
            load("/data/cardshop/augments/" + f + ".json");
        CardShopMod.LOGGER.info("[AugmentLoader] {} augments charges.",
            AugmentRegistry.getTotalCount());
    }

    private static void load(String path) {
        try (InputStream is = AugmentLoader.class.getResourceAsStream(path)) {
            if (is == null) { CardShopMod.LOGGER.warn("[AugmentLoader] Manquant: {}", path); return; }
            JsonArray arr = GSON.fromJson(new InputStreamReader(is), JsonArray.class);
            for (JsonElement el : arr) {
                try {
                    JsonObject o = el.getAsJsonObject();
                    AugmentRegistry.register(new Augment(
                        o.get("id").getAsString(),
                        o.get("name").getAsString(),
                        Augment.Category.valueOf(o.get("category").getAsString().toUpperCase()),
                        o.get("tier").getAsInt(),
                        o.get("cost").getAsLong(),
                        o.has("description") ? o.get("description").getAsString() : "",
                        o.has("effect")      ? o.get("effect").getAsString()      : "",
                        o.has("amplifier")   ? o.get("amplifier").getAsInt()      : 0,
                        o.has("bonus")       ? o.get("bonus").getAsFloat()        : 0f,
                        o.has("bonus_type")  ? o.get("bonus_type").getAsString()  : ""
                    ));
                } catch (Exception e) {
                    CardShopMod.LOGGER.warn("[AugmentLoader] Invalide: {}", e.getMessage());
                }
            }
        } catch (IOException e) {
            CardShopMod.LOGGER.error("[AugmentLoader] Erreur {}: {}", path, e.getMessage());
        }
    }
}
