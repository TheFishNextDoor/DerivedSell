package fun.sunrisemc.derivedsell;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.PluginCommand;

import fun.sunrisemc.derivedsell.commands.Worth;

public class Commands {

    public final PluginCommand WORTH;

    public Commands(Plugin plugin) {
        this.WORTH = plugin.getCommand("worth");
        Worth worthCommandHandler = new Worth();
        this.WORTH.setExecutor(worthCommandHandler);
        this.WORTH.setTabCompleter(worthCommandHandler);
    }

    public static ArrayList<String> materialNames() {
        ArrayList<String> names = new ArrayList<>(Material.values().length);
        for (Material material : Material.values()) {
            names.add(material.name());
        }
        return names;
    }

    public static String titleCase(String str) {
        str = str.replace("_", " ");
        String[] words = str.split(" ");
        String titleCase = "";
        for (String word : words) {
            titleCase += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
        }
        return titleCase.trim();
    }

    public static int number(String numeral) {
        try {
            return Integer.parseInt(numeral);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
