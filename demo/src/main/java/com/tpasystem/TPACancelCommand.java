package com.tpasystem;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPACancelCommand implements CommandExecutor {

    private final TPASystem plugin;

    public TPACancelCommand(TPASystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
            return true;
        }

        Player requester = (Player) sender;
        TPAManager tpaManager = plugin.getTpaManager();
        TPARequest requestToCancel = null;

        for (TPARequest tpaRequest : tpaManager.getRequests().values()) {
            if (tpaRequest.getRequester().equals(requester)) {
                requestToCancel = tpaRequest;
                break;
            }
        }

        if (requestToCancel == null) {
            requester.sendMessage(ChatColor.RED + "보낸 텔레포트 요청이 없습니다.");
            return true;
        }

        Player target = requestToCancel.getTarget();
        if (target != null && target.isOnline()) {
            target.sendMessage(ChatColor.RED + "[TPA] " + ChatColor.WHITE + requester.getName() + "님이 텔레포트 요청을 취소했습니다.");
        }

        requester.sendMessage(ChatColor.GREEN + "[TPA] " + ChatColor.WHITE + "텔레포트 요청을 취소했습니다.");
        tpaManager.removeRequest(target.getUniqueId());

        return true;
    }
}
