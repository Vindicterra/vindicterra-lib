package org.vindicterra.vindicterraLib;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class VindicterraLib extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("-- VindicterraLib Enabled! --");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("-- VindicterraLib Disabled! --");
    }
}