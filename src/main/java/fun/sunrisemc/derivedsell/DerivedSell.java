package fun.sunrisemc.derivedsell;

import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import fun.sunrisemc.derivedsell.commands.Sell;
import fun.sunrisemc.derivedsell.commands.Worth;
import fun.sunrisemc.derivedsell.commands.Worths;
import net.milkbowl.vault.economy.Economy;

public class DerivedSell extends JavaPlugin {

    private static DerivedSell instance = null;
    private static Economy economy = null;

    public void onEnable() {
        instance = this;

        registerCommands();

        if (!hookVault()) {
            logSevere("Vault not found. Disabling plugin.");
            disable();
            return;
        }

        logInfo("Plugin enabled.");
    }

    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static void logInfo(String message) {
        if (instance != null) {
            instance.getLogger().info(message);
        }
    }

    public static void logSevere(String message) {
        if (instance != null) {
            instance.getLogger().severe(message);
        }
    }

    private void disable() {
        logInfo("Disabling plugin...");
        getServer().getPluginManager().disablePlugin(this);
    }

    private void registerCommands() {
        PluginCommand worthCommand = this.getCommand("worth");
        Worth worthCommandHandler = new Worth();
        worthCommand.setExecutor(worthCommandHandler);
        worthCommand.setTabCompleter(worthCommandHandler);

        PluginCommand worthsCommand = this.getCommand("worths");
        Worths worthsCommandHandler = new Worths();
        worthsCommand.setExecutor(worthsCommandHandler);
        worthsCommand.setTabCompleter(worthsCommandHandler);

        PluginCommand sellCommand = this.getCommand("sell");
        Sell sellCommandHandler = new Sell();
        sellCommand.setExecutor(sellCommandHandler);
        sellCommand.setTabCompleter(sellCommandHandler);
    }

    private boolean hookVault() {
        getLogger().info("Hooking Vault...");

        Server server = getServer();
        RegisteredServiceProvider<Economy> economyProvider = server.getServicesManager().getRegistration(Economy.class);

        economy = economyProvider != null ? economyProvider.getProvider() : null;

        return economy != null;
    }
}