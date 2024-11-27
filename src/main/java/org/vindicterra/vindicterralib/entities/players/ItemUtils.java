package org.vindicterra.vindicterralib.entities.players;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.vindicterra.vindicterralib.items.ItemRemoveResult;

public class ItemUtils {
    /**
     * Removes the specified amount of Bukkit material from the player's inventory
     * @param player Not-null Player to remove item from
     * @param material Not-null Bukkit material of item being removed
     * @param amount Amount of item to be removed
     *
     * @return True if item was successfully removed, otherwise false
     */
    public static ItemRemoveResult removeItem(@NotNull Player player, @NotNull Material material, int amount, boolean checkGamemode) {
        if (checkGamemode && player.getGameMode().equals(GameMode.CREATIVE)) {
            return ItemRemoveResult.SUCCESS;
        }
        if (!player.getInventory().contains(material)) {
            return ItemRemoveResult.FAIL;
        }
        if (!player.getInventory().contains(material, amount)) {
            return ItemRemoveResult.INVALID_AMOUNT;
        }
        
        player.getInventory().removeItem(new ItemStack(material, amount));
        return ItemRemoveResult.SUCCESS;
    }
}
