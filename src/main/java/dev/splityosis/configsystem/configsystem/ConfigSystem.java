package dev.splityosis.configsystem.configsystem;

import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigSystem extends JavaPlugin {

    public static JavaPlugin plugin;

    public static void setup(JavaPlugin javaPlugin){
        if (plugin != null) return;
        plugin = javaPlugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
    }

    @Override
    public void onDisable() {}
}
