package com.ronaldophc.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.MySQL.PlayerField;
import com.ronaldophc.constant.MySQL.Tables;
import com.ronaldophc.database.MySQLManager;
import com.ronaldophc.database.PlayerRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class PlayerService {

    private static final boolean devMode = LegendHG.getInstance().devMode;

    private static Response getResponse(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute();
    }

    public static String getOriginalUUID(String name) throws Exception {
        try (Response response = getResponse("https://api.mojang.com/users/profiles/minecraft/" + name)) {
            if (!response.isSuccessful()) {
                if (devMode) {
                    LegendHG.logger.warning("Failed to fetch API to get UUID for player in Mojang: " + name + ". Code: " + response.code());
                }
                return getOriginalUUIDPlayerDB(name);
            }

            if (response.body() == null) {
                return null;
            }
            String responseBody = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            String id = jsonObject.get("id").getAsString();

            if (devMode) {
                LegendHG.logger.info("Original UUID of player " + name + ": " + id);
            }
            return id;
        } catch (IOException e) {
            if (devMode) {
                LegendHG.logger.severe("Failed to get UUID for player: " + name + ". Error: " + e);
            }
            return null;
        }
    }

    public static String getOriginalUUIDPlayerDB(String name) throws Exception {
        try (Response response = getResponse("https://playerdb.co/api/player/minecraft/" + name)) {
            if (!response.isSuccessful()) {
                if (devMode) {
                    LegendHG.logger.warning("Failed to fetch API to get UUID for player in PlayerDB: " + name + ". Code: " + response.code());
                }
                return null;
            }

            if (response.body() == null) {
                return null;
            }
            String responseBody = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonObject player = jsonObject.getAsJsonObject("data")
                    .getAsJsonObject("player");

            String id = player.get("id").getAsString();
            if (devMode) {
                LegendHG.logger.info("Original UUID of player " + name + ": " + id);
            }
            return id;
        } catch (IOException e) {
            if (devMode) {
                LegendHG.logger.severe("Failed to get UUID for player: " + name + ". Error: " + e);
            }
            return null;
        }
    }

    public static Property getSkinProperty(String skinName) throws Exception {
        String uuid = getOriginalUUID(skinName);
        if (uuid == null) return null;

        try (Response response = getResponse("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false")) {
            if (!response.isSuccessful()) {
                if (devMode) {
                    LegendHG.logger.warning("Failed to fetch API to get Skin: " + skinName + ". Code: " + response.code());
                }
                return null;
            }

            JsonObject properties = JsonParser.parseReader(new InputStreamReader(response.body().byteStream()))
                    .getAsJsonObject()
                    .getAsJsonArray("properties")
                    .get(0)
                    .getAsJsonObject();

//            String base64Value = properties.get("value").getAsString();
//            String decodedValue = new String(Base64.getDecoder().decode(base64Value));
//
//            LegendHG.logger.info(decodedValue);

            return new Property("textures", properties.get("value").getAsString(), properties.get("signature").getAsString());
        } catch (IOException e) {
            if (devMode) {
                LegendHG.logger.severe("Failed to get Skin: " + skinName + ". Error: " + e);
            }
            return null;
        }
    }

    public static String getNameByUUID(UUID uuid) {
        try {
            return MySQLManager.getString(String.valueOf(uuid), Tables.PLAYER.getTableName(), PlayerField.NAME.getFieldName());
        } catch (Exception e) {
            LegendHG.logger.severe("Failed to get name by UUID: " + uuid + ". Error: " + e);
            return null;
        }
    }

    public static boolean isPlayerRegistered(String name) {
        try {
            return PlayerRepository.isPlayerRegisteredByName(name);
        } catch (Exception e) {
            LegendHG.logger.severe("Failed to check if player is registered: " + name + ". Error: " + e);
            return false;
        }
    }

    public static UUID getUUIDByName(String name) {
        try {
            String uuid = MySQLManager.getStringByName(name, Tables.PLAYER.getTableName(), PlayerField.UUID.getFieldName());
            if (uuid == null) {
                return null;
            }
            return UUID.fromString(uuid);
        } catch (Exception e) {
            LegendHG.logger.severe("Failed to get UUID by name: " + name + ". Error: " + e);
            return null;
        }
    }
}