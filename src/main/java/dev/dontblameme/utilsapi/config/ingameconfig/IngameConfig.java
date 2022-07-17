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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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

        ArrayList<String> sections = new ArrayList<>();

        for(String key : config.getKeys(true)) {
            String value = config.getValue(key);
            String sectionValue = "";

            if(value.equals("MemorySection[path='" + key + "', root='YamlConfiguration']")) {
                sections.add(key);
                continue;
            }

            for(String section : sections) {
                if (key.startsWith(section + ".")) {
                    sectionValue = section;
                    key = key.replace(section + ".", "");
                }
            }

            String finalKey = key;
            String finalSectionValue = sectionValue;
            ingameConfigEntries.add(new IngameConfigEntry(key, Material.WRITABLE_BOOK, sectionValue, key, value, TextParser.parseHexAndCodes(editingTitle), TextParser.parseHexAndCodes(editingTitleMessage.replace("%n%", key)), e -> {
                if(finalSectionValue.isEmpty()) {
                    config.setValue(finalKey, e.getNewValue());
                } else {
                    config.setValue(finalSectionValue, finalKey, e.getNewValue());
                }
            }));
        }
    }

    /**
     * @apiNote Setups the command & makes the gui work
     */
    public void setup() {
        CommandUtils.registerCommand(new CustomCommand(command, permission, permissionMessage, e -> e.getEvent().getPlayer().openInventory(getInventory())));
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

                    new Thread(() -> {
                        while(Main.getEntriesForChat().containsKey(p)) {
                            p.sendTitle(TextParser.parseHexAndCodes(entry.getSetValueTitle()), TextParser.parseHexAndCodes(entry.getSetValueMessage()), 0, 50, 0);

                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {ex.printStackTrace();}
                        }
                    }).start();
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
