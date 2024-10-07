package org.vindicterra.vindicterralib;

import org.bukkit.plugin.java.JavaPlugin;
@SuppressWarnings("unused")
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