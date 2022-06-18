package dev.dontblameme.utilsapi.inventorybuilder;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class InventoryItem {

    @Getter
    private final ItemStack item;
    @Getter
    private final int slot;
    @Getter
    private final Consumer<InventoryClickEvent> handler;

    /**
     *
     * @param item ItemStack used as item
     * @param slot Slot where the item should be
     */
    public InventoryItem(ItemStack item, int slot) {
        this(item, slot, null);
    }

    /**
     *
     * @param item ItemStack used as item
     * @param slot Slot where the item should be
     * @param handler Consumer for the click event on the item
     */
    public InventoryItem(ItemStack item, int slot, Consumer<InventoryClickEvent> handler) {
        this.item = item;
        this.slot = slot;
        this.handler = handler;
    }

}
