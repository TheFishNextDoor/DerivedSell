package fun.sunrisemc.derivedsell;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.PluginCommand;

import fun.sunrisemc.derivedsell.commands.Sell;
import fun.sunrisemc.derivedsell.commands.Worth;
import fun.sunrisemc.derivedsell.commands.Worths;

public class Commands {

    public final PluginCommand WORTH;
    public final PluginCommand WORTHS;
    public final PluginCommand SELL;

    private static final String MONEY_PREFIX = "$";
    private static final String MONEY_SUFFIX = "";
    private static final int roundTo = 2;

    public Commands(Plugin plugin) {
        this.WORTH = plugin.getCommand("worth");
        Worth worthCommandHandler = new Worth();
        this.WORTH.setExecutor(worthCommandHandler);
        this.WORTH.setTabCompleter(worthCommandHandler);

        this.WORTHS = plugin.getCommand("worths");
        Worths worthsCommandHandler = new Worths();
        this.WORTHS.setExecutor(worthsCommandHandler);
        this.WORTHS.setTabCompleter(worthsCommandHandler);

        this.SELL = plugin.getCommand("sell");
        Sell sellCommandHandler = new Sell();
        this.SELL.setExecutor(sellCommandHandler);
        this.SELL.setTabCompleter(sellCommandHandler);
    }

    public static ArrayList<String> materialNames() {
        ArrayList<String> names = new ArrayList<>(Material.values().length);
        for (Material material : Material.values()) {
            names.add(material.toString().toLowerCase());
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

    public static String displayMoney(double value) {
        return MONEY_PREFIX + String.format("%." + roundTo + "f", value) + MONEY_SUFFIX;
    }
}
