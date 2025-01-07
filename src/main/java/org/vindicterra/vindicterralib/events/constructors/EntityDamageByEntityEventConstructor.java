package org.vindicterra.vindicterralib.events.constructors;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class EntityDamageByEntityEventConstructor {
    public static EntityDamageByEntityEvent entityDamageByEntityEvent(
            @NotNull final Entity damager,
            @NotNull final Entity damagee,
            final @NotNull EntityDamageEvent.DamageCause damageCause,
            final double damage,
            final boolean isCritical
    ) {
        return new EntityDamageByEntityEvent(
                damager, damagee, damageCause,
                new EnumMap<>(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, damage)),
                new EnumMap<>(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, Functions.constant(-0.0))),
                isCritical
        );
    }
}
