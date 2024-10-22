package org.vindicterra.vindicterralib;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class VindicterraLib extends JavaPlugin {
    @Getter
    private static VindicterraLib instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getLogger().info("-- VindicterraLib Enabled! --");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("-- VindicterraLib Disabled! --");
    }
}