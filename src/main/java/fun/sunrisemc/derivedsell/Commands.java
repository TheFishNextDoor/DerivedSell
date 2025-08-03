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
    private static final int ROUND_TO = 2;

    public Commands(DerivedSell plugin) {
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

    public static String displayMoney(double value) {
        return MONEY_PREFIX + String.format("%." + ROUND_TO + "f", value) + MONEY_SUFFIX;
    }
}