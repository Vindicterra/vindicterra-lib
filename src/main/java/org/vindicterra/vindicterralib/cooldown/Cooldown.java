package org.vindicterra.vindicterralib.cooldown;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.vindicterra.vindicterralib.utils.StringUtils;

@Getter
public class Cooldown extends BukkitRunnable {
    @Setter
    private long timeLeft;
    private final long initialTime;
    private final int duration;
    private final String reason;
    private final boolean showMessage;
    private final Player player;
    private long timeRan = 0L;
    
    public Cooldown(Player player, long initialTime, int duration, String reason, boolean showMessage) {
        this.player = player;
        this.initialTime = initialTime;
        this.duration = duration;
        this.timeLeft = duration;
        this.reason = reason;
        this.showMessage = showMessage;
    }
    
    @Override
    public void run() {
        if (timeRan >= duration) {
            sendOffCooldownMessage();
            remove();
            return;
        }
        timeRan++;
        timeLeft = (long) ((((double) initialTime / 1000) + duration)
            - ((double) System.currentTimeMillis() / 1000));
    }
    
    /**
     * @param message Message to send to player
     */
    public void sendActionBar(Component message) {
        if (!showMessage) {
            return;
        }
        player.sendActionBar(message);
    }
    
    /**
     * Send off cooldown message to player
     */
    public void sendOffCooldownMessage() {
        sendActionBar(Component.text(
            "You are no longer on cooldown for " + StringUtils.toFriendlyFormat(reason) + "!",
            NamedTextColor.GREEN)
        );
    }
    
    /**
     * Send on cooldown message to player
     */
    public void sendOnCooldownMessage() {
        sendActionBar(Component.text(
            String.format("You are on cooldown for %s seconds...", timeLeft),
            NamedTextColor.RED)
        );
    }
    
    public void remove() {
        CooldownManager.cooldowns.remove(this);
        this.cancel();
    }
}
