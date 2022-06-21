package dev.dontblameme.utilsapi.inventorybuilder;

import dev.dontblameme.utilsapi.utils.TextParser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InventoryBuilder {

    @Getter
    private final Inventory inventory;
    private final List<InventoryItem> items = new ArrayList<>();
    @Getter
    private final List<Option> options = new ArrayList<>();
    private ItemStack placeholder, border = null;

    /**
     *
     * @param name Name of the inventory
     * @param size Size of the inventory
     */
    public InventoryBuilder(String name, int size) {
        this.inventory = Bukkit.createInventory(null, size, TextParser.parseHexAndCodesForChat(name));
    }

    /**
     *
     * @param name Name of the inventory
     * @param inventoryType Type of the inventory
     */
    public InventoryBuilder(String name, CustomInventoryType inventoryType) {
        this.inventory = Bukkit.createInventory(null, InventoryType.valueOf(inventoryType.name()), TextParser.parseHexAndCodesForChat(name));
    }

    /**
     *
     * @param is ItemStack which should be added
     * @param position Position where the itemstack should be added
     * @return Instance of the current state of the builder
     */
    public InventoryBuilder addItem(ItemStack is, int position) {
        this.addItem(is, position,null);
        return this;
    }

    /**
     *
     * @param is ItemStack which should be added
     * @param position Position where the itemstack should be added
     * @param consumer Consumer of a click event on the item
     * @return Instance of the current state of the builder
     */
    public InventoryBuilder addItem(ItemStack is, int position, Consumer<InventoryClickEvent> consumer) {
        this.addItem(new InventoryItem(is, position, consumer));
        return this;
    }

    /**
     *
     * @param item InventoryItem which should be added
     * @return Instance of the current state of the builder
     */
    public InventoryBuilder addItem(InventoryItem item) {
        this.items.add(item);
        return this;
    }

    /**
     *
     * @param option Option which should be added
     * @return Instance of the current state of the builder
     */
    public InventoryBuilder addOption(Option option) {
        if(options.contains(option)) return this;

        options.add(option);
        return this;
    }

    /**
     *
     * @param placeholder ItemStack which should be used as a placeholder
     * @return Instance of the current state of the builder
     * @apiNote Placeholder will be inserted on every free space in the inventory on build
     */
    public InventoryBuilder placeholder(ItemStack placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    /**
     *
     * @param border ItemStack which should be used as a border
     * @return Instance of the current state of the builder
     * @apiNote Border will be inserted in a rectangle around the gui on build
     */
    public InventoryBuilder border(ItemStack border) {
        this.border = border;
        return this;
    }

    /**
     *
     * @return The inventory fully builded and ready to use
     */
    public Inventory build() {

        getInventory().getViewers().add(new ViewerHandler(this));

        getInventory().getViewers().add(new ViewerHandler((event, isPlayerInventory) -> {

            if(event instanceof InventoryDragEvent e && !isPlayerInventory && !getOptions().contains(Option.PUT_ITEM)) {
                e.setCancelled(true);
                return;
            }

            if(event instanceof InventoryClickEvent e) {

                if(isPlayerInventory) {

                    if(!getOptions().contains(Option.PUT_ITEM) && e.getClick().isShiftClick())
                        e.setCancelled(true);

                    // global disable due to spigot being ass again
                    if(!getOptions().contains(Option.TAKE_ITEM) && e.getAction() == InventoryAction.COLLECT_TO_CURSOR)
                        e.setCancelled(true);

                    return;
                }

                String action = e.getAction().name();

                if(!getOptions().contains(Option.TAKE_ITEM) && (action.contains("PICK") || action.contains("DROP") || action.contains("CLONE") || action.contains("HOTBAR") || action.contains("MOVE") || action.contains("SWAP") || action.contains("COLLECT") || action.contains("UNKNOWN")))
                    e.setCancelled(true);

                if(!getOptions().contains(Option.PUT_ITEM) && (action.contains("SWAP") || action.contains("HOTBAR") || action.contains("MOVE") || action.contains("PLACE")))
                    e.setCancelled(true);

            }


        }));

        items.forEach(inventoryItem -> getInventory().setItem(inventoryItem.getSlot(), inventoryItem.getItem()));

         if(this.border != null && getInventory().getType() == InventoryType.CHEST) {
             //top
             for(int i = 0; i < 9; i++) {
                 getInventory().setItem(i, this.border);
                 items.add(new InventoryItem(border, i));
             }

             //down
             for(int i = getInventory().getSize() - 9; i < getInventory().getSize(); i++) {
                 getInventory().setItem(i, this.border);
                 items.add(new InventoryItem(border, i));
             }

             //left 0, 9, 18, 27, 36...
             for(int i = 0; i < getInventory().getSize(); i++) {

                 if(i % 9 != 0) continue;

                 getInventory().setItem(i, border);
                 items.add(new InventoryItem(border, i));
             }

             //right 8, 17, 26, 35, 44..
             for(int i = 1; i < getInventory().getSize(); i++) {

                 if(i % 9 != 0) continue;

                 getInventory().setItem(i-1, border);
                 items.add(new InventoryItem(border, i-1));
             }

         }

        if(this.placeholder != null) {
            for(int i = 0; i < getInventory().getSize(); i++) {
                if (getInventory().getItem(i) == null) {
                    getInventory().setItem(i, this.placeholder);
                    items.add(new InventoryItem(placeholder, i));
                }
            }
        }

        return getInventory();
    }

    /**
     *
     * @param item ItemStack which will be searched for
     * @return InventoryItem of the itemstack found in the inventory
     */
    public InventoryItem getItemAt(ItemStack item) {
        return items.stream().filter(inventoryItem -> inventoryItem.getItem().equals(item)).findAny().orElse(null);
    }

    public int getSize() {
        return getInventory().getSize();
    }

    public enum Option {
        TAKE_ITEM,
        PUT_ITEM
    }
}
