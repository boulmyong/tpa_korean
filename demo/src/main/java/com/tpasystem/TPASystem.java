package com.tpasystem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TPASystem extends JavaPlugin implements Listener {

    private TPAManager tpaManager;

    @Override
    public void onEnable() {
        // Load config
        saveDefaultConfig();
        int expiration = getConfig().getInt("request-expiration-seconds", 60);
        int cooldown = getConfig().getInt("command-cooldown-seconds", 30);

        tpaManager = new TPAManager(this, expiration, cooldown);
        getLogger().info("TPASystem has been enabled!");
        getCommand("tpa").setExecutor(new TPACommand(this));
        getCommand("tpa").setTabCompleter(new TPATabCompleter(this));
        getCommand("tpaccept").setExecutor(new TPAcceptCommand(this));
        getCommand("tpaccept").setTabCompleter(new TPATabCompleter(this));
        getCommand("tpdeny").setExecutor(new TPADenyCommand(this));
        getCommand("tpdeny").setTabCompleter(new TPATabCompleter(this));
        getCommand("tpcancel").setExecutor(new TPACancelCommand(this));

        getServer().getPluginManager().registerEvents(this, this);
    }

    public TPAManager getTpaManager() {
        return tpaManager;
    }

    @Override
    public void onDisable() {
        if (tpaManager != null) {
            tpaManager.cancelTasks();
        }
        getLogger().info("TPASystem has been disabled!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        tpaManager.handlePlayerQuit(player);
    }
}
