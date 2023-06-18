package me.xjuppo.parrotletter;

import jdk.tools.jlink.plugin.Plugin;
import me.xjuppo.parrotletter.commands.BaseTabCompleter;
import me.xjuppo.parrotletter.commands.CommandManager;
import me.xjuppo.parrotletter.listeners.EntityListener;
import me.xjuppo.parrotletter.listeners.TestListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class ParrotLetter extends JavaPlugin {

    public static ParrotLetter plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;
        ParrotConfigMessage.loadConfig(this);

        // Register events

        getServer().getPluginManager().registerEvents(new EntityListener(), this);

        // Register commands

        CommandManager commandManager = new CommandManager(this);
        BaseTabCompleter baseTabCompleter = new BaseTabCompleter(commandManager);

        getCommand("parrotletter").setExecutor(commandManager);
        getCommand("parrotletter").setTabCompleter(baseTabCompleter);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
