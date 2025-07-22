package com.tpasystem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TPAManager {

    private final TPASystem plugin;
    private final HashMap<UUID, TPARequest> requests = new HashMap<>();
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public TPAManager(TPASystem plugin) {
        this.plugin = plugin;
        startExpirationTask();
    }

    public void addRequest(TPARequest request) {
        requests.put(request.getTarget().getUniqueId(), request);
        cooldowns.put(request.getRequester().getUniqueId(), System.currentTimeMillis());
    }

    public TPARequest getRequest(UUID target) {
        return requests.get(target);
    }

    public void removeRequest(UUID target) {
        requests.remove(target);
    }

    public HashMap<UUID, TPARequest> getRequests() {
        return requests;
    }

    public long getCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return 0;
        }
        long timeLeft = (cooldowns.get(player.getUniqueId()) + TimeUnit.SECONDS.toMillis(30)) - System.currentTimeMillis();
        return Math.max(0, TimeUnit.MILLISECONDS.toSeconds(timeLeft));
    }

    private void startExpirationTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                requests.entrySet().removeIf(entry -> {
                    TPARequest request = entry.getValue();
                    if (System.currentTimeMillis() - request.getRequestTime() > TimeUnit.SECONDS.toMillis(60)) {
                        Player requester = request.getRequester();
                        Player target = request.getTarget();
                        if (requester != null && requester.isOnline()) {
                            requester.sendMessage(ChatColor.RED + "[TPA] " + ChatColor.WHITE + target.getName() + "님에게 보낸 텔레포트 요청이 만료되었습니다.");
                        }
                        if (target != null && target.isOnline()) {
                            target.sendMessage(ChatColor.RED + "[TPA] " + ChatColor.WHITE + requester.getName() + "님에게 받은 텔레포트 요청이 만료되었습니다.");
                        }
                        return true;
                    }
                    return false;
                });
            }
        }.runTaskTimer(plugin, 20, 20);
    }
}
