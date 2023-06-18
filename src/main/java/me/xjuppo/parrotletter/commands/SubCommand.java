package me.xjuppo.parrotletter.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

public abstract class SubCommand {

    public Plugin plugin;
    public String name;
    public String description;
    public String usage;
    public String permission;

    public SubCommand(Plugin plugin, String name, String description, String usage, String permission) {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
    }

    public void executeCommand(CommandSender commandSender, Command command, List<String> args) {
    }

    public List<String> tabComplete(CommandSender commandSender, Command command, String label, List<String> args) {
        return null;
    }
}
