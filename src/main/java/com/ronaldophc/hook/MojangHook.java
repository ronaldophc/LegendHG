package com.ronaldophc.hook;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import com.ronaldophc.LegendHG;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MojangHook {

    public static String getOriginalUUID(String name) throws Exception {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        if (connection.getResponseCode() != 200) {
            LegendHG.logger.warning("Não foi possível obter o UUID para o jogador: " + name);
            return null;
        }

        JsonObject profileJson = new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
        return profileJson.get("id").getAsString();
    }

    public static Property getSkinProperty(String skinName) throws Exception {
        String uuid = MojangHook.getOriginalUUID(skinName);
        if (uuid == null) {
            return null;
        }
        String skinUrl = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false";
        HttpURLConnection connection = (HttpURLConnection) new URL(skinUrl).openConnection();
        connection = (HttpURLConnection) new URL(skinUrl).openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        if (connection.getResponseCode() != 200) {
            LegendHG.logger.warning("Não foi possível obter a skin para o jogador: " + skinName);
            return null;
        }

        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        JsonObject skinJson = new JsonParser().parse(reader).getAsJsonObject();
        JsonObject properties = skinJson.getAsJsonArray("properties").get(0).getAsJsonObject();
        String skinValue = properties.get("value").getAsString();
        String skinSignature = properties.get("signature").getAsString();

        // Atualizar o GameProfile do jogador
        return new Property("textures", skinValue, skinSignature);
    }
}
