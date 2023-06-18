package me.xjuppo.parrotletter.commands.subcommands;

import me.xjuppo.parrotletter.commands.SubCommand;
import me.xjuppo.parrotletter.parrot.ParrotCarrier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class SendCommand extends SubCommand {
    public SendCommand(Plugin plugin, String name, String description, String usage, String permission) {
        super(plugin, name, description, usage, permission);
    }

    @Override
    public void executeCommand(CommandSender commandSender, Command command, List<String> args) {
        if (!(commandSender instanceof Player)) {
            plugin.getLogger().log(Level.INFO, "This command can only be used by a player!");
            return;
        }
        if (args.size() == 0) {
            commandSender.sendMessage(ChatColor.GRAY + "Correct usage: " + this.usage);
            return;
        }

        if (ParrotCarrier.parrots.containsKey((Player) commandSender)) {
            commandSender.sendMessage(ChatColor.GRAY + "Another parrot is already working for you");
            return;
        }

        Player player = Bukkit.getPlayer(args.get(0));

        if (player == null) {
            commandSender.sendMessage(ChatColor.GRAY + "This player is not online");
            return;
        }

        if (plugin.getConfig().getBoolean("use_call_item")) {
            Material mat = Material.matchMaterial(plugin.getConfig().getString("call_item"));

            if (mat == null) {
                plugin.getLogger().log(Level.WARNING, "Invalid cost item specified in config, using default");
                mat = Material.DIAMOND;
            }

            ItemStack itemToPay = new ItemStack(mat, plugin.getConfig().getInt("call_item_amount"));

            if (!((Player) commandSender).getInventory().containsAtLeast(itemToPay, plugin.getConfig().getInt("call_item_amount"))) {
                commandSender.sendMessage(ChatColor.GRAY + String.format("You have to pay %s %s to call a parrot", itemToPay.getAmount(), itemToPay.getType().name()));
                return;
            }

            ((Player) commandSender).getInventory().removeItem(itemToPay);
        }

        if (!commandSender.hasPermission("parrotletter.send.self")) {
            commandSender.sendMessage(ChatColor.GRAY + "You can't send something to yourself!");
            return;
        }
        Player commandSenderPlayer = (Player) commandSender;
        RayTraceResult senderResult = commandSenderPlayer.getWorld().rayTraceBlocks(commandSenderPlayer.getLocation().add(0, 1, 0), new Vector(0, 1, 0), 50);
        if (senderResult != null) {
            commandSender.sendMessage(ChatColor.GRAY + "You have to be outside to use this command!");
            return;
        }
        RayTraceResult receiverResult = player.getWorld().rayTraceBlocks(player.getLocation().add(0, 1, 0), new Vector(0, 1, 0), 50);
        if (receiverResult != null) {
            commandSender.sendMessage(ChatColor.GRAY + "The player you are sending an item to is not outside!");
            return;
        }

        ParrotCarrier parrotCarrier = new ParrotCarrier(commandSenderPlayer, player);
        parrotCarrier.startCycle();
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, String label, List<String> args) {
        return super.tabComplete(commandSender, command, label, args);
    }
}
