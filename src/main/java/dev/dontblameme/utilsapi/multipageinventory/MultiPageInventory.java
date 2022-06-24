package dev.dontblameme.utilsapi.multipageinventory;

import dev.dontblameme.utilsapi.inventorybuilder.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiPageInventory {

    private final HashMap<Integer, InventoryBuilder> pages = new HashMap<>();
    private final List<MultiPageButton> globalButtons = new ArrayList<>();

    /**
     *
     * @param inventories InventoryBuilder which should be added
     * @return Instance of the current state of the builder
     */
    public MultiPageInventory addPages(InventoryBuilder... inventories) {
        for(InventoryBuilder inv : inventories)
            pages.put(pages.size() + 1, inv);
        return this;
    }

    /**
     *
     * @param button Button which should be added globally
     * @return Instance of the current state of the builder
     */
    public MultiPageInventory globalButton(MultiPageButton button) {
        globalButtons.add(button);
        return this;
    }

    /**
     *
     * @param button Button which should be added for only one page
     * @return Instance of the current state of the builder
     */
    public MultiPageInventory button(MultiPageButton button) {

        if(button.getPageToDisplay() < 1) throw new IllegalArgumentException("Invalid MultiPageButton provided: Button has a page less than 1 (Provide Page as argument for non-global buttons)");

        pages.get(button.getPageToDisplay()).addItem(button.getItem(), button.getInventorySlot(), e -> handleClick(button, e));
        return this;
    }

    /**
     *
     * @return Page number one of the builder ready to be used
     */
    public Inventory build() {
        return getInventory(1);
    }

    /**
     *
     * @param pageNumber Number of which page should be returned
     * @return Inventory of the page ready to be used
     */
    public Inventory getInventory(int pageNumber) {
        if(pages.get(pageNumber) == null) throw new IllegalStateException("Page " + pageNumber + " does not exist in " + getClass().getSimpleName());

        if(!globalButtons.isEmpty())
            pages.forEach((key, value) -> globalButtons.forEach(btn -> {

                MultiPageButton button = new MultiPageButton(btn);

                button.setPageToDisplay(key);

                if(!button.isShouldStayWhenInvalid() &&
                        ((key <= 1 && (button.getButtonType() == MultiPageButton.ButtonType.PREVIOUS || button.getButtonType() == MultiPageButton.ButtonType.FIRST)) ||
                        (key >= pages.size() && (button.getButtonType() == MultiPageButton.ButtonType.NEXT || button.getButtonType() == MultiPageButton.ButtonType.LAST))))
                    return;

                value.addItem(button.getItem(), button.getInventorySlot(), e -> handleClick(button, e));
            }));

        return pages.get(pageNumber).build();
    }

    /**
     *
     * @return Every inventory currently created in a list
     */
    public List<Inventory> getEveryInventory() {
        return pages.values().stream().map(InventoryBuilder::build).toList();
    }

    /**
     *
     * @param material Material to check for in each inventory
     * @return List of inventory's containing this material
     */
    public List<Inventory> getInventoryWith(Material material) {
        return getEveryInventory().stream().filter(i -> i.contains(material)).toList();
    }

    /**
     *
     * @param itemStack ItemStack to check for in each inventory
     * @return List of inventory's containing this ItemStack
     */
    public List<Inventory> getInventoryWith(ItemStack itemStack) {
        return getEveryInventory().stream().filter(i -> i.contains(itemStack)).toList();
    }

    /**
     *
     * @param button Button of which the event should be handled
     * @param e The actual event which happened
     */
    private void handleClick(MultiPageButton button, InventoryClickEvent e) {

        if(button.getInventorySlot() != e.getSlot()) return;

        if(button.getPageToRedirect() > 0) {

            if(pages.get(button.getPageToRedirect()) == null) throw new IllegalArgumentException("Custom Button To-Page may not be a page which is not existing");

            e.getWhoClicked().openInventory(pages.get(button.getPageToRedirect()).build());
            return;
        }

        switch(button.getButtonType()) {
            case NEXT:
                if(button.getPageToDisplay() < pages.size())
                    e.getWhoClicked().openInventory(pages.get(button.getPageToDisplay() + 1).build());
                break;
            case PREVIOUS:
                if(button.getPageToDisplay() > 1)
                    e.getWhoClicked().openInventory(pages.get(button.getPageToDisplay() - 1).build());
                break;
            case FIRST:
                if(button.getPageToDisplay() > 1)
                    e.getWhoClicked().openInventory(pages.get(1).build());
                break;
            case LAST:
                if(button.getPageToDisplay() < pages.size())
                    e.getWhoClicked().openInventory(pages.get(pages.size()).build());
                break;
            default:
                throw new IllegalArgumentException("Invalid ButtonType provided");
        }
    }

}
