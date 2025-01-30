package org.vindicterra.vindicterralib.events.constructors;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
/**
 * A class that constructs an EntityDamageByEntityEvent.
 * <p>
 *     This class is used to create an EntityDamageByEntityEvent with the given parameters.
 *     This is because the EntityDamageByEntityEvent constructor is Unstable and may be changed in the future.
 */
public class EntityDamageByEntityEventConstructor {
    public static EntityDamageByEntityEvent entityDamageByEntityEvent(
            @NotNull final Entity damager,
            @NotNull final Entity damagee,
            final @NotNull EntityDamageEvent.DamageCause damageCause,
            final DamageSource damageSource,
            final double damage,
            final boolean isCritical
    ) {
        return new EntityDamageByEntityEvent(
                damager, damagee, damageCause, damageSource,
                Map.of(EntityDamageEvent.DamageModifier.BASE, damage),
                Map.of(),
                isCritical
        );
    }
}
