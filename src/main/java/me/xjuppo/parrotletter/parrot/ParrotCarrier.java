package me.xjuppo.parrotletter.parrot;

import me.xjuppo.parrotletter.ParrotConfigMessage;
import me.xjuppo.parrotletter.ParrotLetter;
import me.xjuppo.parrotletter.parrot.tasks.DeliveringTask;
import me.xjuppo.parrotletter.parrot.tasks.FallingTask;
import me.xjuppo.parrotletter.parrot.tasks.FlyingAwayTask;
import me.xjuppo.parrotletter.parrot.tasks.WaitingTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ParrotCarrier {

    Entity parrot;
    Player player, receiver;

    int yDistance = 6, horizontalDistance = 6;

    public static HashMap<Player, ParrotCarrier> parrots = new HashMap<>();

    public ParrotState currentState = ParrotState.FALLING;
    ParrotState previousState = null;
    BukkitRunnable parrotTask;

    ParrotCarrier parrotCarrier;

    public ItemStack toSend = null;

    public static List<String> names = Arrays.asList("Jerome", "Mike", "Lucas");

    String parrotName;

    public boolean firstEnterState = true;

    public ParrotCarrier(Player player, Player receiver) {
        this.player = player;
        this.receiver = receiver;
        this.parrotCarrier = this;

        if (parrots.keySet().contains(player)) {
            if (parrots.get(player).currentState != ParrotState.EXIT) {
                return;
            }
        }

        this.parrotName = ParrotConfigMessage.getRandomName();
        this.parrot = player.getWorld().spawnEntity(this.getSpawnLocation(this.player), EntityType.PARROT);
        this.parrot.setInvulnerable(true);

        parrots.put(player, this);

        this.parrot.setGlowing(true);
        this.parrot.setCustomName(this.parrotName);
        this.parrot.setCustomNameVisible(true);
    }

    public static HashMap<Entity, ParrotCarrier> getEntities() {
        HashMap<Entity, ParrotCarrier> entities = new HashMap<>();
        for (ParrotCarrier parrotCarrier : parrots.values()) {
            entities.put(parrotCarrier.parrot, parrotCarrier);
        }
        return entities;
    }

    public Location getSpawnLocation(Player player) {

        Location playerLocation = player.getLocation();

        playerLocation.add(0, this.yDistance, 0);

        double randomFloat = Math.random();

        playerLocation.add(this.horizontalDistance*Math.cos(randomFloat*2*Math.PI), 0,
                this.horizontalDistance*Math.sin(randomFloat*2*Math.PI));

        return playerLocation;
    }

    public void startCycle() {
        this.parrotTask = new BukkitRunnable() {
            @Override
            public void run() {
                executeNextTask(parrotCarrier);
            }
        };
        this.parrotTask.runTaskAsynchronously(ParrotLetter.plugin);
    }

    public void cancelCycle() {
        if (!parrotTask.isCancelled()) {
            this.parrotTask.cancel();
        }
    }

    public void executeNextTask(ParrotCarrier parrotCarrier) {
        while (true) {
            if (parrotCarrier.parrot.isDead()) {
                parrots.remove(parrotCarrier.getPlayer());
                return;
            }

            parrotCarrier.previousState = parrotCarrier.currentState;

            try {
                TimeUnit.MILLISECONDS.sleep(40);
            }
            catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING, e.toString());
                return;
            }
            switch (parrotCarrier.currentState) {
                case FALLING:
                    FallingTask fallingTask = new FallingTask();
                    parrotCarrier.currentState = fallingTask.executeTask(parrotCarrier);
                    break;
                case WAITING:
                    WaitingTask waitingTask = new WaitingTask();
                    parrotCarrier.currentState = waitingTask.executeTask(parrotCarrier);
                    break;
                case FLYING_AWAY:
                    FlyingAwayTask flyingAwayTask = new FlyingAwayTask();
                    parrotCarrier.currentState = flyingAwayTask.executeTask(parrotCarrier);
                    break;
                case DELIVERING:
                    DeliveringTask deliveringTask = new DeliveringTask();
                    parrotCarrier.currentState = deliveringTask.executeTask(parrotCarrier);
                    break;
                case EXIT:
                    parrotCarrier.getEntity().getServer().getScheduler().runTask(ParrotLetter.plugin, () -> {
                        parrotCarrier.getEntity().remove();
                    });
                    parrots.remove(parrotCarrier.getPlayer());
                    return;
            }
            if (parrotCarrier.currentState != parrotCarrier.previousState) {
                parrotCarrier.firstEnterState = true;
            }
            else {
                parrotCarrier.firstEnterState = false;
            }
        }
    }

    public Player getPlayer() {
        return player;
    }
    public Entity getEntity() {
        return parrot;
    }

    public Player getReceiver() {
        return receiver;
    }

    public String getParrotName() {
        return parrotName;
    }
}
