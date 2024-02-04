package fun.sunrisemc.derivedsell;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Plugin extends JavaPlugin {

    public final Logger LOGGER = Logger.getLogger(getName());

    private static Plugin instance = null;
    private static Economy economy = null;

    public void onEnable() {
        instance = this;

        if (!hookVault()) {
            return;
        }

        MaterialWorth.preCacheWorths();

        LOGGER.info("Plugin enabled.");
    }

    public void onDisable() {
        LOGGER.info("Plugin disabled.");
    }

    public static Plugin getInstance() {
        return instance;
    }

    public static Economy getEconomy() {
        return economy;
    }

    private boolean hookVault() {
        LOGGER.info("Hooking Vault...");

        Server server = getServer();
        RegisteredServiceProvider<Economy> economyProvider = server.getServicesManager().getRegistration(Economy.class);

        economy = economyProvider != null ? economyProvider.getProvider() : null;
        if (economy == null) {
            LOGGER.severe("Vault not found. Disabling plugin.");
            server.getPluginManager().disablePlugin(this);
            return false;
        }

        return true;
    }
}
