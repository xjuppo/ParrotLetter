package me.xjuppo.parrotletter.parrot.tasks;

import jdk.tools.jlink.plugin.Plugin;
import me.xjuppo.parrotletter.ParrotConfigMessage;
import me.xjuppo.parrotletter.Utility.ParrotMessage;
import me.xjuppo.parrotletter.parrot.ParrotCarrier;
import me.xjuppo.parrotletter.parrot.ParrotState;
import me.xjuppo.parrotletter.parrot.ParrotTask;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.bukkit.util.Vector;

import java.util.logging.Level;

public class FallingTask extends ParrotTask {

    @Override
    public ParrotState executeTask(ParrotCarrier parrotCarrier) {

        Parrot parrot = (Parrot) parrotCarrier.getEntity();
        parrot.setAware(false);
        parrot.setVelocity(new Vector(0,-0.1,0));

        if (parrotCarrier.firstEnterState) {
            parrotCarrier.getPlayer().sendMessage(ParrotConfigMessage.getMessage(parrotCarrier, ParrotMessage.HELLO_MESSAGE));
            parrotCarrier.getPlayer().sendTitle(parrotCarrier.getParrotName(), "Right click me", 10, 30, 10);
            parrot.playEffect(EntityEffect.ENTITY_POOF);
            parrotCarrier.getPlayer().playSound(parrot.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 0.8f);
        }

        if (parrot.isOnGround()) {
            return ParrotState.WAITING;
        }
        else {
            return ParrotState.FALLING;
        }
    }
}
