package dev.dontblameme.utilsapi.multipageinventory;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

public class MultiPageButton implements Cloneable {

    @Getter
    private final ItemStack item;
    @Getter
    private final int inventorySlot;
    @Getter
    @Setter
    private int pageToDisplay;
    @Getter
    private final int pageToRedirect;
    @Getter
    private final ButtonType buttonType;
    @Getter
    @Setter
    private boolean shouldStayWhenInvalid = false;

    /**
     *
     * @return Clone of provided button
     */
    @Override
    public MultiPageButton clone() {
        try {
            return (MultiPageButton) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     *
     * @param item ItemStack used for the button
     * @param inventorySlot Slot in which this button should appear
     * @param pageToDisplay Page on which this button should appear
     * @param pageToRedirect Poge this button should redirect to
     * @apiNote Used for a local (page only) button which will prompt a specific page
     */
    public MultiPageButton(ItemStack item, int inventorySlot, int pageToDisplay, int pageToRedirect) {
        this.item = item;
        this.inventorySlot = inventorySlot;
        this.pageToDisplay = pageToDisplay;
        this.pageToRedirect = pageToRedirect;
        this.buttonType = ButtonType.NEXT;
    }

    /**
     *
     * @param item ItemStack used for the button
     * @param inventorySlot Slot in which this button should appear
     * @param pageToDisplay Page on which this button should appear
     * @param buttonType Type of which the button should be
     * @apiNote Used for a local button which will prompt a specific page using a ButtonType
     */
    public MultiPageButton(ItemStack item, int inventorySlot, int pageToDisplay, ButtonType buttonType) {
        this.item = item;
        this.inventorySlot = inventorySlot;
        this.pageToDisplay = pageToDisplay;
        this.pageToRedirect = 0;
        this.buttonType = buttonType;
    }

    /**
     *
     * @param item ItemStack used for the button
     * @param inventorySlot Slot in which this button should appear
     * @param buttonType Type of which the button should be
     * @param shouldStayWhenInvalid Should the button stay in the inventory even though it makes no sense? (Example: Next page button on the last page)
     * @apiNote Used for a global button which will prompt a specific page using a ButtonType
     */
    public MultiPageButton(ItemStack item, int inventorySlot, ButtonType buttonType, boolean shouldStayWhenInvalid) {
        this(item, inventorySlot, -1, buttonType);
        this.shouldStayWhenInvalid = shouldStayWhenInvalid;
    }

    public enum ButtonType {
        NEXT,
        PREVIOUS,
        FIRST,
        LAST
    }

}
