package dev.dontblameme.utilsapi.config.ingameconfig;

import dev.dontblameme.utilsapi.config.customconfig.CustomConfig;
import dev.dontblameme.utilsapi.events.command.CommandUtils;
import dev.dontblameme.utilsapi.events.command.CustomCommand;
import dev.dontblameme.utilsapi.inventorybuilder.InventoryBuilder;
import dev.dontblameme.utilsapi.inventorybuilder.InventoryItem;
import dev.dontblameme.utilsapi.itembuilder.ItemBuilder;
import dev.dontblameme.utilsapi.main.Main;
import dev.dontblameme.utilsapi.multipageinventory.MultiPageButton;
import dev.dontblameme.utilsapi.multipageinventory.MultiPageInventory;
import dev.dontblameme.utilsapi.utils.TextParser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IngameConfig {

    @Getter
    private String command;
    private String permission;
    private String permissionMessage;
    private MultiPageInventory configInventory;
    private ArrayList<IngameConfigEntry> ingameConfigEntries = new ArrayList<>();
    private String title;
    private ItemStack nextPage = new ItemBuilder(Material.ARROW).name("&7Next Page").build();
    private ItemStack previousPage = new ItemBuilder(Material.ARROW).name("&7Previous Page").build();
    private CustomConfig config = null;
    private String editingTitle;
    private String editingTitleMessage;

    /**
     *
     * @param command The Command for the GUI to open
     * @param permission Permission for the command
     * @param permissionMessage Message when someone does not have enough permissions for the command
     * @param title Title for the GUI
     * @param config Config to use to sync
     * @param editingTitle Title when editing a value
     * @param editingTitleMessage Message when editing a value, replacing %n% with the key
     */
    public IngameConfig(String command, String permission, String permissionMessage, String title, CustomConfig config, String editingTitle, String editingTitleMessage) {
        this.title = title;
        this.command = command;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.config = config;
        this.editingTitle = editingTitle;
        this.editingTitleMessage = editingTitleMessage;
        configInventory = new MultiPageInventory();

        syncConfig();
    }

    /**
     *
     * @param command The Command for the GUI to open
     * @param permission Permission for the command
     * @param permissionMessage Message when someone does not have enough permissions for the command
     * @param title Title for the GUI
     */
    public IngameConfig(String command, String permission, String permissionMessage, String title) {
        this.title = title;
        this.command = command;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.editingTitle = "&7Please insert";
        this.editingTitleMessage = "&ePlease insert the new value for %n% in chat";
        configInventory = new MultiPageInventory();
    }

    /**
     *
     * @param command The Command for the GUI to open
     * @param permission Permission for the command
     * @param permissionMessage Message when someone does not have enough permissions for the command
     */
    public IngameConfig(String command, String permission, String permissionMessage) {
        this(command, permission, permissionMessage, "&7Ingame Config");
    }

    private void syncConfig() {
        Set<String> keys =  config.getKeys(true);

        for(String key : keys) {
            String[] keySplit = key.split("\\.");

            // Not wanted first sub-section
            if(!String.join(", ", keySplit).contains(",") && config.getValue(String.join(", ", keySplit)).equals("MemorySection[path='" + keySplit[0] + "', root='YamlConfiguration']"))
                continue;

            // No sub section
            if(keySplit.length == 1) {
                ingameConfigEntries.add(new IngameConfigEntry(keySplit[0], Material.WRITABLE_BOOK, keySplit[0], config.getValue(keySplit[0]), TextParser.parseHexAndCodes(editingTitle), TextParser.parseHexAndCodes(editingTitleMessage.replace("%n%", keySplit[0])), e -> {
                    config.setValue(keySplit[0], e.newValue());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> e.player().openInventory(getInventory()), 0);
                }));
                continue;
            }

            String keyName = keySplit[keySplit.length - 1];
            String keyPath = key.substring(0, key.length() - keyName.length() - 1);
            String value = config.getValue(keyName, keyPath);

            // Not wanted
            if(value.equals("MemorySection[path='" + keyPath + "." + keyName + "', root='YamlConfiguration']"))
                continue;

            // Valid sub section
            ingameConfigEntries.add(new IngameConfigEntry(keyName, Material.WRITABLE_BOOK, keyName, value, TextParser.parseHexAndCodes(editingTitle), TextParser.parseHexAndCodes(editingTitleMessage.replace("%n%", keyName)), e -> {
                config.setValue(keyName, e.newValue(), keyPath.split("\\."));
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> e.player().openInventory(getInventory()), 0);
            }, keyPath.split("\\.")));
        }
    }

    /**
     * @apiNote Setups the command & makes the gui work
     */
    public void setup() {
        CommandUtils.registerCommand(new CustomCommand(command, permission, permissionMessage, e -> e.event().getPlayer().openInventory(getInventory())));
    }

    /**
     *
     * @return The Inventory which will be opened on command execute
     */
    public Inventory getInventory() {
        configInventory = new MultiPageInventory();

        configInventory.globalButton(new MultiPageButton(nextPage, 32, MultiPageButton.ButtonType.NEXT, false));
        configInventory.globalButton(new MultiPageButton(previousPage, 30, MultiPageButton.ButtonType.PREVIOUS, false));

        ArrayList<IngameConfigEntry> configEntriesCopy = (ArrayList<IngameConfigEntry>) ingameConfigEntries.clone();
        int inventories = configEntriesCopy.size() / 36;

        for(int i=0;i<=inventories;i++) {
            InventoryBuilder builder = new InventoryBuilder(TextParser.parseHexAndCodes(title), 36);

            for(int f=0;f<36;f++) {
                if(configEntriesCopy.isEmpty()) continue;
                if(f == 30 && i != 0) continue;
                if(f == 32 && i != inventories) continue;

                IngameConfigEntry entry = configEntriesCopy.get(0);
                InventoryItem item = new InventoryItem(entry.getItem(), f, e -> {
                    Player p = (Player)e.getWhoClicked();

                    e.getWhoClicked().closeInventory();
                    Main.getEntriesForChat().put(p, entry);

                    ExecutorService es = Executors.newSingleThreadExecutor();

                    es.execute(() -> {
                        while(Main.getEntriesForChat().containsKey(p)) {
                            p.sendTitle(TextParser.parseHexAndCodes(entry.getSetValueTitle()), TextParser.parseHexAndCodes(entry.getSetValueMessage()), 0, 50, 0);

                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {ex.printStackTrace();}
                        }
                    });

                    es.submit(() -> Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> p.openInventory(getInventory()), 0L));
                });

                builder.addItem(item);
                configEntriesCopy.remove(entry);
            }
            configInventory.addPages(builder);
        }

        return configInventory.build();
    }

    /**
     *
     * @param entry Entry to add to the inventory
     */
    public void addEntry(IngameConfigEntry entry) {
        ingameConfigEntries.add(entry);
    }

    /**
     *
     * @param itemStack ItemStack for the next button
     */
    public void setNextPageButton(ItemStack itemStack) {
        nextPage = itemStack;
    }

    /**
     *
     * @param itemStack ItemStack for the previous button
     */
    public void setPreviousPageButton(ItemStack itemStack) {
        previousPage = itemStack;
    }

}
