package com.tpasystem;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPADenyCommand implements CommandExecutor {

    private final TPASystem plugin;

    public TPADenyCommand(TPASystem plugin) {
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
        if (requester != null && requester.isOnline()) {
            requester.sendMessage(ChatColor.RED + "[TPA] " + ChatColor.WHITE + target.getName() + "님이 텔레포트 요청을 거절했습니다.");
        }

        target.sendMessage(ChatColor.GREEN + "[TPA] " + ChatColor.WHITE + "텔레포트 요청을 거절했습니다.");
        tpaManager.removeRequest(target.getUniqueId());

        return true;
    }
}
