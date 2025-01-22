package com.ronaldophc.api.skin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.ronaldophc.player.PlayerService;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SkinAPI {

    public static HashMap<UUID, Property> playerSkin = new HashMap<>();

    public static boolean fixPlayerSkin(Player player) throws Exception {
        return updatePlayerSkinFinal(player, player.getName());
    }

    public static boolean changePlayerSkin(Player player, String skinName) throws Exception {
        return updatePlayerSkinFinal(player, skinName);
    }

    public static void changePlayerSkin(Player player, Property property) {
        setProfileTextures(player, property);
    }

    public static boolean updatePlayerSkinFinal(Player player, String skinName) throws Exception {
        Property texture = PlayerService.getSkinProperty(skinName);
        if (texture == null) {
            return false;
        }
        setProfileTextures(player, texture);
        return true;
    }

    public static void setProfileTextures(Player player, Property property) {
        GameProfile profile = ((CraftPlayer) player).getProfile();
        profile.getProperties().clear();
        profile.getProperties().put("textures", property);

        // Salvar skin atual
        playerSkin.put(player.getUniqueId(), property);
    }

}
