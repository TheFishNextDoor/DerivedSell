package fun.sunrisemc.derivedsell;

import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import fun.sunrisemc.derivedsell.commands.Sell;
import fun.sunrisemc.derivedsell.commands.Worth;
import fun.sunrisemc.derivedsell.commands.Worths;
import fun.sunrisemc.derivedsell.utils.CommandUtils;
import net.milkbowl.vault.economy.Economy;

public class DerivedSell extends JavaPlugin {

    private static DerivedSell instance = null;
    private static Economy economy = null;

    public void onEnable() {
        instance = this;

        CommandUtils.register(this, "worth", new Worth());
        CommandUtils.register(this, "worths", new Worths());
        CommandUtils.register(this, "sell", new Sell());

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

    public static void logWarning(String message) {
        if (instance != null) {
            instance.getLogger().warning(message);
        }
    }

    public static void logSevere(String message) {
        if (instance != null) {
            instance.getLogger().severe(message);
        }
    }

    private boolean hookVault() {
        logInfo("Hooking Vault...");

        Server server = getServer();
        RegisteredServiceProvider<Economy> economyProvider = server.getServicesManager().getRegistration(Economy.class);

        economy = economyProvider != null ? economyProvider.getProvider() : null;

        return economy != null;
    }

    private void disable() {
        getServer().getPluginManager().disablePlugin(this);
    }
}