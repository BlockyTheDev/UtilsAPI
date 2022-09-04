package dev.dontblameme.utilsapi.itembuilder;

import dev.dontblameme.utilsapi.utils.TextParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    private final Material material;
    private int amount = 1;
    private String name;
    private final List<String> lores = new ArrayList<>();
    private final List<ItemFlag> flags = new ArrayList<>();
    private final HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    private boolean unbreakable = false;
    private UUID skullOwner = null;

    /**
     *
     * @param material Material used for the item
     */
    public ItemBuilder(Material material) {
        this.material = material;
    }

    /**
     *
     * @param amount Amount of the item which should be given
     * @return Instance of the current state of the builder
     */
    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     *
     * @param name Name which should be used, null for §r (Empty) name
     * @return Instance of the current state of the builder
     */
    public ItemBuilder name(String name) {
        if(name == null || name.isEmpty()) {
            this.name = "§r";
            return this;
        }

        this.name = TextParser.parseHexAndCodes(name);
        return this;
    }

    /**
     *
     * @param lore String which should be added as a lore
     * @return Instance of the current state of the builder
     * @apiNote Every new addLore will be added in a seperate line
     */
    public ItemBuilder addLore(String lore) {
        if(lore == null || lore.isEmpty()) {
            lores.add("§a");
            return this;
        }

        lores.add(TextParser.parseHexAndCodes(lore));
        return this;
    }

    /**
     *
     * @param enchantment Enchantment which should be added
     * @param level Level of the enchantment that should be added
     * @return Instance of the current state of the builder
     * @apiNote Unsafe Enchantments are allowed
     */
    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    /**
     *
     * @param flag Custom ItemFlag which should be added
     * @return Instance of the current state of the builder
     */
    public ItemBuilder flag(ItemFlag flag) {
        flags.add(flag);
        return this;
    }

    /**
     *
     * @return Instance of the current state of the builder
     * @apiNote Makes the item unbreakable
     */
    public ItemBuilder unbreakable() {
        this.unbreakable = true;
        return this;
    }

    /**
     *
     * @param owner Owner of the skull (using NBT)
     * @return Instance of the current state of the builder
     */
    public ItemBuilder skullOwner(UUID owner) {
        this.skullOwner = owner;
        return this;
    }

    /**
     *
     * @return Ready to use itemstack with every custom value set
     */
    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if(meta == null)
            throw new IllegalStateException("ItemMeta is null, this should not happen");

        if(name != null)
            meta.setDisplayName(TextParser.parseHexAndCodes(name));

        if(!lores.isEmpty())
            meta.setLore(lores.stream().map(TextParser::parseHexAndCodes).toList());

        if(!flags.isEmpty())
            flags.forEach(meta::addItemFlags);

        if(!enchantments.isEmpty())
            enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));

        if(skullOwner != null)
            ((SkullMeta) meta).setOwningPlayer(Bukkit.getOfflinePlayer(skullOwner));

        meta.setUnbreakable(unbreakable);

        item.setItemMeta(meta);

        return item;
    }

}
