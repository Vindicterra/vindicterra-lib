package org.vindicterra.vindicterralib.entities.players;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {
    /**
     * Removes the specified amount of Bukkit material from the player's inventory
     * @param player Player to remove item from
     * @param material Bukkit material of item being removed
     * @param amount Amount of item to be removed
     */
    public static void removeItem(Player player, Material material, int amount) {
        int index = player.getInventory().first(material);
        if (index == -1) return;
        ItemStack first = player.getInventory().getItem(index);
        if (first == null) return;
        first.setAmount(first.getAmount() - amount);
        player.getInventory().setItem(index, first);
    }
}
