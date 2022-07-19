package dev.dontblameme.utilsapi.inventorybuilder;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if(e.getInventory().getViewers().size() != 3) return;

        if(e.getInventory().getViewers().get(1).getName().equals("InventoryBuilt")) {
            ViewerHandler handler = (ViewerHandler) e.getInventory().getViewers().get(1);

            // Disabled in every inventory because spigot is ass, and we can't find out which inventory was actually clicked
            handler.getHandler().accept(e, false);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if(e.getClickedInventory() == null) return;

        if(e.getClickedInventory().getType() == InventoryType.PLAYER && e.getWhoClicked().getOpenInventory().getBottomInventory().equals(e.getClickedInventory())){
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

            optionalHandler.getHandler().accept(e, isPlayerInventory);

            if(e.getCurrentItem() == null) return;

            InventoryBuilder builder = entity.getBuilder();
            ItemStack item = e.getCurrentItem();


            if(builder.getInventory() == null || !Arrays.asList(builder.getInventory().getContents()).contains(item) || !Objects.equals(e.getClickedInventory(), top)) return;

            if(builder.getAnimatedItemAt(item, e.getSlot()) != null) {
                builder.getAnimatedItemAt(item, e.getSlot()).getHandler().accept(e);
                return;
            }

            if(builder.getItemAt(item, e.getSlot()) != null && builder.getItemAt(item, e.getSlot()).getHandler() != null)
                builder.getItemAt(item, e.getSlot()).getHandler().accept(e);

        }
    }


}