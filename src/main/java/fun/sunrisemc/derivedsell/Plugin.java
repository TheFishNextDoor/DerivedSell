package fun.sunrisemc.derivedsell;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    public final Logger LOGGER = Logger.getLogger(getName());

    private static Plugin instance;

    public void onEnable() {
        instance = this;

        for (Material material : Material.values()) {
            MaterialWorth.getWorth(material, 1);
        }

        LOGGER.info("Plugin enabled.");
    }

    public void onDisable() {
        LOGGER.info("Plugin disabled.");
    }

    public static Plugin getInstance() {
        return instance;
    }
}
