package org.vindicterra.vindicterralib.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.vindicterra.vindicterralib.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * an item builder class allowing for easy item creation
 */
@SuppressWarnings("ALL")
public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta meta;
    /**
     * @param material the material of the item
     */
    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
        this.meta = itemStack.getItemMeta();
    }
    /**
     * param name the name of the item
     */
    public ItemBuilder withName(String name) {
        this.meta.setDisplayName(StringUtils.formatHex(name));
        return this;
    }
    /**
     * @param amount the amount of the item
     */
    public ItemBuilder withAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }
    /**
     * @param loreLine the lore line of the item (adds to the lore)
     */
    public ItemBuilder withLoreLine(String loreLine) {
        List<String> lore = this.meta.hasLore() ? this.meta.getLore() : new ArrayList<>();
        lore.add(StringUtils.formatHex(loreLine));
        this.meta.setLore(lore);
        return this;
    }
    /**
     * uses an elipsis to allow for multiple lore lines
     * @param loreLine the lore line of the item (adds to the lore)
     */
    public ItemBuilder withLore(String... loreLine) {
        List<String> lore = this.meta.hasLore() ? this.meta.getLore() : new ArrayList<>();
        for(String line : loreLine) {
            lore.add(StringUtils.formatHex(line));
        }

        this.meta.setLore(lore);
        return this;
    }
    /**
     * builds the item and returns it
     */
    public ItemStack build() {
        this.itemStack.setItemMeta(this.meta);
        return this.itemStack;
    }
}

