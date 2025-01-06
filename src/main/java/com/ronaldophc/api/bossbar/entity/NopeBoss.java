package com.ronaldophc.api.bossbar.entity;

import com.ronaldophc.api.bossbar.BossBarEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class NopeBoss extends BossBarEntity {

    public NopeBoss(Player player) {
        super(player);
    }

    @Override
    public void spawn() {
        
    }

    @Override
    public void remove() {
      
    }

    @Override
    public void update() {
        
    }

    @Override
    public void move(PlayerMoveEvent event) {
        
    }

    @Override
    public boolean setHealth(float percent) {
        return false;
    }

    @Override
    public boolean setTitle(String title) {
        return false;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public void setAlive(boolean alive) {
 
    }
}
