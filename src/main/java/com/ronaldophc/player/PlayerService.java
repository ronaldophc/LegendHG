package com.ronaldophc.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import com.ronaldophc.LegendHG;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStreamReader;

public class PlayerService {

    private static final boolean devMode = LegendHG.getInstance().devMode;

    private static Response getResponse(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute();
    }

    public static String getOriginalUUID(String name) throws Exception {
        try (Response response = getResponse("https://playerdb.co/api/player/minecraft/" + name)) {
            if (!response.isSuccessful()) {
                if (devMode) {
                    LegendHG.logger.warning("Failed to fetch API to get UUID for player: " + name + ". Code: " + response.code());
                }
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

            return new Property("textures", properties.get("value").getAsString(), properties.get("signature").getAsString());
        } catch (IOException e) {
            if (devMode) {
                LegendHG.logger.severe("Failed to get Skin: " + skinName + ". Error: " + e);
            }
            return null;
        }
    }
}