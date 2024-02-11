package fun.sunrisemc.derivedsell.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.derivedsell.Commands;
import fun.sunrisemc.derivedsell.MaterialWorth;

public class Worth implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Commands.materialNames();
        }
        return null;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Material material;
        int quantity;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                return false;
            }

            Player player = (Player) sender;
            ItemStack handItem = player.getInventory().getItemInMainHand();
            material = handItem.getType();
            quantity = handItem.getAmount();
        }
        else {
            material = Material.matchMaterial(args[0]);
            quantity = 1;
        }

        if (material == null) {
            sender.sendMessage(ChatColor.RED + "Invalid material.");
            return true;
        }

        Double worth = MaterialWorth.getWorth(material);
        if (worth == null) {
            sender.sendMessage(ChatColor.YELLOW + Commands.titleCase(material.name()) + " cannot be sold to the server.");
            return true;
        }

        if (quantity > 1) {
            sender.sendMessage(ChatColor.GREEN + "Stack of " + quantity + " " + Commands.titleCase(material.name()) + " is worth " + (worth * quantity) + " (" + worth + " per item).");
        }
        else {
            sender.sendMessage(ChatColor.GREEN + Commands.titleCase(material.name()) + " is worth " + worth + " per item.");
        }
        return true;
    }
}