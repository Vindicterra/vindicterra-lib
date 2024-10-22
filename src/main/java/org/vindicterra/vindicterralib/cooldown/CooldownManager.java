package org.vindicterra.vindicterralib.cooldown;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.vindicterra.vindicterralib.VindicterraLib;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CooldownManager {
    public static final List<Cooldown> cooldowns = new ArrayList<>();
    /**
     * @param player Player to apply cooldown to
     * @param event Cancellable event thrown by player
     * @param seconds Duration of cooldown
     * @param reason Reason for cooldown
     * @param showMessage Whether to show cooldown message as actionbar
     * @return Status of new or existing cooldown
     */
    public static CooldownStatus handleCooldown(Player player, @Nullable Event event, int seconds, String reason, boolean showMessage) {
        UUID uuid = player.getUniqueId();
        
        if (player.hasPermission("vindicterraweapons.admin.cooldown-bypass")) {
            return CooldownStatus.OFF_COOLDOWN;
        }
        
        for (Cooldown cd : cooldowns) {
            if (cd.getPlayer().getUniqueId() != uuid) continue;
            if (!cd.getReason().equals(reason)) continue;
            long timeLeft = cd.getTimeLeft();
            if (timeLeft > 0) {
                cd.sendOnCooldownMessage();
                if (event instanceof Cancellable) {
                    ((Cancellable) event).setCancelled(true);
                }
                return CooldownStatus.ON_COOLDOWN;
            } else if (timeLeft == 0) {
                cd.remove();
                cd.sendOffCooldownMessage();
                return CooldownStatus.OFF_COOLDOWN;
            }
        }
        
        Cooldown cd = new Cooldown(player, System.currentTimeMillis(), seconds, reason, showMessage);
        cd.runTaskTimer(VindicterraLib.getInstance(), 0, 20);
        cooldowns.add(cd);
        
        return CooldownStatus.NEW_COOLDOWN;
    }
}
