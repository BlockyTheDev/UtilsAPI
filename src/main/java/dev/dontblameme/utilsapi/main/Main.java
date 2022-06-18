package dev.dontblameme.utilsapi.main;

import dev.dontblameme.utilsapi.inventorybuilder.InventoryListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
    }

    public static Main getInstance() {
        return instance;
    }

}
