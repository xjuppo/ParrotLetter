package me.xjuppo.parrotletter;

import me.xjuppo.parrotletter.Utility.ParrotMessage;
import me.xjuppo.parrotletter.parrot.ParrotCarrier;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class ParrotConfigMessage {

    static HashMap<ParrotMessage, String> messages = new HashMap<>();
    static List<String> parrotNames = new ArrayList<>();

    public ParrotConfigMessage() {
    }

    public static void loadConfig(Plugin plugin) {

        plugin.saveDefaultConfig();

        for (ParrotMessage parrotMessage : ParrotMessage.values()) {
            messages.put(parrotMessage, plugin.getConfig().getString(String.format("text.%s", parrotMessage.name().toLowerCase())));

        }
        Bukkit.getLogger().log(Level.INFO, plugin.getConfig().getStringList("parrot_names").toString());
        parrotNames = plugin.getConfig().getStringList("parrot_names");
    }

    public static String getMessage(ParrotCarrier parrotCarrier, ParrotMessage parrotMessage) {
        String message = messages.get(parrotMessage);
        message = message.replace("%p", parrotCarrier.getParrotName());
        message = message.replace("%s", parrotCarrier.getPlayer().getName());
        message = message.replace("%r", parrotCarrier.getReceiver().getName());

        return message;
    }

    public static String getRandomName() {
        Random random = new Random();
        return parrotNames.get(random.nextInt(parrotNames.size()));
    }
}
