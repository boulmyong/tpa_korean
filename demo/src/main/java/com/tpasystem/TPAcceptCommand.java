package com.tpasystem;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TPAcceptCommand implements CommandExecutor {

    private final TPASystem plugin;

    public TPAcceptCommand(TPASystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
            return true;
        }

        Player target = (Player) sender;
        TPAManager tpaManager = plugin.getTpaManager();
        TPARequest tpaRequest = tpaManager.getRequest(target.getUniqueId());

        if (tpaRequest == null) {
            target.sendMessage(ChatColor.RED + "받은 텔레포트 요청이 없습니다.");
            return true;
        }

        Player requester = tpaRequest.getRequester();
        if (requester == null || !requester.isOnline()) {
            target.sendMessage(ChatColor.RED + "요청을 보낸 플레이어가 오프라인 상태입니다.");
            tpaManager.removeRequest(target.getUniqueId());
            return true;
        }

        target.sendMessage(ChatColor.GREEN + "[TPA] " + ChatColor.WHITE + requester.getName() + "님의 텔레포트 요청을 수락했습니다. 3초 후 텔레포트됩니다.");
        requester.sendMessage(ChatColor.GREEN + "[TPA] " + ChatColor.WHITE + target.getName() + "님이 텔레포트 요청을 수락했습니다. 3초 후 텔레포트됩니다.");

        Location initialLocation = requester.getLocation();

        new BukkitRunnable() {
            int countdown = 3;

            @Override
            public void run() {
                if (!requester.isOnline() || !target.isOnline()) {
                    tpaManager.removeRequest(target.getUniqueId());
                    cancel();
                    return;
                }

                if (requester.getLocation().distanceSquared(initialLocation) > 1) {
                    requester.sendMessage(ChatColor.RED + "움직여서 텔레포트가 취소되었습니다.");
                    target.sendMessage(ChatColor.RED + requester.getName() + "님이 움직여서 텔레포트가 취소되었습니다.");
                    tpaManager.removeRequest(target.getUniqueId());
                    cancel();
                    return;
                }

                if (countdown <= 0) {
                    requester.teleport(target);
                    tpaManager.removeRequest(target.getUniqueId());
                    cancel();
                } else {
                    requester.sendMessage(ChatColor.GREEN + "" + countdown + "초 후 텔레포트됩니다...");
                    countdown--;
                }
            }
        }.runTaskTimer(plugin, 0, 20);

        return true;
    }
}
