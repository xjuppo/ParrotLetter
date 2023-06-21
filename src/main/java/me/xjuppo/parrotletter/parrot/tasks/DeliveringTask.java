package me.xjuppo.parrotletter.parrot.tasks;

import me.xjuppo.parrotletter.ParrotConfigMessage;
import me.xjuppo.parrotletter.ParrotLetter;
import me.xjuppo.parrotletter.Utility.ParrotMessage;
import me.xjuppo.parrotletter.parrot.ParrotCarrier;
import me.xjuppo.parrotletter.parrot.ParrotState;
import me.xjuppo.parrotletter.parrot.ParrotTask;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Parrot;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class DeliveringTask extends ParrotTask {

    @Override
    public ParrotState executeTask(ParrotCarrier parrotCarrier) {
        Location teleportPosition = parrotCarrier.getSpawnLocation(parrotCarrier.getReceiver());

        Parrot parrot = (Parrot) parrotCarrier.getEntity();

        Bukkit.getScheduler().runTask(ParrotLetter.plugin, () -> {
            parrot.teleport(teleportPosition);
            parrot.playEffect(EntityEffect.ENTITY_POOF);
            parrotCarrier.getReceiver().playSound(parrot.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 0.8f);
            parrot.setSitting(false);
        });

        AtomicBoolean onGround = new AtomicBoolean(false);

        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(ParrotLetter.plugin, () -> {
            parrot.setVelocity(new Vector(0, -0.1f, 0));

            if (parrot.isOnGround()) {
                onGround.set(true);
            }
        }, 1L, 1L);

        while (!onGround.get()) {
            continue;
        }

        bukkitTask.cancel();

        Bukkit.getScheduler().runTask(ParrotLetter.plugin, () -> {
            parrot.setSitting(true);
        });
        parrotCarrier.getReceiver().sendMessage(ParrotConfigMessage.getMessage(parrotCarrier, ParrotMessage.DELIVERY_START_MESSAGE));

        try {
            TimeUnit.SECONDS.sleep(3);
        }
        catch (Exception e) {
            Bukkit.getLogger().log(Level.INFO, e.toString());
        }

        Bukkit.getScheduler().runTask(ParrotLetter.plugin, () -> {
            parrot.getWorld().dropItemNaturally(parrot.getLocation(), parrotCarrier.toSend);
            parrotCarrier.toSend = null;
        });

        try {
            TimeUnit.SECONDS.sleep(4);
        }
        catch (Exception e) {
            Bukkit.getLogger().log(Level.INFO, e.toString());
        }

        parrotCarrier.getReceiver().sendMessage(ParrotConfigMessage.getMessage(parrotCarrier, ParrotMessage.DELIVERY_DONE_MESSAGE));
        Bukkit.getScheduler().runTask(ParrotLetter.plugin, () -> {
            parrot.setSitting(false);
        });

        return ParrotState.FLYING_AWAY;
    }
}
