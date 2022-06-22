package dev.dontblameme.utilsapi.inventorybuilder;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {

        if(e.getInventory().getViewers().size() < 2) return;

        if(e.getInventory().getViewers().get(1).getName().equals("InventoryBuilt")) {

            ViewerHandler handler = (ViewerHandler) e.getInventory().getViewers().get(1);

            // Disabled in every inventory because spigot is ass, and we can't find out which inventory was actually clicked
            handler.getConsumer().accept(e, false);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        ItemStack item = e.getCurrentItem();

        if(item == null) return;

        if(Objects.requireNonNull(e.getClickedInventory()).getType() == InventoryType.PLAYER && e.getWhoClicked().getOpenInventory().getBottomInventory().equals(e.getClickedInventory())){

            Inventory top = e.getWhoClicked().getOpenInventory().getTopInventory();

            handle(e, top, true);

            return;
        }

        if(e.getClickedInventory().getViewers().isEmpty()) return;

        handle(e, e.getClickedInventory(), false);

    }

    public void handle(InventoryClickEvent e, Inventory top, boolean isPlayerInventory) {

        if(top.getViewers().isEmpty()) return;

        if(top.getViewers().get(0).getName().equals("InventoryBuilt")) {

            // Fix for "after reload" click
            if(!(top.getViewers().get(0) instanceof ViewerHandler)) {
                e.getWhoClicked().closeInventory();
                return;
            }

            ViewerHandler entity = (ViewerHandler) top.getViewers().get(0);
            ViewerHandler optionalHandler = (ViewerHandler) top.getViewers().get(1);

            optionalHandler.getConsumer().accept(e, isPlayerInventory);

            InventoryBuilder builder = entity.getBuilder();
            ItemStack item = e.getCurrentItem();

            if(builder.getItemAt(item) == null || builder.getItemAt(item).getHandler() == null) return;

            if(Arrays.asList(builder.build().getContents()).contains(item) && Objects.equals(e.getClickedInventory(), top))
                builder.getItemAt(item).getHandler().accept(e);

        }
    }


}
