package dev.dontblameme.utilsapi.inventorybuilder;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AnimatedInventoryItem {

    @Getter
    private ArrayList<ItemStack> items = new ArrayList<>();
    @Getter
    private final int slot;
    @Getter
    private final Consumer<InventoryClickEvent> handler;
    @Getter
    private int delay = 0;
    private int count = -1;

    public AnimatedInventoryItem(int slot, Consumer<InventoryClickEvent> handler, int delay, ItemStack... items) {
        this.slot = slot;
        this.handler = handler;
        this.delay = delay;
        this.items.addAll(List.of(items));
    }

    public AnimatedInventoryItem(int slot, ItemStack... items) {
        this(slot, null, 20, items);
    }

    public ItemStack getNextItem() {
        count++;

        if(count >= items.size())
            count = 0;

        return items.get(count);
    }

    public void addItem(ItemStack item) {
        this.items.add(item);
    }

    public void removeItem(ItemStack item) {
        this.items.remove(item);
    }

    public void clearItems() {
        this.items.clear();
    }

}
