package me.xjuppo.parrotletter.listeners;

import me.xjuppo.parrotletter.ParrotLetter;
import me.xjuppo.parrotletter.parrot.ParrotCarrier;
import me.xjuppo.parrotletter.parrot.ParrotState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class EntityListener implements Listener {

    int inventorySize;

    public EntityListener() {
        try {
            inventorySize = ParrotLetter.plugin.getConfig().getInt("parrot_inventory_size");
        }
        catch (Exception e) {
            ParrotLetter.plugin.getLogger().severe("Couldn't load inventory size from config, setting default value");
            inventorySize = 9;
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (ParrotCarrier.getEntities().keySet().contains(event.getEntity())) {
            ParrotCarrier parrotCarrier = ParrotCarrier.getEntities().get(event.getEntity());
            parrotCarrier.cancelCycle();
            ParrotCarrier.parrots.remove(parrotCarrier.getPlayer());
        }
    }
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (ParrotCarrier.getEntities().containsKey(event.getRightClicked())) {
            ParrotCarrier parrotCarrier = ParrotCarrier.getEntities().get(event.getRightClicked());

            if (parrotCarrier.currentState != ParrotState.WAITING) {
                return;
            }

            if (parrotCarrier.getPlayer() != event.getPlayer()) {
                return;
            }

            Inventory parrotInventory = Bukkit.createInventory(event.getPlayer(), inventorySize, "Put the items here!");
            parrotCarrier.parrotInventory = parrotInventory;
            event.getPlayer().openInventory(parrotInventory);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (ParrotCarrier.parrots.containsKey(event.getPlayer())) {
            ParrotCarrier parrotCarrier = ParrotCarrier.parrots.get(event.getPlayer());
            parrotCarrier.toSend = event.getInventory().getContents();
        }
    }
}
