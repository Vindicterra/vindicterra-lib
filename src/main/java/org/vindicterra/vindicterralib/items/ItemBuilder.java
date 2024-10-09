package org.vindicterra.vindicterralib.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.vindicterra.vindicterralib.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
        this.meta = itemStack.getItemMeta();
    }

    public ItemBuilder withName(String name) {
        this.meta.setDisplayName(StringUtils.formatHex(name));
        return this;
    }

    public ItemBuilder withAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder withLoreLine(String loreLine) {
        List<String> lore = this.meta.hasLore() ? this.meta.getLore() : new ArrayList<>();
        lore.add(StringUtils.formatHex(loreLine));
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilder withLore(String... loreLine) {
        List<String> lore = this.meta.hasLore() ? this.meta.getLore() : new ArrayList<>();
        for(String line : loreLine) {
            lore.add(StringUtils.formatHex(line));
        }

        this.meta.setLore(lore);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.meta);
        return this.itemStack;
    }
}

