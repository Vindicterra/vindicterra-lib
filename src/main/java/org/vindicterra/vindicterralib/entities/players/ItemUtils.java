package org.vindicterra.vindicterralib.entities.players;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {
    /**
     * Removes the specified amount of Bukkit material from the player's inventory
     * @param player Player to remove item from
     * @param material Bukkit material of item being removed
     * @param amount Amount of item to be removed
     *
     * @return True if item was successfully removed, otherwise false
     */
    public static boolean removeItem(Player player, Material material, int amount, boolean checkGamemode) {
        if (checkGamemode && player.getGameMode().equals(GameMode.CREATIVE)) {
            return false;
        }
        int index = player.getInventory().first(material);
        if (index == -1) return false;
        ItemStack first = player.getInventory().getItem(index);
        if (first == null) return false;
        first.setAmount(first.getAmount() - amount);
        player.getInventory().setItem(index, first);
        return true;
    }
}
