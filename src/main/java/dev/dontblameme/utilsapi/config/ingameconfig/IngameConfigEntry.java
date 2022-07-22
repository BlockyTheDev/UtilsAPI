package dev.dontblameme.utilsapi.config.ingameconfig;

import dev.dontblameme.utilsapi.itembuilder.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
@Setter
public class IngameConfigEntry {

    private String itemName;
    private Material material;
    private final List<String> sections = new ArrayList<>();
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
     * @param key Key of the entry
     * @param value Value of the entry
     * @param setValueTitle Title for the set value GUI
     * @param setValueMessage Message for the set value GUI, replacing %n% with the key of the entry
     * @param consumer Consumer for the set value GUI
     * @param section Section of the entry
     */
    public IngameConfigEntry(String itemName, Material material, String key, String value, String setValueTitle, String setValueMessage, Consumer<IngameConfigUpdateEvent> consumer, String... section) {
        this(itemName, material, key, value, setValueTitle, setValueMessage, consumer, List.of(section));
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
     * @param section Section of the entry
     */
    public IngameConfigEntry(String itemName, Material material, String key, String value, String setValueTitle, String setValueMessage, Consumer<IngameConfigUpdateEvent> consumer, List<String> section) {
        this.itemName = itemName;
        this.material = material;
        this.sections.addAll(section);
        this.key = key;
        this.value = value;
        this.setValueTitle = setValueTitle;
        this.setValueMessage = setValueMessage;
        this.consumer = consumer;
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

        if(!sections.isEmpty())
            for(int i=0;i<sections.size();i++)
                builder.addLore("&7Sub-Section " + (i + 1) + ": &e" + sections.get(i));

        builder.addLore("&7Key: &e" + key);
        builder.addLore("&7Value: &e" + value);

        lores.forEach(builder::addLore);
        return builder.build();
    }

}
