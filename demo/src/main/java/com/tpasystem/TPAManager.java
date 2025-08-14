package com.tpasystem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TPAManager {

    private final TPASystem plugin;
    private final HashMap<UUID, TPARequest> requests = new HashMap<>();
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private BukkitTask expirationTask;

    private final int requestExpirationSeconds;
    private final int commandCooldownSeconds;

    public TPAManager(TPASystem plugin, int requestExpirationSeconds, int commandCooldownSeconds) {
        this.plugin = plugin;
        this.requestExpirationSeconds = requestExpirationSeconds;
        this.commandCooldownSeconds = commandCooldownSeconds;
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
        long timeLeft = (cooldowns.get(player.getUniqueId()) + TimeUnit.SECONDS.toMillis(commandCooldownSeconds)) - System.currentTimeMillis();
        return Math.max(0, TimeUnit.MILLISECONDS.toSeconds(timeLeft));
    }

    private void startExpirationTask() {
        expirationTask = new BukkitRunnable() {
            @Override
            public void run() {
                requests.entrySet().removeIf(entry -> {
                    TPARequest request = entry.getValue();
                    if (System.currentTimeMillis() - request.getRequestTime() > TimeUnit.SECONDS.toMillis(requestExpirationSeconds)) {
                        Player requester = request.getRequester();
                        Player target = request.getTarget();
                        if (requester != null && requester.isOnline()) {
                            requester.sendMessage(ChatColor.RED + "[TPA] " + ChatColor.WHITE + (target != null ? target.getName() : "알 수 없는 플레이어") + "님에게 보낸 텔레포트 요청이 만료되었습니다.");
                        }
                        if (target != null && target.isOnline()) {
                            target.sendMessage(ChatColor.RED + "[TPA] " + ChatColor.WHITE + (requester != null ? requester.getName() : "알 수 없는 플레이어") + "님에게 받은 텔레포트 요청이 만료되었습니다.");
                        }
                        return true;
                    }
                    return false;
                });
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    public void cancelTasks() {
        if (expirationTask != null) {
            expirationTask.cancel();
        }
    }

    public void handlePlayerQuit(Player player) {
        UUID playerUUID = player.getUniqueId();
        cooldowns.remove(playerUUID);

        // Player was the target of a request
        removeRequest(playerUUID);

        // Player was the requester
        requests.entrySet().removeIf(entry -> {
            TPARequest request = entry.getValue();
            if (request.getRequester().getUniqueId().equals(playerUUID)) {
                Player target = request.getTarget();
                if (target != null && target.isOnline()) {
                    target.sendMessage(ChatColor.RED + "[TPA] " + ChatColor.WHITE + player.getName() + "님이 서버를 나가셔서 텔레포트 요청이 취소되었습니다.");
                }
                return true;
            }
            return false;
        });
    }
}
