package me.xjuppo.parrotletter.listeners;

import me.xjuppo.parrotletter.parrot.ParrotCarrier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class TestListener implements Listener {

    // Used for testing

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        ParrotCarrier parrot = new ParrotCarrier(event.getPlayer(), event.getPlayer());
        parrot.startCycle();
    }
}
