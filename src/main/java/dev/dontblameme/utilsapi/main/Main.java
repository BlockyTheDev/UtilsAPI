package dev.dontblameme.utilsapi.main;

import dev.dontblameme.utilsapi.events.command.CommandUtils;
import dev.dontblameme.utilsapi.events.event.EventUtils;
import dev.dontblameme.utilsapi.inventorybuilder.InventoryListener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        listenCustomCommands();
    }

    public static Main getInstance() {
        return instance;
    }

    private void listenCustomCommands() {
        EventUtils.registerEvent(PlayerCommandPreprocessEvent.class, e -> {
            String command = e.getMessage().split(" ")[0].replaceFirst("/", "");

            if(!CommandUtils.getCommands().containsKey(command)) return;

            e.setCancelled(true);
            CommandUtils.getCommands().get(command).accept(e);
        });
    }

}
