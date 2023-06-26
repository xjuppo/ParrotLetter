package me.xjuppo.parrotletter.parrot.tasks;

import me.xjuppo.parrotletter.ParrotConfigMessage;
import me.xjuppo.parrotletter.ParrotLetter;
import me.xjuppo.parrotletter.Utility.ParrotMessage;
import me.xjuppo.parrotletter.parrot.ParrotCarrier;
import me.xjuppo.parrotletter.parrot.ParrotState;
import me.xjuppo.parrotletter.parrot.ParrotTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class WaitingTask extends ParrotTask {

    int millisecondsDelay;

    public WaitingTask() {
        try {
            millisecondsDelay = ParrotLetter.plugin.getConfig().getInt("fly_away_time");
        }
        catch (Exception e) {
            ParrotLetter.plugin.getLogger().severe("Couldn't load fly_away_time from config, setting default value");
            millisecondsDelay = 10000;
        }
    }

    @Override
    public ParrotState executeTask(ParrotCarrier parrotCarrier) {
        Parrot parrot = (Parrot) parrotCarrier.getEntity();

        Bukkit.getScheduler().runTask(ParrotLetter.plugin, () -> {
            parrot.setSitting(true);
            parrot.setAware(true);
            parrot.setAI(false);
        });

        long endWait = System.currentTimeMillis() + millisecondsDelay;

        while (System.currentTimeMillis() < endWait) {
            if (ParrotCarrier.parrots.get(parrotCarrier.getPlayer()).hasItemsInInventory()) {
                Bukkit.getScheduler().runTask(ParrotLetter.plugin, () -> {
                    parrotCarrier.getPlayer().sendMessage(ParrotConfigMessage.getMessage(parrotCarrier, ParrotMessage.ITEM_GOT_MESSAGE));
                });
                return ParrotState.FLYING_AWAY;
            }
        }

        if (ParrotCarrier.parrots.get(parrotCarrier.getPlayer()).parrotInventory != null) {
            Bukkit.getScheduler().runTask(ParrotLetter.plugin, () -> {
                parrotCarrier.getPlayer().closeInventory();
                for (ItemStack itemStack : parrotCarrier.parrotInventory.getContents()) {
                    if (itemStack != null) {
                        parrotCarrier.getPlayer().getWorld().dropItemNaturally(parrotCarrier.getEntity().getLocation(), itemStack);
                    }
                }
                parrotCarrier.getPlayer().sendMessage(ParrotConfigMessage.getMessage(parrotCarrier, ParrotMessage.TIME_EXPIRED_MESSAGE));
            });
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            catch (Exception e) {
                Bukkit.getLogger().info(e.toString());
            }
            parrotCarrier.toSend = new ItemStack[]{};
            return ParrotState.FLYING_AWAY;
        }

        parrotCarrier.getPlayer().sendMessage(ParrotConfigMessage.getMessage(parrotCarrier, ParrotMessage.TIME_EXPIRED_MESSAGE));
        return ParrotState.FLYING_AWAY;
    }
}
