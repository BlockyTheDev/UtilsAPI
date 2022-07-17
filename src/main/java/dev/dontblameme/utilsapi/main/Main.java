package dev.dontblameme.utilsapi.main;

import dev.dontblameme.utilsapi.config.ingameconfig.IngameConfigEntry;
import dev.dontblameme.utilsapi.config.ingameconfig.IngameConfigUpdateEvent;
import dev.dontblameme.utilsapi.events.command.CommandEvent;
import dev.dontblameme.utilsapi.events.command.CommandUtils;
import dev.dontblameme.utilsapi.events.command.CustomCommand;
import dev.dontblameme.utilsapi.events.event.EventUtils;
import dev.dontblameme.utilsapi.inventorybuilder.InventoryListener;
import dev.dontblameme.utilsapi.utils.TextParser;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private static Main instance;
    @Getter
    private static HashMap<Player, IngameConfigEntry> entriesForChat = new HashMap<>();

    @Override
    public void onEnable() {

        checkVersion();

        instance = this;
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        listenCustomCommands();
        listenChatInputIngameConfig();
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
                getLogger().log(Level.WARNING, "You can download the new version at {0}", url);
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
            String[] args = e.getMessage().replaceFirst("/" + command, "").trim().split(" ");
            List<CustomCommand> commands = CommandUtils.getCommands();

            for(CustomCommand c : commands) {
                if(c.getCommandName().equalsIgnoreCase(command)) {
                    CommandEvent event = new CommandEvent(e, args);

                    e.setCancelled(true);

                    if(!e.getPlayer().hasPermission(c.getPermission())) {
                        e.getPlayer().sendMessage(TextParser.parseHexAndCodes(c.getPermissionMessage()));
                        return;
                    }

                    c.getConsumer().accept(event);
                }
            }
        });
    }

    private void listenChatInputIngameConfig() {

        EventUtils.registerEvent(PlayerCommandPreprocessEvent.class, e -> {
            if(entriesForChat.containsKey(e.getPlayer())) {
                e.setCancelled(true);

                IngameConfigEntry entry = entriesForChat.get(e.getPlayer());
                String oldValue = entry.getValue();

                entry.setValue(e.getMessage());
                entriesForChat.remove(e.getPlayer());
                entry.getConsumer().accept(new IngameConfigUpdateEvent(entry.getKey(), oldValue, e.getMessage(), e.getPlayer()));
            }
        });

        EventUtils.registerEvent(AsyncPlayerChatEvent.class, e -> {
            if(entriesForChat.containsKey(e.getPlayer())) {
                e.setCancelled(true);

                IngameConfigEntry entry = entriesForChat.get(e.getPlayer());
                String oldValue = entry.getValue();

                entry.setValue(e.getMessage());
                entriesForChat.remove(e.getPlayer());
                entry.getConsumer().accept(new IngameConfigUpdateEvent(entry.getKey(), oldValue, e.getMessage(), e.getPlayer()));
            }
        });
    }

}