package dev.dontblameme.utilsapi.main;

import dev.dontblameme.utilsapi.events.command.CommandEvent;
import dev.dontblameme.utilsapi.events.command.CommandUtils;
import dev.dontblameme.utilsapi.events.event.EventUtils;
import dev.dontblameme.utilsapi.inventorybuilder.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {

        checkVersion();

        instance = this;
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        listenCustomCommands();
    }

    private void checkVersion() {
        try {
            String githubURL = "https://github.com/DrachenfeuerHD/UtilsAPI/releases/latest";
            String currentVersion = getDescription().getVersion();
            URLConnection connection = new URL(githubURL).openConnection();

            connection.connect();

            InputStream is = connection.getInputStream();
            String url = String.valueOf(connection.getURL());

            is.close();

            String versionOnline = url.split("/")[url.split("/").length - 1].replace("v", "");

            if(!currentVersion.equals(versionOnline)) {
                getLogger().log(Level.WARNING, "There is a new version of UtilsAPI!");
                getLogger().log(Level.WARNING, "You can download the new version at {0}", githubURL);
            }

        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Could not check for new version! Error: {0}", e.getMessage());
        }
    }

    public static Main getInstance() {
        return instance;
    }

    private void listenCustomCommands() {
        EventUtils.registerEvent(PlayerCommandPreprocessEvent.class, e -> {
            String command = e.getMessage().split(" ")[0].replaceFirst("/", "");

            if(!CommandUtils.getCommands().containsKey(command)) return;

            String[] args = e.getMessage().replaceFirst("/" + command, "").trim().split(" ");

            e.setCancelled(true);
            CommandUtils.getCommands().get(command).accept(new CommandEvent(e, args));
        });
    }
}
