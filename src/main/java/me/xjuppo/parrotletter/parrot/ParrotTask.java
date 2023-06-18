package me.xjuppo.parrotletter.parrot;

import jdk.tools.jlink.plugin.Plugin;

public abstract class ParrotTask {

    public ParrotState executeTask(ParrotCarrier parrotCarrier) {
        return ParrotState.WAITING;
    }
}
