package me.xjuppo.parrotletter.commands;

import me.xjuppo.parrotletter.commands.subcommands.HelpCommand;
import me.xjuppo.parrotletter.commands.subcommands.SendCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class CommandManager implements CommandExecutor {

    HashMap<String, SubCommand> subCommands = new HashMap<>();
    public static List<String> helpPages;
    Plugin plugin;
    int pageLength = 5;

    public CommandManager(Plugin plugin) {

        this.plugin = plugin;

        // Register SubCommands Here

        this.registerCommand(new SendCommand(
                plugin,
                "send",
                "Sends a parrot to your location",
                "/parrotletter send <player>",
                "parrotletter.send"
        ));

        this.registerCommand(new HelpCommand(
                plugin,
                "help",
                "Shows the help page",
                "/parrotletter help <page>",
                "parrotletter.help"
        ));

        // Initialize helpCommand

        StringBuilder stringBuilder = new StringBuilder();

        List<String> pages = new ArrayList<>();

        int commandCounter = 0;

        for (SubCommand subCommand : this.subCommands.values()) {
            stringBuilder.append(ChatColor.GRAY + "Name: " + ChatColor.RESET);
            stringBuilder.append(subCommand.name);
            stringBuilder.append("\n");
            stringBuilder.append(ChatColor.GRAY + "Usage: " + ChatColor.RESET);
            stringBuilder.append(subCommand.usage);
            stringBuilder.append("\n");
            stringBuilder.append(ChatColor.GRAY + "Description: " + ChatColor.RESET);
            stringBuilder.append(subCommand.description);
            stringBuilder.append("\n");
            stringBuilder.append(ChatColor.GRAY + "Permission: " + ChatColor.RESET);
            stringBuilder.append(subCommand.permission);
            stringBuilder.append("\n");
            stringBuilder.append(ChatColor.GRAY + "-----" + ChatColor.RESET);
            stringBuilder.append("\n");
            commandCounter++;

            if (commandCounter >= this.pageLength) {
                pages.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                commandCounter = 0;
            }
        }

        if (commandCounter != 0) {
            pages.add(stringBuilder.toString());
        }

        helpPages = pages;
    }

    public void registerCommand(SubCommand command) {
        this.subCommands.put(command.name, command);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        List<String> subCommandArgs = Arrays.asList(args).subList(1, args.length);

        if (!this.subCommands.keySet().contains(args[0])) {
            return false;
        }

        SubCommand subCommand = this.subCommands.get(args[0]);

        if (commandSender.hasPermission(subCommand.permission)) {
            subCommand.executeCommand(commandSender, command, subCommandArgs);
        }
        else {
            commandSender.sendMessage(String.format("You do not have the required permission (%s) to use this command", subCommand.permission));
        }

        return true;
    }

    public HashMap<String, SubCommand> getSubCommands() {
        return subCommands;
    }
}
