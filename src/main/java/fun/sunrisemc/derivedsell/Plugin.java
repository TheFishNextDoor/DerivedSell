package fun.sunrisemc.derivedsell;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import fun.sunrisemc.derivedsell.commands.Worth;
import net.milkbowl.vault.economy.Economy;

public class Plugin extends JavaPlugin {

    private static Plugin instance = null;
    private static Economy economy = null;
    private static Commands commands = null;

    public void onEnable() {
        instance = this;
        commands = new Commands(this);

        if (!hookVault()) {
            getLogger().severe("Vault not found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        MaterialWorth.setWorth(Material.OAK_LOG, 1.0);
        MaterialWorth.setWorth(Material.COBBLESTONE, 2.0);
        MaterialWorth.setWorth(Material.COAL, 10.0);
        MaterialWorth.setWorth(Material.RAW_COPPER, 20.0);
        MaterialWorth.setWorth(Material.RAW_IRON, 40.0);
        MaterialWorth.setWorth(Material.RAW_GOLD, 250.0);
        MaterialWorth.setWorth(Material.DIAMOND, 1000.0);
        MaterialWorth.setWorth(Material.NETHERITE_SCRAP, 1500.0);

        MaterialWorth.preCacheWorths();

        getCommand("worth").setExecutor(new Worth());

        getLogger().info("Plugin enabled.");
    }

    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    public static Plugin getInstance() {
        return instance;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static Commands getCommands() {
        return commands;
    }

    private boolean hookVault() {
        getLogger().info("Hooking Vault...");

        Server server = getServer();
        RegisteredServiceProvider<Economy> economyProvider = server.getServicesManager().getRegistration(Economy.class);

        economy = economyProvider != null ? economyProvider.getProvider() : null;

        return economy != null;
    }
}
