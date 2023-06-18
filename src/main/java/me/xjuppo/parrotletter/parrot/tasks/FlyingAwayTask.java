package me.xjuppo.parrotletter.parrot.tasks;

import jdk.tools.jlink.plugin.Plugin;
import me.xjuppo.parrotletter.parrot.ParrotCarrier;
import me.xjuppo.parrotletter.parrot.ParrotState;
import me.xjuppo.parrotletter.parrot.ParrotTask;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Parrot;
import org.bukkit.util.Vector;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class FlyingAwayTask extends ParrotTask {

    float horizontalSpeed = 0.1f;

    @Override
    public ParrotState executeTask(ParrotCarrier parrotCarrier) {

        Parrot parrot = (Parrot) parrotCarrier.getEntity();

        parrot.setAI(true);
        parrot.setAware(false);
        parrot.setSitting(false);
        parrot.setGliding(true);

        Vector vector = new Vector();
        vector.setY(0.1f);
        double randomFloat = Math.random();
        vector.setX(Math.cos(randomFloat*2*Math.PI)*this.horizontalSpeed);
        vector.setZ(Math.sin(randomFloat*2*Math.PI)*this.horizontalSpeed);

        long end = System.currentTimeMillis() + 3000;

        while (System.currentTimeMillis() < end) {
            if (parrotCarrier.toSend == null) {
                parrot.setRotation(-parrotCarrier.getReceiver().getLocation().getYaw(), parrotCarrier.getReceiver().getLocation().getPitch());
            }
            else {
                parrot.setRotation(parrotCarrier.getPlayer().getLocation().getYaw(), parrotCarrier.getPlayer().getLocation().getPitch());
            }
            parrot.setVelocity(vector);
        }

        parrot.playEffect(EntityEffect.ENTITY_POOF);

        try {
            TimeUnit.MILLISECONDS.sleep(200);
        }
        catch (Exception e) {
            Bukkit.getLogger().log(Level.INFO, e.toString());
        }

        if (parrotCarrier.toSend == null) {
            return ParrotState.EXIT;
        }
        else {
            return ParrotState.DELIVERING;
        }
    }
}
