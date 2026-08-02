package net.vindicterra.vindicterralib.events.constructors;

import com.google.common.base.Functions;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EntityDamageByEntityEventConstructor {
    @SuppressWarnings({"deprecation", "UnstableApiUsage"})
    public static EntityDamageByEntityEvent entityDamageByEntityEvent(
        @NotNull final Entity damager,
        @NotNull final Entity damagee,
        final @NotNull EntityDamageEvent.DamageCause damageCause,
        final @NotNull DamageSource damageSource,
        final double damage,
        final boolean isCritical
    ) {
        return new EntityDamageByEntityEvent(
                damager, damagee, damageCause, damageSource,
                new HashMap<>(Map.of(EntityDamageEvent.DamageModifier.BASE, damage)),
                new HashMap<>(Map.of(EntityDamageEvent.DamageModifier.BASE, Functions.constant(1.0))),
                isCritical
        );
    }
}
