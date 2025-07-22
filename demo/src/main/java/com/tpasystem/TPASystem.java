package com.tpasystem;

import org.bukkit.plugin.java.JavaPlugin;

public class TPASystem extends JavaPlugin {

    private TPAManager tpaManager;

    @Override
    public void onEnable() {
        tpaManager = new TPAManager(this);
        getLogger().info("TPASystem has been enabled!");
        getCommand("tpa").setExecutor(new TPACommand(this));
        getCommand("tpaccept").setExecutor(new TPAcceptCommand(this));
        getCommand("tpdeny").setExecutor(new TPADenyCommand(this));
        getCommand("tpcancel").setExecutor(new TPACancelCommand(this));
    }

    public TPAManager getTpaManager() {
        return tpaManager;
    }

    @Override
    public void onDisable() {
        getLogger().info("TPASystem has been disabled!");
    }
}
