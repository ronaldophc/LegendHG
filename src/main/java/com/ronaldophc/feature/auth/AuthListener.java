package com.ronaldophc.feature.auth;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PublicKey;

public class AuthListener implements Listener {

    private final KeyPair keyPair;

    public AuthListener() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024); // Tamanho da chave
        this.keyPair = keyPairGenerator.generateKeyPair();
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String playerName = event.getName();
        String playerUUID = event.getUniqueId().toString();

        try {
            // Gerar chave secreta para o jogador
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();

            // Validar o jogador
            boolean isPremium = validatePremiumPlayer(
                    playerName,
                    "", // Base server ID vazio
                    secretKey,
                    keyPair.getPublic()
            );

            if (isPremium) {
                System.out.println("Jogador autenticado como premium: " + playerName);
            } else {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                        "Você precisa de uma conta original para entrar com este nick.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    "Ocorreu um erro durante a autenticação. Tente novamente.");
        }
    }

    public static boolean validatePremiumPlayer(String username, String serverId, SecretKey secretKey, PublicKey publicKey) {
        try {
            // Gerar o hash (serverId) com os dados do jogador
            String generatedServerId = generateServerId(serverId, publicKey, secretKey);
            System.out.println("Server ID gerado: " + generatedServerId);

            // Verificar na Mojang com a API de autenticação
            String urlString = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=PHC02&serverId=ad72fe1efe364e6eb78c644a9fba1d30";
            System.out.println("URL de validação: " + urlString);
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            System.out.println("Response code: " + connection.getResponseCode());
            // Se a resposta for 200, o jogador passou pela autenticação padrão
            return connection.getResponseCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String generateServerId(String baseServerId, PublicKey publicKey, SecretKey secretKey) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(baseServerId.getBytes("ISO_8859_1"));
        messageDigest.update(secretKey.getEncoded());
        messageDigest.update(publicKey.getEncoded());
        byte[] digestData = messageDigest.digest();
        return new BigInteger(digestData).toString(16);
    }
}
