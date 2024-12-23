package org.vindicterra.vindicterralib.entities.players;

import dev.lone.itemsadder.api.CustomStack;
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
     * @param checkGamemode Check if player is in creative
     *
     * @return Result of attempting to remove the item
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
    
    /**
     * Removes the specified item from the player's inventory
     * @param player Not-null Player to remove item from
     * @param item Not-null ItemStack, with amount to remove already set, to remove
     * @param checkGamemode Check if player is in creative
     *
     * @return Result of attempting to remove the item
     */
    public static ItemRemoveResult removeItem(@NotNull Player player, @NotNull ItemStack item, boolean checkGamemode) {
        if (checkGamemode && player.getGameMode().equals(GameMode.CREATIVE)) {
            return ItemRemoveResult.SUCCESS;
        }
        
        boolean match = false;
        int total = 0;
        for (ItemStack invItem : player.getInventory().getStorageContents()) {
            if (invItem == null) continue;
            if (!invItem.isSimilar(item)) continue;
            match = true;
            total += invItem.getAmount();
        }
        
        if (!match) {
            return ItemRemoveResult.FAIL;
        }
        
        if (total < item.getAmount()) {
            return ItemRemoveResult.INVALID_AMOUNT;
        }
        
        player.getInventory().removeItem(item);
        return ItemRemoveResult.SUCCESS;
    }
    
    public static ItemRemoveResult removeCustomItem(@NotNull Player player, @NotNull ItemStack item, boolean checkGamemode) {
        if (checkGamemode && player.getGameMode().equals(GameMode.CREATIVE)) {
            return ItemRemoveResult.SUCCESS;
        }
        
        CustomStack stack = CustomStack.byItemStack(item);
        if (stack == null) return ItemRemoveResult.FAIL;
        
        boolean match = false;
        int total = 0;
        for (ItemStack invItem : player.getInventory().getStorageContents()) {
            if (invItem == null) continue;
            
            CustomStack invCStack = CustomStack.byItemStack(invItem);
            if (invCStack == null) continue;
            
            if (!invCStack.getNamespacedID().equalsIgnoreCase(stack.getNamespacedID())) continue;
            
            match = true;
            total += invItem.getAmount();
        }
        
        if (!match) {
            return ItemRemoveResult.FAIL;
        }
        if (total < stack.getItemStack().getAmount()) {
            return ItemRemoveResult.INVALID_AMOUNT;
        }
        
        player.getInventory().removeItem(stack.getItemStack());
        return ItemRemoveResult.SUCCESS;
    }
}
