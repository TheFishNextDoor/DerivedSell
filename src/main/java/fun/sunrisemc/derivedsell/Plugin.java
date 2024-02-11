package fun.sunrisemc.derivedsell;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import fun.sunrisemc.derivedsell.commands.Worth;
import net.milkbowl.vault.economy.Economy;

public class Plugin extends JavaPlugin {

    private static Plugin instance = null;
    private static Logger logger = null;
    private static Economy economy = null;
    private static Commands commands = null;

    public void onEnable() {
        instance = this;
        logger = getLogger();
        commands = new Commands(this);

        if (!hookVault()) {
            logger.severe("Vault not found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        MaterialWorth.preCacheWorths();

        getCommand("worth").setExecutor(new Worth());

        logger.info("Plugin enabled.");
    }

    public void onDisable() {
        logger.info("Plugin disabled.");
    }

    public static Plugin getInstance() {
        return instance;
    }

    public static Logger getPluginLogger() {
        return logger;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static Commands getCommands() {
        return commands;
    }

    private boolean hookVault() {
        logger.info("Hooking Vault...");

        Server server = getServer();
        RegisteredServiceProvider<Economy> economyProvider = server.getServicesManager().getRegistration(Economy.class);

        economy = economyProvider != null ? economyProvider.getProvider() : null;

        return economy != null;
    }
}
