package org.vindicterra.vindicterralib.cooldown;

import org.bukkit.entity.Player;

import java.util.List;

public class CooldownUtils {
    /**
     * @param player Player to check for cooldown
     * @return True if player is on cooldown, otherwise false
     * @apiNote This method will return if the player is on cooldown for <strong>ANY</strong> reason!
     * To check for a specific reason, use {@link CooldownUtils#isOnCooldownForReason(Player, String)}
     */
    public static boolean isOnCooldown(Player player) {
        List<Cooldown> cooldowns = CooldownManager.cooldowns;
        for (Cooldown cd : cooldowns) {
            if (!cd.getPlayer().getUniqueId().equals(player.getUniqueId())) continue;
            return true;
        }
        return false;
    }
    
    /**
     * @param player Player to check for cooldown
     * @param reason Reason for cooldown
     * @return True if player is on cooldown for specified reason, otherwise false
     * @apiNote This method will return if the player is on cooldown for a <strong>SPECIFIC</strong> reason!
     * To check for any reason, use {@link CooldownUtils#isOnCooldown(Player)}
     */
    public static boolean isOnCooldownForReason(Player player, String reason) {
        if (!isOnCooldown(player)) return false;
        for (Cooldown cd : CooldownManager.cooldowns) {
            if (!cd.getPlayer().getUniqueId().equals(player.getUniqueId())) continue;
            if (!cd.getReason().equals(reason)) continue;
            return true;
        }
        return false;
    }
}
