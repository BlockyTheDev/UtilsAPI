package dev.dontblameme.utilsapi.config.ingameconfig;

import dev.dontblameme.utilsapi.itembuilder.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.function.Consumer;

@Getter
@Setter
public class IngameConfigEntry {

    private String itemName;
    private Material material;
    private final String section;
    private String key;
    private String value;
    private ArrayList<String> lores = new ArrayList<>();
    private final String setValueTitle;
    private final String setValueMessage;
    private final Consumer<IngameConfigUpdateEvent> consumer;

    /**
     *
     * @param itemName Name for the ItemStack
     * @param material Material of the ItemStack
     * @param section Section of the entry
     * @param key Key of the entry
     * @param value Value of the entry
     * @param setValueTitle Title for the set value GUI
     * @param setValueMessage Message for the set value GUI, replacing %n% with the key of the entry
     * @param consumer Consumer for the set value GUI
     */
    public IngameConfigEntry(String itemName, Material material, String section, String key, String value, String setValueTitle, String setValueMessage, Consumer<IngameConfigUpdateEvent> consumer) {
        this.itemName = itemName;
        this.material = material;
        this.section = section;
        this.key = key;
        this.value = value;
        this.setValueTitle = setValueTitle;
        this.setValueMessage = setValueMessage;
        this.consumer = consumer;
    }

    /**
     *
     * @param itemName Name for the ItemStack
     * @param material Material of the ItemStack
     * @param key Key of the entry
     * @param value Value of the entry
     * @param setValueTitle Title for the set value GUI
     * @param setValueMessage Message for the set value GUI, replacing %n% with the key of the entry
     * @param consumer Consumer for the set value GUI
     */
    public IngameConfigEntry(String itemName, Material material, String key, String value, String setValueTitle, String setValueMessage, Consumer<IngameConfigUpdateEvent> consumer) {
        this(itemName, material, "", key, value, setValueTitle, setValueMessage, consumer);
    }

    /**
     *
     * @param lore Lore to add to the item
     */
    public void addLore(String lore) {
        lores.add(lore);
    }

    /**
     *
     * @return Returns the fully built entry item
     */
    public ItemStack getItem() {
        ItemBuilder builder = new ItemBuilder(material).name(itemName);

        if(!section.isEmpty())
            builder.addLore("&7Section: &e" + section);
        builder.addLore("&7Key: &e" + key);
        builder.addLore("&7Value: &e" + value);

        lores.forEach(builder::addLore);
        return builder.build();
    }

}
