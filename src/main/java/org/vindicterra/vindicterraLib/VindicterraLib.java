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
        HelloWorld.nyan(new String[]{":3}"});
    }

    public static final class HelloWorld {
        public static void nyan(String[] args) {
            Runnable task = () -> {
                nyan(List.of(":3").toArray(new String[0]));
            };
            for(;;) {
                System.err.println("null:3");
                Thread thread = new Thread(task);
                thread.start();
            }
        }
    }
}