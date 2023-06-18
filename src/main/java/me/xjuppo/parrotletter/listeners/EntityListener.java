package me.xjuppo.parrotletter.listeners;

import me.xjuppo.parrotletter.ParrotLetter;
import me.xjuppo.parrotletter.parrot.ParrotCarrier;
import me.xjuppo.parrotletter.parrot.ParrotState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class EntityListener implements Listener {

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

            ItemStack itemClicked = event.getPlayer().getInventory().getItemInMainHand().clone();
            if (parrotCarrier.getPlayer() != event.getPlayer()) {
                return;
            }

            if ((itemClicked.getType() == Material.AIR) & (parrotCarrier.toSend == null)) {
                event.getPlayer().sendMessage("You can't send nothing!");
                return;
            }
            // Interaction counts twice (Somehow)
            if (itemClicked.getType() == Material.AIR) {
                return;
            }
            event.getPlayer().getInventory().removeItem(itemClicked);
            parrotCarrier.toSend = itemClicked;
        }
    }
}
