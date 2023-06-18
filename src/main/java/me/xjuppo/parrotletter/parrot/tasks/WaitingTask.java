package me.xjuppo.parrotletter.parrot.tasks;

import me.xjuppo.parrotletter.ParrotConfigMessage;
import me.xjuppo.parrotletter.Utility.ParrotMessage;
import me.xjuppo.parrotletter.parrot.ParrotCarrier;
import me.xjuppo.parrotletter.parrot.ParrotState;
import me.xjuppo.parrotletter.parrot.ParrotTask;
import org.bukkit.entity.Parrot;

public class WaitingTask extends ParrotTask {

    @Override
    public ParrotState executeTask(ParrotCarrier parrotCarrier) {
        Parrot parrot = (Parrot) parrotCarrier.getEntity();

        parrot.setSitting(true);
        parrot.setAware(true);
        parrot.setAI(false);

        long endWait = System.currentTimeMillis() + 8000;

        while (System.currentTimeMillis() < endWait) {
            if (ParrotCarrier.parrots.get(parrotCarrier.getPlayer()).toSend != null) {
                parrotCarrier.getPlayer().sendMessage(ParrotConfigMessage.getMessage(parrotCarrier, ParrotMessage.ITEM_GOT_MESSAGE));
                return  ParrotState.FLYING_AWAY;
            }
        }

        parrotCarrier.getPlayer().sendMessage(ParrotConfigMessage.getMessage(parrotCarrier, ParrotMessage.TIME_EXPIRED_MESSAGE));
        return ParrotState.FLYING_AWAY;
    }
}
